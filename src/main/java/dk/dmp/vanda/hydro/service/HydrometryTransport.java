package dk.dmp.vanda.hydro.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

/**
 * A selection of operations to interact with the VanDa Hydro API.
 */
public interface HydrometryTransport {
    /**
     * Get a list of stations.
     * The stations are returned in JSON format, unless query parameter
     * {@code format} is set to "csv" or "parquet".
     * @param queryParameters Query parameters and their values.
     * @return The stations fulfilling all conditions of the request.
     */
    Response getStations(Iterable<Map.Entry<String,String>> queryParameters) throws IOException;

    /**
     * Get water level measurements.
     * Returns the current results, i.e. overwritten history is discarded.
     * The measurements are returned in JSON format, unless query parameter
     * {@code format} is set to "csv" or "parquet".
     * @param queryParameters Query parameters and their values.
     *        Must include {@code stationId} or {@code operatorStationId}.
     * @return The measurements fulfilling all conditions of the request.
     */
    Response getWaterLevels(Iterable<Map.Entry<String,String>> queryParameters) throws IOException;

    interface Response {
        Optional<Charset> charset();
        InputStream body();
    }
}
