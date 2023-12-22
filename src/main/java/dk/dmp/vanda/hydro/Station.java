package dk.dmp.vanda.hydro;

import org.locationtech.jts.geom.Point;

import java.util.UUID;

/**
 * A gauge station.
 */
public interface Station {
    /**
     * Get the VanDa station GUID.
     * @return VanDa station GUID.
     */
    UUID stationUid();

    /**
     * Get the unique VanDa Hydro station ID.
     * Often the same as the old obsStedNr.
     * @return The 8-digit station ID, a.k.a. obsStedNr.
     */
    String stationId();

    /**
     * Get the station ID, a.k.a. ejerStedNr, as known by the operator
     * of the station.
     * This is normally 6 or 8 digits, if it exists.
     * @return Operator station id, a.k.a. ejerStedNr.
     */
    String operatorStationId();

    /**
     * Get the station number, a.k.a. obsStedNr, as known by the old Hydrometri API.
     * This is normally 8 digits, if it exists.
     * @return Old 8-digit station number, a.k.a. obsStedNr.
     */
    String oldStationNumber();

    /**
     * Get the location type name, e.g. "Vandløb" or "Pumpestation".
     * @return Location type name.
     */
    String locationType();

    /**
     * Get the location type as stancode.
     * @return Location type stancode.
     */
    int locationTypeSc();

    /**
     * Get the CVR number of the station owner.
     * @return Station owner CVR number.
     */
    String stationOwnerCvr();

    /**
     * Get the name of the station owner.
     * @return Station owner name.
     */
    String stationOwnerName();

    /**
     * Get the CVR number of the station operator.
     * @return Operator CVR number.
     */
    String operatorCvr();

    /**
     * Get the name of the station operator.
     * @return Operator name.
     */
    String operatorName();

    /**
     * Get the name of the station.
     * @return Station name.
     */
    String name();

    /**
     * Get a description of the station, similar to the old locality,
     * sometimes including the size of the catchment area.
     * @return Station description.
     */
    String description();

    /**
     * Get the ID of the logger (gauge) at the station.
     * @return Logger ID.
     */
    String loggerId();

    /**
     * Get the geographical location of the station.
     * @return Location in UTM zone 32N coordinates.
     */
    Point location();

    /**
     * Get the points in the station where measurements are taken.
     * Pumping stations have two measurement points: one incoming, and
     * one outgoing. Other stations have just one measurement point.
     * @return The measurement points in the station.
     */
    MeasurementPoint[] measurementPoints();
}
