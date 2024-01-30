package dk.dmp.vanda.hydro.service;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class HydrometryHttpTransport implements HydrometryTransport {
    private final URI apiBase;

    /**
     * Construct the transport layer.
     * @param apiBase Base URL of VanDa Hydro service, e.g.
     *        {@code https://vandah.miljoeportal.dk/api},
     *        {@code https://vandah.test.miljoeportal.dk/api} or
     *        {@code https://vandah.demo.miljoeportal.dk/api}
     */
    public HydrometryHttpTransport(URI apiBase) {
        this.apiBase = apiBase;
    }

    @Override
    public Response getStations(Iterable<Map.Entry<String, String>> queryParameters) throws IOException {
        return null;
    }

    @Override
    public Response getWaterLevels(Iterable<Map.Entry<String, String>> queryParameters) throws IOException {
        return null;
    }
}
