package dk.dmp.vanda.hydro.service;

import dk.dmp.vanda.hydro.Station;
import dk.dmp.vanda.hydro.WaterLevelMeasurement;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Iterator;

/**
 * A selection of operations to interact with the VanDa Hydro API.
 */
public interface HydrometryService {
    /**
     * Get stations
     * @return a stations request builder.
     */
    GetStationsOperation getStations();

    /**
     * Get water level measurements.
     * @return a water-levels request builder.
     */
    GetWaterLevelsOperation getWaterLevels();

    /**
     * Build a request for the {@link #getStations()} operation.
     */
    interface GetStationsOperation
    {
        /**
         * Perform the request.
         * @return The stations fulfilling all conditions of the request.
         */
        Iterator<Station> exec() throws IOException;

        /**
         * Query by station ID.
         */
        GetStationsOperation stationId(String stationId);

        /**
         * Query by station ID as known by the operator of the station.
         * This is normally 6 or 8 digits.
         */
        GetStationsOperation operatorStationId(String operatorStationId);

        /**
         * Query by CVR number of station owner, in DK12345678 format.
         */
        GetStationsOperation stationOwnerCvr(String stationOwnerCvr);

        /**
         * Query by CVR number of station operator, in DK12345678 format.
         */
        GetStationsOperation operatorCvr(String operatorCvr);

        /**
         * Query by measured parameter as stancode.
         */
        GetStationsOperation parameterSc(int parameterSc);

        /**
         * Query by examination type as stancode.
         */
        GetStationsOperation examinationTypeSc(int examinationTypeSc);

        /**
         * Filter the results after a point in time.
         * Note that components after minute are ignored.
         */
        GetStationsOperation withResultsAfter(OffsetDateTime pointInTime);
    }

    /**
     * Request for the {@link #getWaterLevels()} operation.
     * {@linkplain #stationId(String) Station ID} or
     * {@linkplain #operatorStationId(String) operator station ID}
     * must be specified.
     */
    interface GetWaterLevelsOperation
    {
        /**
         * Perform the request.
         * Returns the current results, i.e. no overwritten history.
         * @return The measurements fulfilling all conditions of the request.
         */
        Iterator<WaterLevelMeasurement> exec() throws IOException;

        /**
         * Query by station ID.
         */
        GetWaterLevelsOperation stationId(String stationId);

        /**
         * Query by station ID as known by the operator of the station.
         */
        GetWaterLevelsOperation operatorStationId(String operatorStationId);

        /**
         * Query by measurement point.
         * If not specified, return data for all measurement points.
         */
        GetWaterLevelsOperation measurementPointNumber(int measurementPointNumber);

        /**
         * Query from the given timestamp.
         * Both {@linkplain #from(OffsetDateTime) from} and
         * {@linkplain #to(OffsetDateTime) to} must be specified if one
         * of them is present.
         * Note that components after minute are ignored.
         * If not specified, return data for the last 24 hours.
         */
        GetWaterLevelsOperation from(OffsetDateTime pointInTime);

        /**
         * Query until the given timestamp.
         * Both {@linkplain #from(OffsetDateTime) from} and
         * {@linkplain #to(OffsetDateTime) to} must be specified if one
         * of them is present.
         * Note that components after minute are ignored.
         * If not specified, return data for the last 24 hours.
         */
        GetWaterLevelsOperation to(OffsetDateTime pointInTime);
    }
}
