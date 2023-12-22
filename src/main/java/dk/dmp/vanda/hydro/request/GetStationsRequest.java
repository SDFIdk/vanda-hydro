package dk.dmp.vanda.hydro.request;

import dk.dmp.vanda.hydro.Station;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.concurrent.Flow;

/**
 * Request builder for the {@link dk.dmp.vanda.hydro.HydrometryService#getStations()} operation.
 */
public interface GetStationsRequest {
    /**
     * Query by station ID.
     * @param stationId An 8-digit station id.
     * @return The modified request.
     */
    GetStationsRequest stationId(String stationId);

    /**
     * Query by station ID as known by the operator of the station.
     * This is normally 6 or 8 digits.
     * @param operatorStationId Operator station id.
     * @return The modified request.
     */
    GetStationsRequest operatorStationId(String operatorStationId);

    /**
     * Query by CVR number of station owner.
     * @param stationOwnerCvr Station owner cvr with DK12345678 format.
     * @return The modified request.
     */
    GetStationsRequest stationOwnerCvr(String stationOwnerCvr);

    /**
     * Query by CVR number of station operator.
     * @param operatorCvr Operator cvr with DK12345678 format.
     * @return The modified request.
     */
    GetStationsRequest operatorCvr(String operatorCvr);

    /**
     * Query by measured parameter as stancode.
     * @param parameterSc Parameter stancode.
     * @return The modified request.
     */
    GetStationsRequest parameterSc(int parameterSc);

    /**
     * Query by examination type as stancode.
     * @param examinationTypeSc Examination type stancode.
     * @return The modified request.
     */
    GetStationsRequest examinationTypeSc(int examinationTypeSc);

    /**
     * Filter the results after a point in time.
     * @param ts A point in time.
     * @return The modified request.
     */
    GetStationsRequest withResultsAfter(ZonedDateTime ts);

    /**
     * Perform the request.
     * @return The stations fulfilling all conditions of the request.
     */
    Flow.Publisher<Station> exec() throws IOException;
}
