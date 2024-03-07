package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.HydrometryService;
import dk.dmp.vanda.hydro.WaterLevelMeasurement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HydrometryServiceClientWaterLevelTest {
    @Mock
    StreamService streamLayer;
    HydrometryService service;
    WaterLevelMeasurement a;

    @BeforeEach
    void setUp() {
        service = new HydrometryServiceClient(streamLayer);

        JsonWaterLevelMeasurement m = new JsonWaterLevelMeasurement();
        m.setMeasurementPointNumber(1);
        m.setParameterSc(1233);
        m.setParameter("Vandstand");
        m.setExaminationTypeSc(25);
        m.setExaminationType("Vandstand");
        m.setMeasurementDateTime(OffsetDateTime.parse("2023-10-02T18:10Z"));
        m.setResult(31.8);
        m.setResultElevationCorrected(-58.2);
        m.setUnitSc(19);
        m.setUnit("cm");
        a = m;
    }

    @AfterEach
    void tearDown() throws Exception {
        ((HydrometryServiceClient)service).close();
    }

    @Test
    void testNoParameters() throws IOException, InterruptedException {
        service.getWaterLevels().exec();
        verify(streamLayer).get("water-levels", "");
    }

    @Test
    void testSomeParameters() throws IOException, InterruptedException {
        service.getWaterLevels()
            .stationId("61000181")
            .operatorStationId("610181")
            .measurementPointNumber(1)
            .from(OffsetDateTime.of(2024,2,29, 10,10,10,0, ZoneOffset.ofHours(1)))
            .to(OffsetDateTime.of(2024,3,1, 10,10,10,0, ZoneOffset.ofHours(1)))
            .createdAfter(OffsetDateTime.of(2024,3,7, 11,25,10,0, ZoneOffset.ofHours(1)))
            .exec();
        verify(streamLayer).get("water-levels",
            "stationId=61000181&operatorStationId=610181"
                + "&measurementPointNumber=1"
                + "&from=2024-02-29T09%3A10Z&to=2024-03-01T09%3A10Z"
                + "&createdAfter=2024-03-07T10%3A25Z");
    }

    @Test
    void testNullResponse() throws IOException, InterruptedException {
        Iterator<WaterLevelMeasurement> water = service.getWaterLevels().exec();
        assertFalse(water.hasNext());
    }

    @Test
    void testEmptyResponse() throws IOException, InterruptedException {
        when(streamLayer.get(any(), any())).thenReturn(InputStream.nullInputStream());
        Iterator<WaterLevelMeasurement> water = service.getWaterLevels().exec();
        assertFalse(water.hasNext());
    }

    @Test
    void testSpaceResponse() throws IOException, InterruptedException {
        when(streamLayer.get(any(), any())).thenReturn(new ByteArrayInputStream(new byte[]{' ', '\t', '\f', '\r', '\n'}));
        Iterator<WaterLevelMeasurement> water = service.getWaterLevels().exec();
        assertFalse(water.hasNext());
    }

    @Test
    void testSomeResponse() throws IOException, InterruptedException {
        when(streamLayer.get(any(), any())).thenReturn(getClass().getResourceAsStream("water-level_61000181.json"));
        Iterator<WaterLevelMeasurement> water = service.getWaterLevels().exec();
        assertEquals(a, water.next());
        assertTrue(water.hasNext());
        assertNotNull(water.next());
        assertFalse(water.hasNext());
        assertThrows(NoSuchElementException.class, water::next);
    }

    @Test
    void testInvalidResponse() throws IOException, InterruptedException {
        when(streamLayer.get(any(), any())).thenReturn(new ByteArrayInputStream(new byte[]{'X'}));
        assertThrows(IOException.class, () -> service.getWaterLevels().exec());
    }

    @Test
    void testFail(@Mock HttpResponseException response) throws IOException, InterruptedException {
        when(response.statusCode()).thenReturn(400);
        when(streamLayer.get(any(), any())).thenThrow(response);
        try {
            service.getWaterLevels().exec();
            fail();
        } catch (HttpResponseException e) {
            assertEquals(400, e.statusCode());
        } catch (Exception e) {
            fail();
        }
    }
}