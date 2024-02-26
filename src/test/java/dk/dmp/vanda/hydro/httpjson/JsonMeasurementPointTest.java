package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.MeasurementPoint;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

import java.io.InputStream;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JsonMeasurementPointTest {
MeasurementPoint a;

@BeforeEach
void setup() {
    GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 25832);
    JsonMeasurementPoint m = new JsonMeasurementPoint();
    m.setNumber(1);
    m.setName("Sted 1");
    m.setMeasurementPointType("Vandløb");
    m.setMeasurementPointTypeSc(1);
    m.setDescription(null);
    m.setLocation(gf.createPoint(new Coordinate(679796.2734, 6091352.6536)));
    m.setIntakeNumber(null);
    JsonExamination e1 = new JsonExamination();
    e1.setParameter("Vandstand");
    m.setExaminations(Collections.singletonList(e1));
    a = m;
}

@Test
void testGetters() {
    GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 25832);
    JsonExamination e1 = new JsonExamination();
    e1.setParameter("Vandstand");
    assertAll(
        () -> assertEquals(1, a.number()),
        () -> assertEquals("Sted 1", a.name()),
        () -> assertEquals("Vandløb", a.measurementPointType()),
        () -> assertEquals(1, a.measurementPointTypeSc()),
        () -> assertNull(a.description()),
        () -> assertEquals(gf.createPoint(new Coordinate(679796.2734, 6091352.6536)), a.location()),
        () -> assertNull(a.intakeNumber()),
        () -> assertIterableEquals(Collections.singletonList(e1), a.examinations())
    );
}

@Test
void testEquals() {
    GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 25832);
    JsonMeasurementPoint m = new JsonMeasurementPoint();
    m.setNumber(1);
    m.setName("Sted 1");
    m.setMeasurementPointType("Vandløb");
    m.setMeasurementPointTypeSc(1);
    m.setDescription(null);
    m.setLocation(gf.createPoint(new Coordinate(679796.2734, 6091352.6536)));
    m.setIntakeNumber(null);
    JsonExamination e1 = new JsonExamination();
    e1.setParameter("Vandstand");
    m.setExaminations(Collections.singletonList(e1));
    m.setNumber(0);
    assertNotEquals(a, m);
    m.setNumber(1);
    m.setName(null);
    assertNotEquals(a, m);
    m.setName("Sted 1");
    m.setMeasurementPointType(null);
    assertNotEquals(a, m);
    m.setMeasurementPointType("Vandløb");
    m.setMeasurementPointTypeSc(null);
    assertNotEquals(a, m);
    m.setMeasurementPointTypeSc(1);
    m.setDescription("");
    assertNotEquals(a, m);
    m.setDescription(null);
    m.setLocation(null);
    assertNotEquals(a, m);
    m.setLocation(gf.createPoint(new Coordinate(679796.2734, 6091352.6536)));
    m.setIntakeNumber(0);
    assertNotEquals(a, m);
    m.setIntakeNumber(null);
    m.setExaminations(Collections.emptyList());
    assertNotEquals(a, m);
    m.setExaminations(Collections.singletonList(e1));
    assertEquals(a, m);
}

@Test
void testString() {
    String s = "JsonMeasurementPoint(number: 1, name: »Sted 1«"
        + ", measurementPointType: »Vandløb«, measurementPointTypeSc: 1"
        + ", description: null, location: POINT (679796.2734 6091352.6536)"
        + ", intakeNumber: null"
        + ", examinations: ["
        + "JsonExamination(parameter: »Vandstand«, parameterSc: null"
        + ", examinationType: null, examinationTypeSc: null"
        + ", unit: null, unitSc: null, earliestResult: null"
        + ", latestResult: null)"
        + "]"
        + ")";
    assertEquals(s, a.toString());
}

@Test
void testJson() throws Exception {
    try (InputStream is = getClass().getResourceAsStream("station_61000181_measurementPoint_1.json"); Jsonb jsonb = JsonbBuilder.create()) {
        MeasurementPoint j = jsonb.fromJson(is, JsonMeasurementPoint.class);
        assertEquals(a, j);
    }
}

}
