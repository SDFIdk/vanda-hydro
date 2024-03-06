package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.Station;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class HydrometryServiceClientIT {
    @Test
    void testStations() throws Exception {
        URI endpoint = URI.create("https://vandah.test.miljoeportal.dk/api/");
        try (HttpClient http = HttpClient.newHttpClient();
             HydrometryServiceClient service = new HydrometryServiceClient(new JsonStreamHttpClient(endpoint, http))
        ) {
            Iterator<Station> iter = service.getStations().stationId("61000181").exec();
            Station station = iter.next();
            assertAll(
                () -> assertFalse(iter.hasNext()),
                () -> assertEquals("61000181", station.stationId()),
                () -> assertEquals("Vandløb", station.locationType()),
                () -> assertEquals(1, station.measurementPoints().size()),
                () -> assertEquals(3, station.measurementPoints().getFirst().examinations().size()));
        }
    }

    @Test
    void testStationsAfter() throws Exception {
        URI endpoint = URI.create("https://vandah.test.miljoeportal.dk/api/");
        try (HttpClient http = HttpClient.newHttpClient();
             HydrometryServiceClient service = new HydrometryServiceClient(new JsonStreamHttpClient(endpoint, http))
        ) {
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
    }
}
