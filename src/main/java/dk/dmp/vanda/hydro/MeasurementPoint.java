package dk.dmp.vanda.hydro;

import org.locationtech.jts.geom.Point;

import java.util.List;

/**
 * A point in the station where measurements are taken.
 */
public interface MeasurementPoint {
    /**
     * Get the number (starting from 1) of this measurement point in the
     * station.
     * @return Measurement point number in a station.
     */
    int number();

    /**
     * Get the denotation of the measurement point.
     * @return Measurement point denotation.
     */
    String name();

    /**
     * Get the denotation of the type of measurement point.
     * @return Measurement point type name.
     */
    String measurementPointType();

    /**
     * Get the stancode of the type of measurement point.
     * @return Measurement point type stancode.
     */
    Integer measurementPointTypeSc();

    /**
     * Get a description of the measurement point.
     * @return Measurement point description.
     */
    String description();

    /**
     * Get the projected planar coordinate (in UTM zone 32N) of the measurement point.
     * @return Planar coordinate of measurement point.
     */
    Point location();

    /**
     * Get the intake number of this measurement point at the gauge station.
     * @return Intake number.
     */
    Integer intakeNumber();

    /**
     * Get the examinations performed at the measurement point.
     * @return Examinations at measurement point.
     */
    List<Examination> examinations();
}
