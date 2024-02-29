package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.HydrometryService;
import dk.dmp.vanda.hydro.Station;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HydrometryServiceTest {
    @Mock JsonStreamService streamLayer;
    HydrometryService service;
    Station a;

    @BeforeEach
    void setUp() {
        service = new dk.dmp.vanda.hydro.httpjson.HydrometryService(streamLayer);

        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 25832);
        JsonStation s = new JsonStation();
        s.setStationUid("2e76caf9-d772-4c07-a6f1-0b7b4cf4d187");
        s.setStationId("61000181");
        s.setOperatorStationId("610181");
        s.setOldStationNumber("61000399");
        s.setLocationType("Vandløb");
        s.setLocationTypeSc(1);
        s.setStationOwnerCvr("DK25798376");
        s.setStationOwnerName("Miljøstyrelsen");
        s.setOperatorCvr("12345678-9012");
        s.setOperatorName("Artificial Operator");
        s.setName("Tt Vålse Vig, Vålse Vig");
        s.setDescription("Opland = 27,01 km2");
        s.setLoggerId("41662");
        s.setLocation(gf.createPoint(new Coordinate(679796.2734,6091352.6536)));
        JsonMeasurementPoint m = new JsonMeasurementPoint();
        m.setNumber(1);
        s.setMeasurementPoints(Collections.singletonList(m));
        a = s;
    }

    @AfterEach
    void tearDown() throws Exception {
        ((dk.dmp.vanda.hydro.httpjson.HydrometryService)service).close();
    }

    @Test
    void testStationsNoParameters() throws IOException, InterruptedException, URISyntaxException {
        service.getStations().exec();
        verify(streamLayer).submit("stations");
    }

    @Test
    void testStationsSomeParameters() throws IOException, InterruptedException, URISyntaxException {
        service.getStations()
            .stationId("61000181")
            .operatorStationId("610181")
            .stationOwnerCvr("DK25798376")
            .operatorCvr("12345678-9012")
            .parameterSc(25)
            .examinationTypeSc(27)
            .withResultsAfter(OffsetDateTime.of(2024,2,29, 10,10,10,0, ZoneOffset.ofHours(1)))
            .exec();
        verify(streamLayer).submit("stations" +
            "?stationId=61000181&operatorStationId=610181" +
            "&stationOwnerCvr=DK25798376&operatorCvr=12345678-9012" +
            "&parameterSc=25&examinationTypeSc=27" +
            "&withResultsAfter=2024-02-29T10%3A10%2B01%3A00");
    }

    @Test
    void testStationsNullResponse() throws IOException, InterruptedException {
        Iterator<Station> stations = service.getStations().exec();
        assertFalse(stations.hasNext());
    }

    @Test
    void testStationsEmptyResponse() throws IOException, InterruptedException, URISyntaxException {
        when(streamLayer.submit(any())).thenReturn(InputStream.nullInputStream());
        Iterator<Station> stations = service.getStations().exec();
        assertFalse(stations.hasNext());
    }

    @Test
    void testStationsSpaceResponse() throws IOException, InterruptedException, URISyntaxException {
        when(streamLayer.submit(any())).thenReturn(new ByteArrayInputStream(new byte[]{' ', '\t', '\f', '\r', '\n'}));
        Iterator<Station> stations = service.getStations().exec();
        assertFalse(stations.hasNext());
    }

    @Test
    void testStationsSomeResponse() throws IOException, InterruptedException, URISyntaxException {
        when(streamLayer.submit(any())).thenReturn(getClass().getResourceAsStream("stations.json"));
        Iterator<Station> stations = service.getStations().exec();
        assertEquals(a, stations.next());
        assertFalse(stations.hasNext());
    }

    @Test
    void testStationsInvalidResponse() throws IOException, InterruptedException, URISyntaxException {
        when(streamLayer.submit(any())).thenReturn(new ByteArrayInputStream(new byte[]{'X'}));
        assertThrows(IOException.class, () -> service.getStations().exec());
    }

    @Test
    void testStationsFail(@Mock HttpResponseException response) throws IOException, InterruptedException, URISyntaxException {
        when(response.statusCode()).thenReturn(400);
        when(streamLayer.submit(any())).thenThrow(response);
        try {
            service.getStations().exec();
            fail();
        } catch (HttpResponseException e) {
            assertEquals(400, e.statusCode());
        } catch (Exception e) {
            fail();
        }
    }
}