package dk.dmp.vanda.hydro.request;

import dk.dmp.vanda.hydro.WaterLevelMeasurement;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.concurrent.Flow;

/**
 * Request builder for the {@link dk.dmp.vanda.hydro.HydrometryService#getWaterLevels()} operation.
 * {@linkplain #stationId(String) Station ID} or
 * {@linkplain #operatorStationId(String) operator station ID}
 * must be specified.
 */
public interface GetWaterLevelsRequest {
    /**
     * Query by station ID.
     * @param stationId Station ID.
     * @return The modified request builder.
     */
    GetWaterLevelsRequest stationId(String stationId);

    /**
     * Query by station ID as known by the operator of the station.
     * @param operatorStationId Operator station ID.
     * @return The modified request builder.
     */
    GetWaterLevelsRequest operatorStationId(String operatorStationId);

    /**
     * Query by measurement point.
     * If not specified, return data for all measurement points.
     * @param measurementPointNumber Measurement point number.
     * @return The modified request builder.
     */
    GetWaterLevelsRequest measurementPointNumber(int measurementPointNumber);

    /**
     * Query from the given timestamp.
     * Both From and To must be specified if one of them presents.
     * If not specified, return data for the last 24 hours.
     * @param from Measurement timestamp.
     * @return The modified request builder.
     */
    GetWaterLevelsRequest from(ZonedDateTime from);

    /**
     * Query until the given timestamp.
     * Both From and To must be specified if one of them presents.
     * If not specified, return data for the last 24 hours.
     * @param to Measurement timestamp.
     * @return The modified request builder.
     */
    GetWaterLevelsRequest to(ZonedDateTime to);

    /**
     * Perform the request.
     * @return The measurements fulfilling all conditions of the request.
     */
    Flow.Publisher<WaterLevelMeasurement> exec() throws IOException;
}
