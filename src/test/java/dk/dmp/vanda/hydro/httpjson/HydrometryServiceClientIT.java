package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.HydrometryService;
import dk.dmp.vanda.hydro.Station;
import dk.dmp.vanda.hydro.WaterLevelMeasurement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class HydrometryServiceClientIT {
    static HttpClient http;
    static HydrometryService service;
    @BeforeAll
    static void setup() {
        URI endpoint = URI.create("https://vandah.test.miljoeportal.dk/api/");
        http = HttpClient.newHttpClient();
        service = new HydrometryServiceClient(new JsonStreamHttpClient(endpoint, http));
    }

    @AfterAll
    static void teardown() throws Exception {
        http.close();
        ((HydrometryServiceClient)service).close();
    }

    @Test
    void testStations() throws Exception {
        Iterator<Station> iter = service.getStations().stationId("61000181").exec();
        Station station = iter.next();
        assertAll(
            () -> assertFalse(iter.hasNext()),
            () -> assertEquals("61000181", station.stationId()),
            () -> assertEquals("Vandløb", station.locationType()),
            () -> assertEquals(1, station.measurementPoints().size()),
            () -> assertEquals(3, station.measurementPoints().getFirst().examinations().size()));
    }

    @Test
    void testStationsAfter() throws Exception {
        Iterator<Station> iter = service.getStations().stationId("61000181")
            .withResultsAfter(OffsetDateTime.of(2018,1,1, 14,0,0,0, ZoneOffset.ofHours(1)))
            .exec();
        Station station = iter.next();
        assertAll(
            () -> assertFalse(iter.hasNext()),
            () -> assertEquals("61000181", station.stationId()),
            () -> assertEquals("Vandløb", station.locationType()),
            () -> assertEquals(1, station.measurementPoints().size()),
            () -> assertEquals(2, station.measurementPoints().getFirst().examinations().size()));
    }

    @Test
    void testWaterLevels() throws Exception {
        HydrometryService.GetWaterLevelsOperation op = service.getWaterLevels();
        op.stationId("61000181");
        op.from(OffsetDateTime.parse("2023-10-02T19:05+01:00"));
        op.to(OffsetDateTime.parse("2023-10-02T19:10+01:00"));
        Iterator<WaterLevelMeasurement> iter = op.exec();
        WaterLevelMeasurement m1 = iter.next();
        assertEquals("Vandstand", m1.parameter());
        WaterLevelMeasurement m2 = iter.next();
        assertEquals("cm", m2.unit());
        assertThrows(NoSuchElementException.class, iter::next);
    }
}
