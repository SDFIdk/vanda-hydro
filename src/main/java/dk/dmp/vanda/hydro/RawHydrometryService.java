package dk.dmp.vanda.hydro;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * A selection of operations to interact with the VanDa Hydro API.
 */
public interface RawHydrometryService {
    /**
     * Get a list of stations
     * @return The stations fulfilling all conditions of the request.
     */
    Response getStations(GetStationsRequest request) throws IOException;

    /**
     * Get water level measurements.
     * Returns the current results, i.e. no overwritten history.
     * @return The measurements fulfilling all conditions of the request.
     */
    Response getWaterLevels(GetWaterLevelsRequest request) throws IOException;

    /**
     * Request builder for the {@link #getStations(GetStationsRequest)} operation.
     */
    interface GetStationsRequest
            extends HydrometryService.GetStationsRequest
    {
        /**
         * Return data in the given format.
         * If not specified, data is returned in JSON.
         * @return "csv" or "parquet".
         */
        Optional<String> format();
    }

    /**
     * Request builder for the {@link #getWaterLevels(GetWaterLevelsRequest)} operation.
     * {@linkplain #stationId() Station ID} or
     * {@linkplain #operatorStationId() operator station ID}
     * must be specified.
     */
    interface GetWaterLevelsRequest
            extends HydrometryService.GetWaterLevelsRequest
    {
        /**
         * Return data in the given format.
         * If not specified, data is returned in JSON.
         * @return "csv" or "parquet".
         */
        Optional<String> format();
    }

    interface Response {
        Optional<Charset> charset();
        InputStream data();
    }
}
