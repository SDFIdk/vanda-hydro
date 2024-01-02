package dk.dmp.vanda.hydro;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.Flow;

/**
 * A selection of operations to interact with the VanDa Hydro API.
 */
public interface HydrometryService {
    /**
     * Get a list of stations
     * @return The stations fulfilling all conditions of the request.
     */
    Flow.Publisher<Station> getStations(GetStationsRequest request) throws IOException;

    /**
     * Get water level measurements.
     * Returns the current results, i.e. no overwritten history.
     * @return The measurements fulfilling all conditions of the request.
     */
    Flow.Publisher<WaterLevelMeasurement> getWaterLevels(GetWaterLevelsRequest request) throws IOException;

    /**
     * Request for the {@link #getStations(GetStationsRequest)} operation.
     */
    interface GetStationsRequest
    {
        /**
         * Query by station ID.
         * @return An 8-digit station id.
         */
        Optional<String> stationId();

        /**
         * Query by station ID as known by the operator of the station.
         * This is normally 6 or 8 digits.
         * @return Operator station id.
         */
        Optional<String> operatorStationId();

        /**
         * Query by CVR number of station owner.
         * @return Station owner CVR with DK12345678 format.
         */
        Optional<String> stationOwnerCvr();

        /**
         * Query by CVR number of station operator.
         * @return Operator CVR with DK12345678 format.
         */
        Optional<String> operatorCvr();

        /**
         * Query by measured parameter as stancode.
         * @return Parameter stancode.
         */
        OptionalInt parameterSc();

        /**
         * Query by examination type as stancode.
         * @return Examination type stancode.
         */
        OptionalInt examinationTypeSc();

        /**
         * Filter the results after a point in time.
         * @return A point in time.
         */
        Optional<ZonedDateTime> withResultsAfter();
    }

    /**
     * Request for the {@link #getWaterLevels(GetWaterLevelsRequest)} operation.
     * {@linkplain #stationId() Station ID} or
     * {@linkplain #operatorStationId() operator station ID}
     * must be specified.
     */
    interface GetWaterLevelsRequest
    {
        /**
         * Query by station ID.
         * @return Station ID.
         */
        Optional<String> stationId();

        /**
         * Query by station ID as known by the operator of the station.
         * @return Operator station ID.
         */
        Optional<String> operatorStationId();

        /**
         * Query by measurement point.
         * If not specified, return data for all measurement points.
         * @return Measurement point number.
         */
        OptionalInt measurementPointNumber();

        /**
         * Query from the given timestamp.
         * Both {@code from} and {@code to} must be specified if one of
         * them is present.
         * If not specified, return data for the last 24 hours.
         * @return Measurement timestamp.
         */
        Optional<ZonedDateTime> from();

        /**
         * Query until the given timestamp.
         * Both {@code from} and {@code to} must be specified if one of
         * them is present.
         * If not specified, return data for the last 24 hours.
         * @return Measurement timestamp.
         */
        Optional<ZonedDateTime> to();
    }
}
