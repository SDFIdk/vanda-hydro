package dk.dmp.vanda.hydro;

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
        Iterator<Station> exec() throws IOException, InterruptedException;

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
         * Query by measured parameter as stancode from stancodelist 1008.
         */
        GetStationsOperation parameterSc(int parameterSc);

        /**
         * Query by examination type as stancode from stancodelist 1101.
         */
        GetStationsOperation examinationTypeSc(int examinationTypeSc);

        /**
         * Return only stations with measurements taken after a point in time.
         * Note that time components after minute are ignored.
         */
        GetStationsOperation withResultsAfter(OffsetDateTime pointInTime);

        /**
         * Return only stations with measurements registered after a point in time.
         * Note that time components after minute are ignored.
         */
        GetStationsOperation withResultsCreatedAfter(OffsetDateTime pointInTime);
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
        Iterator<WaterLevelMeasurement> exec() throws IOException, InterruptedException;

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
         * Query measurements taken from the given point in time, inclusive.
         * Both from and
         * {@linkplain #to(OffsetDateTime) to} must be specified if either
         * of them is.
         * Note that time components after minute are ignored.
         * If not specified, return data for the last 24 hours.
         */
        GetWaterLevelsOperation from(OffsetDateTime pointInTime);

        /**
         * Query measurements taken until the given timestamp, inclusive.
         * Both {@linkplain #from(OffsetDateTime) from} and
         * to must be specified if either
         * of them is.
         * Note that time components after minute are ignored.
         * If not specified, return data for the last 24 hours.
         */
        GetWaterLevelsOperation to(OffsetDateTime pointInTime);

        /**
         * Query measurements registered (created or updated) from the
         * given point in time, inclusive.
         * Note that time components after minute are ignored.
         */
        GetWaterLevelsOperation createdAfter(OffsetDateTime pointInTime);
    }
}
