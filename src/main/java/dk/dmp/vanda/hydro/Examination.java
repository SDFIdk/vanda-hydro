package dk.dmp.vanda.hydro;

import java.time.ZonedDateTime;

/**
 * Examination performed on a measurement point.
 */
public interface Examination {
    /**
     * Get the name of the measured parameter.
     * @return Parameter name.
     */
    String parameter();

    /**
     * Get the stancode of the measured parameter.
     * @return Parameter stancode.
     */
    Integer parameterSc();

    /**
     * Get the type of examination.
     * @return Examination type name.
     */
    String examinationType();

    /**
     * Get the type of examination as stancode.
     * @return Examination type stancode.
     */
    Integer examinationTypeSc();

    /**
     * Get the unit of measurement.
     * @return Unit name.
     */
    String unit();

    /**
     * Get the stancode of the unit of measurement.
     * @return Unit stancode.
     */
    Integer unitSc();

    /**
     * Get the time when the earliest measurement was taken.
     * @return The time of the earliest examination.
     */
    ZonedDateTime earliestResult();

    /**
     * Get the time when the lastest measurement was taken.
     * @return The time of the lastest examination.
     */
    ZonedDateTime latestResult();
}
