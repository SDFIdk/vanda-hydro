package dk.dmp.vanda.hydro;

import java.time.OffsetDateTime;

/**
 * A result of measuring water level.
 */
public interface WaterLevelMeasurement {
    /**
     * Get the number (starting from 1) of this measurement point in the
     * station.
     * @return Measurement point number in a station.
     */
    int measurementPointNumber();

    /**
     * Get the stancode of the property being measured.
     * @return Measured parameter.
     */
    int parameterSc();

    /**
     * Get the denotation of the property being measured.
     * @return Measured parameter.
     */
    String parameter();

    /**
     * Get the stancode of the type of examination.
     * @return Type of examination.
     */
    int examinationTypeSc();

    /**
     * Get the denotation of the type of examination.
     * @return Type of examination.
     */
    String examinationType();

    /**
     * Get the point in time when the examination was performed. The precision is minute.
     * @return Measurement timestamp.
     */
    OffsetDateTime measurementDateTime();

    /**
     * Get the measured value.
     * @return Measured value.
     */
    double result();

    /**
     * Get the result corrected for measuring equipment elevation.
     * Only available if different from measured value.
     * @return Elevation corrected measurement.
     */
    Double resultElevationCorrected();

    /**
     * Get the stancode of the unit of measurement.
     * @return Measurement unit.
     */
    int unitSc();

    /**
     * Get the symbol of the unit of measurement.
     * @return Measurement unit.
     */
    String unit();
}
