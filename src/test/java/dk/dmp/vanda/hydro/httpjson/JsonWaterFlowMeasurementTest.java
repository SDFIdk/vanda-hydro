package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.Measurement;
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

class JsonWaterFlowMeasurementTest {
    Measurement a, b;

    @BeforeEach
    void setUp() {
        JsonMeasurement m = new JsonMeasurement();
        m.setStationId("61000181");
        m.setOperatorStationId("610181");
        m.setMeasurementPointNumber(1);
        m.setParameterSc(1155);
        m.setParameter("Vandføring");
        m.setExaminationTypeSc(27);
        m.setExaminationType("Vandføring");
        m.setMeasurementDateTime(OffsetDateTime.parse("2023-10-02T18:10Z"));
        m.setResult(-1403.8);
        m.setUnitSc(55);
        m.setUnit("l/s");
        a = m;
        m = new JsonMeasurement();
        m.setMeasurementPointNumber(1);
        m.setParameterSc(1155);
        m.setParameter("Vandføring");
        m.setExaminationTypeSc(27);
        m.setExaminationType("Vandføring");
        m.setMeasurementDateTime(OffsetDateTime.parse("2023-10-02T18:10Z"));
        m.setResult(-1403.8);
        m.setUnitSc(55);
        m.setUnit("l/s");
        b = m;
    }

    @Test
    void testGetters() {
        assertAll(
            () -> assertEquals("61000181", a.stationId()),
            () -> assertEquals("610181", a.operatorStationId()),
            () -> assertEquals(1, a.measurementPointNumber()),
            () -> assertEquals(1155, a.parameterSc()),
            () -> assertEquals("Vandføring", a.parameter()),
            () -> assertEquals(27, a.examinationTypeSc()),
            () -> assertEquals("Vandføring", a.examinationType()),
            () -> assertEquals(OffsetDateTime.parse("2023-10-02T18:10Z"), a.measurementDateTime()),
            () -> assertEquals(-1403.8, a.result()),
            () -> assertEquals(55, a.unitSc()),
            () -> assertEquals("l/s", a.unit())
        );
    }

    @Test
    void TestEquals() {
        JsonMeasurement m = new JsonMeasurement();
        m.setStationId("61000181");
        m.setOperatorStationId("610181");
        m.setMeasurementPointNumber(1);
        m.setParameterSc(1155);
        m.setParameter("Vandføring");
        m.setExaminationTypeSc(27);
        m.setExaminationType("Vandføring");
        m.setMeasurementDateTime(OffsetDateTime.parse("2023-10-02T18:10Z"));
        m.setResult(-1403.8);
        m.setUnitSc(55);
        m.setUnit("l/s");

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
        m.setParameterSc(1155);
        m.setParameter(null);
        assertNotEquals(a, m);
        m.setParameter("Vandføring");
        m.setExaminationTypeSc(0);
        assertNotEquals(a, m);
        m.setExaminationTypeSc(27);
        m.setExaminationType(null);
        assertNotEquals(a, m);
        m.setExaminationType("Vandføring");
        m.setMeasurementDateTime(null);
        assertNotEquals(a, m);
        m.setMeasurementDateTime(OffsetDateTime.parse("2023-10-02T18:10Z"));
        m.setResult(0);
        assertNotEquals(a, m);
        m.setResult(-1403.8);
        m.setUnitSc(0);
        assertNotEquals(a, m);
        m.setUnitSc(55);
        m.setUnit(null);
        assertNotEquals(a, m);
        m.setUnit("l/s");
        assertEquals(a, m);
    }

    @Test
    void testString() {
        String expected = "JsonMeasurement("
            + "stationId: »61000181«, operatorStationId: »610181«"
            + ", measurementPointNumber: 1, parameterSc: 1155, parameter: »Vandføring«"
            + ", examinationTypeSc: 27, examinationType: »Vandføring«"
            + ", measurementDateTime: 2023-10-02T18:10Z"
            + ", result: -1403.8"
            + ", unitSc: 55, unit: »l/s«)";
        assertEquals(expected, a.toString());
    }

    @Test
    void testJson() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("water-flow_61000181_1810.json"); Jsonb jsonb = JsonbBuilder.create()) {
            Measurement m = jsonb.fromJson(is, JsonMeasurement.class);
            assertEquals(b, m);
        }
    }

    @Test
    void testJsonList() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("water-flow_61000181_1810_array.json"); Jsonb jsonb = JsonbBuilder.create()) {
            Object dummy = new LinkedList<JsonMeasurement>(){};
            Type t = dummy.getClass().getGenericSuperclass();
            List<JsonMeasurement> m = jsonb.fromJson(is, t);
            assertIterableEquals(Collections.singletonList(b), m);
        }
    }
}