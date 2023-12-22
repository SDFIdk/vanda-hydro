package dk.dmp.vanda.hydro;

import org.locationtech.jts.geom.Point;

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
     * Get the name of the measurement point.
     * @return Measurement point name.
     */
    String name();

    /**
     * Get the type of the measurement point.
     * @return Measurement point type name.
     */
    String measurementPointType();

    /**
     * Get the type of the measurement point as stancode.
     * @return Measurement point type stancode.
     */
    Integer measurementPointTypeSc();

    /**
     * Get a description of the measurement point.
     * @return Measurement point description.
     */
    String description();

    /**
     * Get the geographical location of the measurement point.
     * @return Location in UTM zone 32N coordinates.
     */
    Point location();

    /**
     * Get the examinations performed on the measurement point.
     * @return Examinations performed on the measurement point.
     */
    Examination[] examinations();
}
