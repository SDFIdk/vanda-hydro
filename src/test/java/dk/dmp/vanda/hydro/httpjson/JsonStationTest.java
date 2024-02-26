package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.Station;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

import java.io.InputStream;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JsonStationTest {
    Station a;

    @BeforeEach
    void setup() {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 25832);
        JsonStation s = new JsonStation();
        s.setStationUid("2e76caf9-d772-4c07-a6f1-0b7b4cf4d187");
        s.setStationId("61000181");
        s.setOperatorStationId("610181");
        s.setOldStationNumber("61000399");
        s.setLocationType("Vandløb");
        s.setLocationTypeSc(1);
        s.setStationOwnerCvr("DK25798376");
        s.setStationOwnerName("Miljøstyrelsen");
        s.setOperatorCvr("12345678-9012");
        s.setOperatorName("Artificial Operator");
        s.setName("Tt Vålse Vig, Vålse Vig");
        s.setDescription("Opland = 27,01 km2");
        s.setLoggerId("41662");
        s.setLocation(gf.createPoint(new Coordinate(679796.2734,6091352.6536)));
        JsonMeasurementPoint m = new JsonMeasurementPoint();
        m.setNumber(1);
        s.setMeasurementPoints(Collections.singletonList(m));
        a = s;
    }

    @Test
    void testGetters() {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 25832);
        JsonMeasurementPoint m = new JsonMeasurementPoint();
        m.setNumber(1);
        assertAll(
            () -> assertEquals(UUID.fromString("2e76caf9-d772-4c07-a6f1-0b7b4cf4d187"), a.stationUid()),
            () -> assertEquals("61000181", a.stationId()),
            () -> assertEquals("610181", a.operatorStationId()),
            () -> assertEquals("61000399", a.oldStationNumber()),
            () -> assertEquals("Vandløb", a.locationType()),
            () -> assertEquals(1, a.locationTypeSc()),
            () -> assertEquals("DK25798376", a.stationOwnerCvr()),
            () -> assertEquals("Miljøstyrelsen", a.stationOwnerName()),
            () -> assertEquals("12345678-9012", a.operatorCvr()),
            () -> assertEquals("Artificial Operator", a.operatorName()),
            () -> assertEquals("Tt Vålse Vig, Vålse Vig", a.name()),
            () -> assertEquals("Opland = 27,01 km2", a.description()),
            () -> assertEquals("41662", a.loggerId()),
            () -> assertEquals(gf.createPoint(new Coordinate(679796.2734,6091352.6536)), a.location()),
            () -> assertEquals(Collections.singletonList(m), a.measurementPoints())
        );
    }

    @Test
    void testEquals() {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 25832);
        JsonStation s = new JsonStation();
        s.setStationUid("2e76caf9-d772-4c07-a6f1-0b7b4cf4d187");
        s.setStationId("61000181");
        s.setOperatorStationId("610181");
        s.setOldStationNumber("61000399");
        s.setLocationType("Vandløb");
        s.setLocationTypeSc(1);
        s.setStationOwnerCvr("DK25798376");
        s.setStationOwnerName("Miljøstyrelsen");
        s.setOperatorCvr("12345678-9012");
        s.setOperatorName("Artificial Operator");
        s.setName("Tt Vålse Vig, Vålse Vig");
        s.setDescription("Opland = 27,01 km2");
        s.setLoggerId("41662");
        s.setLocation(gf.createPoint(new Coordinate(679796.2734,6091352.6536)));
        JsonMeasurementPoint m = new JsonMeasurementPoint();
        m.setNumber(1);
        s.setMeasurementPoints(Collections.singletonList(m));
        s.setStationUid(null);
        assertNotEquals(a, s);
        s.setStationUid("2e76caf9-d772-4c07-a6f1-0b7b4cf4d187");
        s.setStationId(null);
        assertNotEquals(a, s);
        s.setStationId("61000181");
        s.setOperatorStationId(null);
        assertNotEquals(a, s);
        s.setOperatorStationId("610181");
        s.setOldStationNumber(null);
        assertNotEquals(a, s);
        s.setOldStationNumber("61000399");
        s.setLocationType(null);
        assertNotEquals(a, s);
        s.setLocationType("Vandløb");
        s.setLocationTypeSc(0);
        assertNotEquals(a, s);
        s.setLocationTypeSc(1);
        s.setStationOwnerCvr(null);
        assertNotEquals(a, s);
        s.setStationOwnerCvr("DK25798376");
        s.setStationOwnerName(null);
        assertNotEquals(a, s);
        s.setStationOwnerName("Miljøstyrelsen");
        s.setOperatorCvr(null);
        assertNotEquals(a, s);
        s.setOperatorCvr("12345678-9012");
        s.setOperatorName(null);
        assertNotEquals(a, s);
        s.setOperatorName("Artificial Operator");
        s.setName(null);
        assertNotEquals(a, s);
        s.setName("Tt Vålse Vig, Vålse Vig");
        s.setDescription(null);
        assertNotEquals(a, s);
        s.setDescription("Opland = 27,01 km2");
        s.setLoggerId(null);
        assertNotEquals(a, s);
        s.setLoggerId("41662");
        s.setLocation(null);
        assertNotEquals(a, s);
        s.setLocation(gf.createPoint(new Coordinate(679796.2734,6091352.6536)));
        s.setMeasurementPoints(null);
        assertNotEquals(a, s);
        s.setMeasurementPoints(Collections.singletonList(m));
        assertEquals(a, s);
    }

    @Test
    void testString() {
        String e = "JsonStation(stationUid: 2e76caf9-d772-4c07-a6f1-0b7b4cf4d187"
            + ", stationId: »61000181«, operatorStationId: »610181«, oldStationNumber: »61000399«"
            + ", locationType: »Vandløb«, locationTypeSc: 1"
            + ", stationOwnerCvr: »DK25798376«, stationOwnerName: »Miljøstyrelsen«"
            + ", operatorCvr: »12345678-9012«, operatorName: »Artificial Operator«"
            + ", name: »Tt Vålse Vig, Vålse Vig«, description: »Opland = 27,01 km2«"
            + ", loggerId: »41662«, location: POINT (679796.2734 6091352.6536)"
            + ", measurementPoints: ["
            + "JsonMeasurementPoint(number: 1, name: null"
            + ", measurementPointType: null, measurementPointTypeSc: null"
            + ", description: null, location: null"
            + ", intakeNumber: null, examinations: null)"
            + "]"
            + ")";
        assertEquals(e, a.toString());
    }

    @Test
    void testJson() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("station_61000181.json"); Jsonb jsonb = JsonbBuilder.create()) {
            Station station = jsonb.fromJson(is, JsonStation.class);
            assertEquals(a, station);
        }
    }
}