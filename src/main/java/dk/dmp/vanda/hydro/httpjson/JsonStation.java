package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.MeasurementPoint;
import dk.dmp.vanda.hydro.Station;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class JsonStation implements Station {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private UUID stationUid;
    public void setStationUid(String str) {
        try {
            stationUid = UUID.fromString(str);
        } catch (IllegalArgumentException e) {
            log.debug("Cannot interpret stationUid as a UUID: {}", str, e);
        }
    }
    @Override
    public UUID stationUid() {
        return stationUid;
    }

    private String stationId;
    public void setStationId(String str) {
        stationId = str;
    }
    @Override
    public String stationId() {
        return stationId;
    }

    private String operatorStationId;
    public void setOperatorStationId(String str) {
        operatorStationId = str;
    }
    @Override
    public String operatorStationId() {
        return operatorStationId;
    }

    private String oldStationNumber;
    public void setOldStationNumber(String str) {
        oldStationNumber = str;
    }
    @Override
    public String oldStationNumber() {
        return oldStationNumber;
    }

    private String locationType;
    public void setLocationType(String str) {
        locationType = str;
    }
    @Override
    public String locationType() {
        return locationType;
    }

    private int locationTypeSc;
    public void setLocationTypeSc(int n) {
        locationTypeSc = n;
    }
    @Override
    public int locationTypeSc() {
        return locationTypeSc;
    }

    private String stationOwnerCvr;
    public void setStationOwnerCvr(String str) {
        stationOwnerCvr = str;
    }
    @Override
    public String stationOwnerCvr() {
        return stationOwnerCvr;
    }

    private String stationOwnerName;
    public void setStationOwnerName(String str) {
        stationOwnerName = str;
    }
    @Override
    public String stationOwnerName() {
        return stationOwnerName;
    }

    private String operatorCvr;
    public void setOperatorCvr(String str) {
        operatorCvr = str;
    }
    @Override
    public String operatorCvr() {
        return operatorCvr;
    }

    private String operatorName;
    public void setOperatorName(String str) {
        operatorName = str;
    }
    @Override
    public String operatorName() {
        return operatorName;
    }

    private String name;
    public void setName(String str) {
        name = str;
    }
    @Override
    public String name() {
        return name;
    }

    private String description;
    public void setDescription(String str) {
        description = str;
    }
    @Override
    public String description() {
        return description;
    }

    private String loggerId;
    public void setLoggerId(String str) {
        loggerId = str;
    }
    @Override
    public String loggerId() {
        return loggerId;
    }

    private Point location;
    @JsonbTypeAdapter(PointJsonAdapter.class)
    public void setLocation(Point p) {
        location = p;
    }
    @Override
    public Point location() {
        return location;
    }

    private MeasurementPoint[] measurementPoints;
/*    public void setMeasurementPoints(MeasurementPoint[] pts) {
        measurementPoints = pts;
    } */
    @Override
    public MeasurementPoint[] measurementPoints() {
        return measurementPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonStation that)) return false;
        return Objects.equals(stationUid, that.stationUid)
                && Objects.equals(stationId, that.stationId)
                && Objects.equals(operatorStationId, that.operatorStationId)
                && Objects.equals(oldStationNumber, that.oldStationNumber)
                && Objects.equals(locationType, that.locationType)
                && locationTypeSc == that.locationTypeSc
                && Objects.equals(stationOwnerCvr, that.stationOwnerCvr)
                && Objects.equals(stationOwnerName, that.stationOwnerName)
                && Objects.equals(operatorCvr, that.operatorCvr)
                && Objects.equals(operatorName, that.operatorName)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(loggerId, that.loggerId)
                && Objects.equals(location, that.location)
                && Arrays.equals(measurementPoints, that.measurementPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationId);
    }

    @Override
    public String toString() {
        return "JsonStation {"
                + "stationUid = " + stationUid
                + ", stationId = \"" + stationId + '"'
                + ", operatorStationId = \"" + operatorStationId + '"'
                + ", oldStationNumber = \"" + oldStationNumber + '"'
                + ", locationType = \"" + locationType + '"'
                + ", locationTypeSc = " + locationTypeSc
                + ", stationOwnerCvr = \"" + stationOwnerCvr + '"'
                + ", stationOwnerName = \"" + stationOwnerName + '"'
                + ", operatorCvr = \"" + operatorCvr + '"'
                + ", operatorName = \"" + operatorName + '"'
                + ", name = \"" + name + '"'
                + ", description = \"" + description + '"'
                + ", loggerId = \"" + loggerId + '"'
                + ", location = " + location
                + ", measurementPoints = " + Arrays.toString(measurementPoints)
                + '}';
    }
}
