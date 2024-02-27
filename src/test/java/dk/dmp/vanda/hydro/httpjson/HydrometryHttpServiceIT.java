package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.Station;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class HydrometryHttpServiceIT {
    @Test
    void testStations() throws IOException, InterruptedException {
        URI endpoint = URI.create("https://vandah.test.miljoeportal.dk/api/");
        try (StreamHttpClient http = new StreamHttpClient();
             HydrometryHttpService service = new HydrometryHttpService(endpoint, http)
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
}
