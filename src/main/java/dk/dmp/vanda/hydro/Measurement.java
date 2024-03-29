package dk.dmp.vanda.hydro;

import java.time.OffsetDateTime;

/**
 * A result of measuring something in a watercourse.
 */
public interface Measurement {
    /**
     * Get the unique VanDa Hydro station ID.
     * Often the same as the old obsStedNr.
     * @return The 8-digit station ID.
     */
    String stationId();

    /**
     * Get the station ID assigned by the operator of the station.
     * This is normally 6 or 8 digits, if it exists.
     * Previously known as ejerStedNr.
     * @return Operator station ID, a.k.a. ejerStedNr.
     */
    String operatorStationId();

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
