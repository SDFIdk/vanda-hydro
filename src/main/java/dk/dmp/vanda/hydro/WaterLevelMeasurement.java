package dk.dmp.vanda.hydro;

/**
 * A result of measuring water level.
 */
public interface WaterLevelMeasurement extends Measurement {
    /**
     * Get the result corrected for measuring equipment elevation.
     * Only available if different from measured value.
     * @return Elevation corrected measurement.
     */
    Double resultElevationCorrected();
}
