package dk.dmp.vanda.hydro;

import java.time.OffsetDateTime;

/**
 * Examination performed on a measurement point.
 */
public interface Examination {
    /**
     * Get the denotation of the property being measured.
     * @return Measured parameter.
     */
    String parameter();

    /**
     * Get the stancode of the property being measured.
     * @return Measured parameter.
     */
    Integer parameterSc();

    /**
     * Get the denotation of the type of examination.
     * @return Examination type name.
     */
    String examinationType();

    /**
     * Get the stancode of the type of examination.
     * @return Examination type stancode.
     */
    Integer examinationTypeSc();

    /**
     * Get the symbol of the unit of measurement.
     * @return Unit symbol.
     */
    String unit();

    /**
     * Get the stancode of the unit of measurement.
     * @return Unit stancode.
     */
    Integer unitSc();

    /**
     * Get the time when the earliest measurement was taken.
     * @return Earliest examination time.
     */
    OffsetDateTime earliestResult();

    /**
     * Get the time when the lastest measurement was taken.
     * @return Lastest examination time.
     */
    OffsetDateTime latestResult();
}
