package dk.dmp.vanda.hydro.raw.request;

import java.io.IOException;
import java.io.InputStream;

/**
 * Request builder for the {@link dk.dmp.vanda.hydro.raw.RawHydrometryService#getStations()} operation.
 */
public interface RawGetStationsRequest
        extends dk.dmp.vanda.hydro.request.generic.GetStationsRequestBuilder<RawGetStationsRequest>
{
    /**
     * Return data in the given format.
     * If not specified, data is returned in JSON.
     * @param format "csv" or "parquet".
     * @return The modified request.
     */
    RawGetStationsRequest format(String format);

    /**
     * Perform the request.
     * @return The stations fulfilling all conditions of the request.
     */
    InputStream exec() throws IOException;
}
