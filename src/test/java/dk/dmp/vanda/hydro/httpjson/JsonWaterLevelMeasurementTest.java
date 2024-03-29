package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.WaterLevelMeasurement;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWaterLevelMeasurementTest {
    WaterLevelMeasurement a, b;

    @BeforeEach
    void setUp() {
        JsonWaterLevelMeasurement m = new JsonWaterLevelMeasurement();
        m.setStationId("61000181");
        m.setOperatorStationId("610181");
        m.setMeasurementPointNumber(1);
        m.setParameterSc(1233);
        m.setParameter("Vandstand");
        m.setExaminationTypeSc(25);
        m.setExaminationType("Vandstand");
        m.setMeasurementDateTime(OffsetDateTime.parse("2023-10-02T18:05Z"));
        m.setResult(31.7);
        m.setResultElevationCorrected(-58.3);
        m.setUnitSc(19);
        m.setUnit("cm");
        a = m;
        m = new JsonWaterLevelMeasurement();
        m.setMeasurementPointNumber(1);
        m.setParameterSc(1233);
        m.setParameter("Vandstand");
        m.setExaminationTypeSc(25);
        m.setExaminationType("Vandstand");
        m.setMeasurementDateTime(OffsetDateTime.parse("2023-10-02T18:05Z"));
        m.setResult(31.7);
        m.setResultElevationCorrected(-58.3);
        m.setUnitSc(19);
        m.setUnit("cm");
        b = m;
    }

    @Test
    void testGetters() {
        assertAll(
            () -> assertEquals("61000181", a.stationId()),
            () -> assertEquals("610181", a.operatorStationId()),
            () -> assertEquals(1, a.measurementPointNumber()),
            () -> assertEquals(1233, a.parameterSc()),
            () -> assertEquals("Vandstand", a.parameter()),
            () -> assertEquals(25, a.examinationTypeSc()),
            () -> assertEquals("Vandstand", a.examinationType()),
            () -> assertEquals(OffsetDateTime.parse("2023-10-02T18:05Z"), a.measurementDateTime()),
            () -> assertEquals(31.7, a.result()),
            () -> assertEquals(-58.3, a.resultElevationCorrected()),
            () -> assertEquals(19, a.unitSc()),
            () -> assertEquals("cm", a.unit())
        );
    }

    @Test
    void TestEquals() {
        JsonWaterLevelMeasurement m = new JsonWaterLevelMeasurement();
        m.setStationId("61000181");
        m.setOperatorStationId("610181");
        m.setMeasurementPointNumber(1);
        m.setParameterSc(1233);
        m.setParameter("Vandstand");
        m.setExaminationTypeSc(25);
        m.setExaminationType("Vandstand");
        m.setMeasurementDateTime(OffsetDateTime.parse("2023-10-02T18:05Z"));
        m.setResult(31.7);
        m.setResultElevationCorrected(-58.3);
        m.setUnitSc(19);
        m.setUnit("cm");

        m.setStationId(null);
        assertNotEquals(a, m);
        m.setStationId("61000181");
        m.setOperatorStationId(null);
        assertNotEquals(a, m);
        m.setOperatorStationId("610181");
        m.setMeasurementPointNumber(0);
        assertNotEquals(a, m);
        m.setMeasurementPointNumber(1);
        m.setParameterSc(0);
        assertNotEquals(a, m);
        m.setParameterSc(1233);
        m.setParameter(null);
        assertNotEquals(a, m);
        m.setParameter("Vandstand");
        m.setExaminationTypeSc(0);
        assertNotEquals(a, m);
        m.setExaminationTypeSc(25);
        m.setExaminationType(null);
        assertNotEquals(a, m);
        m.setExaminationType("Vandstand");
        m.setMeasurementDateTime(null);
        assertNotEquals(a, m);
        m.setMeasurementDateTime(OffsetDateTime.parse("2023-10-02T18:05Z"));
        m.setResult(0);
        assertNotEquals(a, m);
        m.setResult(31.7);
        m.setResultElevationCorrected(0.0);
        assertNotEquals(a, m);
        m.setResultElevationCorrected(-58.3);
        m.setUnitSc(0);
        assertNotEquals(a, m);
        m.setUnitSc(19);
        m.setUnit(null);
        assertNotEquals(a, m);
        m.setUnit("cm");
        assertEquals(a, m);
    }

    @Test
    void testString() {
        String expected = "JsonWaterLevelMeasurement("
            + "stationId: »61000181«, operatorStationId: »610181«"
            + ", measurementPointNumber: 1, parameterSc: 1233, parameter: »Vandstand«"
            + ", examinationTypeSc: 25, examinationType: »Vandstand«"
            + ", measurementDateTime: 2023-10-02T18:05Z"
            + ", result: 31.7"
            + ", unitSc: 19, unit: »cm«"
            + ", resultElevationCorrected: -58.3)";
        assertEquals(expected, a.toString());
    }

    @Test
    void testJson() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("water-level_61000181_1805.json"); Jsonb jsonb = JsonbBuilder.create()) {
            WaterLevelMeasurement m = jsonb.fromJson(is, JsonWaterLevelMeasurement.class);
            assertEquals(b, m);
        }
    }

    @Test
    void testJsonList() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("water-level_61000181_1805_array.json"); Jsonb jsonb = JsonbBuilder.create()) {
            Object dummy = new LinkedList<JsonWaterLevelMeasurement>(){};
            Type t = dummy.getClass().getGenericSuperclass();
            List<JsonWaterLevelMeasurement> m = jsonb.fromJson(is, t);
            assertIterableEquals(Collections.singletonList(b), m);
        }
    }
}