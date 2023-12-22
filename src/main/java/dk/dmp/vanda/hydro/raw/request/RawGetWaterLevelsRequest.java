package dk.dmp.vanda.hydro.raw.request;

import java.io.IOException;
import java.io.InputStream;

/**
 * Request builder for the {@link dk.dmp.vanda.hydro.raw.RawHydrometryService#getWaterLevels()} operation.
 * {@linkplain #stationId(String) Station ID} or
 * {@linkplain #operatorStationId(String) operator station ID}
 * must be specified.
 */
public interface RawGetWaterLevelsRequest
        extends dk.dmp.vanda.hydro.request.generic.GetWaterLevelsRequestBuilder<RawGetWaterLevelsRequest>
{
    /**
     * Return data in the given format.
     * If not specified, data is returned in JSON.
     * @param format "csv" or "parquet".
     * @return The modified request.
     */
    RawGetWaterLevelsRequest format(String format);

    /**
     * Perform the request.
     * @return The measurements fulfilling all conditions of the request.
     */
    InputStream exec() throws IOException;
}
