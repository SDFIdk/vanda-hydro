package dk.dmp.vanda.hydro.request.generic;

import java.time.ZonedDateTime;

/**
 * Generic request builder for the {@link dk.dmp.vanda.hydro.HydrometryService#getStations()} operation.
 */
public interface GetStationsRequestBuilder<B extends GetStationsRequestBuilder<B>> {
    /**
     * Query by station ID.
     * @param stationId An 8-digit station id.
     * @return The modified request.
     */
    B stationId(String stationId);

    /**
     * Query by station ID as known by the operator of the station.
     * This is normally 6 or 8 digits.
     * @param operatorStationId Operator station id.
     * @return The modified request.
     */
    B operatorStationId(String operatorStationId);

    /**
     * Query by CVR number of station owner.
     * @param stationOwnerCvr Station owner cvr with DK12345678 format.
     * @return The modified request.
     */
    B stationOwnerCvr(String stationOwnerCvr);

    /**
     * Query by CVR number of station operator.
     * @param operatorCvr Operator cvr with DK12345678 format.
     * @return The modified request.
     */
    B operatorCvr(String operatorCvr);

    /**
     * Query by measured parameter as stancode.
     * @param parameterSc Parameter stancode.
     * @return The modified request.
     */
    B parameterSc(int parameterSc);

    /**
     * Query by examination type as stancode.
     * @param examinationTypeSc Examination type stancode.
     * @return The modified request.
     */
    B examinationTypeSc(int examinationTypeSc);

    /**
     * Filter the results after a point in time.
     * @param ts A point in time.
     * @return The modified request.
     */
    B withResultsAfter(ZonedDateTime ts);
}
