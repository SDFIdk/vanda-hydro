package dk.dmp.vanda.hydro.request.generic;

import java.time.ZonedDateTime;

/**
 * Request builder for the {@link dk.dmp.vanda.hydro.HydrometryService#getWaterLevels()} operation.
 * {@linkplain #stationId(String) Station ID} or
 * {@linkplain #operatorStationId(String) operator station ID}
 * must be specified.
 */
public interface GetWaterLevelsRequestBuilder<B extends GetWaterLevelsRequestBuilder<B>> {
    /**
     * Query by station ID.
     * @param stationId Station ID.
     * @return The modified request builder.
     */
    B stationId(String stationId);

    /**
     * Query by station ID as known by the operator of the station.
     * @param operatorStationId Operator station ID.
     * @return The modified request builder.
     */
    B operatorStationId(String operatorStationId);

    /**
     * Query by measurement point.
     * If not specified, return data for all measurement points.
     * @param measurementPointNumber Measurement point number.
     * @return The modified request builder.
     */
    B measurementPointNumber(int measurementPointNumber);

    /**
     * Query from the given timestamp.
     * Both From and To must be specified if one of them presents.
     * If not specified, return data for the last 24 hours.
     * @param from Measurement timestamp.
     * @return The modified request builder.
     */
    B from(ZonedDateTime from);

    /**
     * Query until the given timestamp.
     * Both From and To must be specified if one of them presents.
     * If not specified, return data for the last 24 hours.
     * @param to Measurement timestamp.
     * @return The modified request builder.
     */
    B to(ZonedDateTime to);
}
