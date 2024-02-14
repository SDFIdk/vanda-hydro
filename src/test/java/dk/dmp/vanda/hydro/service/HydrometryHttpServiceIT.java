package dk.dmp.vanda.hydro.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;

class HydrometryHttpServiceIT {
    @Test
    void testStations() throws IOException, InterruptedException {
        StreamHttpClient client = new StreamHttpClient(HttpClient.newHttpClient());
        URI endpoint = URI.create("https://vandah.test.miljoeportal.dk/api/");
        HydrometryService service = new HydrometryHttpService(endpoint, client);
        service.getStations().stationId("61000181").exec();
    }
}
