package dk.dmp.vanda.hydro.request;

import dk.dmp.vanda.hydro.WaterLevelMeasurement;

import java.io.IOException;
import java.util.concurrent.Flow;

/**
 * Request builder for the {@link dk.dmp.vanda.hydro.HydrometryService#getWaterLevels()} operation.
 * {@linkplain #stationId(String) Station ID} or
 * {@linkplain #operatorStationId(String) operator station ID}
 * must be specified.
 */
public interface GetWaterLevelsRequest
        extends dk.dmp.vanda.hydro.request.generic.GetWaterLevelsRequestBuilder<GetWaterLevelsRequest>
{
    /**
     * Perform the request.
     * @return The measurements fulfilling all conditions of the request.
     */
    Flow.Publisher<WaterLevelMeasurement> exec() throws IOException;
}
