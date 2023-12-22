package dk.dmp.vanda.hydro;

import java.time.ZonedDateTime;

/**
 * A result of measuring water level.
 */
public interface WaterLevelMeasurement {
    /**
     * Get the measurement point number.
     * @return Measurement point number.
     */
    int measurementPointNumber();

    /**
     * Get stancode of measured parameter.
     * @return Parameter stancode.
     */
    int parameterSc();

    /**
     * Get name of measured parameter.
     * @return Measured parameter.
     */
    String parameter();

    /**
     * Get stancode of examination type.
     * @return Examination type stancode.
     */
    int examinationTypeSc();

    /**
     * Get type of examination.
     * @return Examination type name.
     */
    String examinationType();

    /**
     * Get timestamp of measurement.
     * @return Measurement timestamp.
     */
    ZonedDateTime measurementDateTime();

    /**
     * Get the measurement.
     * @return Measurement.
     */
    double result();

    /**
     * Get the corrected result, if available.
     * Only available for water level, and only if different from measurement.
     * @return Elevation corrected result.
     */
    Double resultElevationCorrected();

    /**
     * Get the stancode of the unit of measurement.
     * @return Unit stancode.
     */
    int unitSc();

    /**
     * Get the unit of measurement.
     * @return Unit name.
     */
    String unit();
}
