package dk.dmp.vanda.hydro.service;

import dk.dmp.vanda.hydro.Station;
import dk.dmp.vanda.hydro.WaterLevelMeasurement;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Iterator;

/**
 * A selection of operations to interact with the VanDa Hydro API.
 */
public interface HydrometryService {
    /**
     * Get stations
     * @return a stations request builder.
     */
    StationsRequest stations();

    /**
     * Get water level measurements.
     * @return a water-levels request builder.
     */
    WaterLevelsRequest waterLevels();

    /**
     * Build a request for the {@link #stations()} operation.
     */
    interface StationsRequest
    {
        /**
         * Perform the request.
         * @return The stations fulfilling all conditions of the request.
         */
        Iterator<Station> get() throws IOException;

        /**
         * Query by station ID.
         */
        StationsRequest withStationId(String stationId);

        /**
         * Query by station ID as known by the operator of the station.
         * This is normally 6 or 8 digits.
         */
        StationsRequest withOperatorStationId(String operatorStationId);

        /**
         * Query by CVR number of station owner, in DK12345678 format.
         */
        StationsRequest withStationOwnerCvr(String stationOwnerCvr);

        /**
         * Query by CVR number of station operator, in DK12345678 format.
         */
        StationsRequest withOperatorCvr(String operatorCvr);

        /**
         * Query by measured parameter as stancode.
         */
        StationsRequest withParameterSc(int parameterSc);

        /**
         * Query by examination type as stancode.
         */
        StationsRequest withExaminationTypeSc(int examinationTypeSc);

        /**
         * Filter the results after a point in time.
         */
        StationsRequest withResultsAfter(ZonedDateTime pointInTime);
    }

    /**
     * Request for the {@link #waterLevels()} operation.
     * {@linkplain #withStationId(String) Station ID} or
     * {@linkplain #withOperatorStationId(String) operator station ID}
     * must be specified.
     */
    interface WaterLevelsRequest
    {
        /**
         * Perform the request.
         * Returns the current results, i.e. no overwritten history.
         * @return The measurements fulfilling all conditions of the request.
         */
        Iterator<WaterLevelMeasurement> get() throws IOException;

        /**
         * Query by station ID.
         */
        WaterLevelsRequest withStationId(String stationId);

        /**
         * Query by station ID as known by the operator of the station.
         */
        WaterLevelsRequest withOperatorStationId(String operatorStationId);

        /**
         * Query by measurement point.
         * If not specified, return data for all measurement points.
         */
        WaterLevelsRequest withMeasurementPointNumber(int measurementPointNumber);

        /**
         * Query from the given timestamp.
         * Both {@linkplain #from(ZonedDateTime) from} and
         * {@linkplain #to(ZonedDateTime) to} must be specified if one
         * of them is present.
         * If not specified, return data for the last 24 hours.
         */
        WaterLevelsRequest from(ZonedDateTime pointInTime);

        /**
         * Query until the given timestamp.
         * Both {@linkplain #from(ZonedDateTime) from} and
         * {@linkplain #to(ZonedDateTime) to} must be specified if one
         * of them is present.
         * If not specified, return data for the last 24 hours.
         */
        WaterLevelsRequest to(ZonedDateTime pointInTime);
    }
}
