package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.Examination;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class JsonExaminationTest {
Examination a;

@BeforeEach
void setup() {
    JsonExamination e = new JsonExamination();
    e.setParameter("Vandstand DVR90");
    e.setParameterSc(439);
    e.setExaminationType("Kotemåling");
    e.setExaminationTypeSc(21);
    e.setUnit("m");
    e.setUnitSc(63);
    e.setEarliestResult(OffsetDateTime.of(2015,12,31, 23,1,0,0, ZoneOffset.ofHours(0)));
    e.setLatestResult(OffsetDateTime.of(2018,1,1, 12,0,0,0, ZoneOffset.ofHours(0)));
    a = e;
}

@Test
void testGetters() {
    assertAll(
        () -> assertEquals("Vandstand DVR90", a.parameter()),
        () -> assertEquals(439, a.parameterSc()),
        () -> assertEquals("Kotemåling", a.examinationType()),
        () -> assertEquals(21, a.examinationTypeSc()),
        () -> assertEquals("m", a.unit()),
        () -> assertEquals(63, a.unitSc()),
        () -> assertEquals(OffsetDateTime.of(2015,12,31, 23,1,0,0, ZoneOffset.ofHours(0)), a.earliestResult()),
        () -> assertEquals(OffsetDateTime.of(2018,1,1, 12,0,0,0, ZoneOffset.ofHours(0)), a.latestResult())
    );
}

@Test
void testEquals() {
    JsonExamination e = new JsonExamination();
    e.setParameter("Vandstand DVR90");
    e.setParameterSc(439);
    e.setExaminationType("Kotemåling");
    e.setExaminationTypeSc(21);
    e.setUnit("m");
    e.setUnitSc(63);
    e.setEarliestResult(OffsetDateTime.of(2015,12,31, 23,1,0,0, ZoneOffset.ofHours(0)));
    e.setLatestResult(OffsetDateTime.of(2018,1,1, 12,0,0,0, ZoneOffset.ofHours(0)));
    assertEquals(e, a);
    e.setParameter("Vandstand DVR91");
    assertNotEquals(e, a);
    e.setParameter("Vandstand DVR90");
    e.setParameterSc(438);
    assertNotEquals(e, a);
    e.setParameterSc(439);
    e.setExaminationType("Kotemaling");
    assertNotEquals(e, a);
    e.setExaminationType("Kotemåling");
    e.setExaminationTypeSc(20);
    assertNotEquals(e, a);
    e.setExaminationTypeSc(21);
    e.setUnit("n");
    assertNotEquals(e, a);
    e.setUnit("m");
    e.setUnitSc(64);
    assertNotEquals(e, a);
    e.setUnitSc(63);
    e.setEarliestResult(OffsetDateTime.of(2015,12,31, 23,2,0,0, ZoneOffset.ofHours(0)));
    assertNotEquals(e, a);
    e.setEarliestResult(OffsetDateTime.of(2015,12,31, 23,1,0,0, ZoneOffset.ofHours(0)));
    e.setLatestResult(OffsetDateTime.of(2018,1,1, 12,0,1,0, ZoneOffset.ofHours(0)));
    assertNotEquals(e, a);
    e.setLatestResult(OffsetDateTime.of(2018,1,1, 12,0,0,0, ZoneOffset.ofHours(0)));
    assertEquals(e, a);
}

@Test
void testString() {
    String s = "JsonExamination(parameter: »Vandstand DVR90«, parameterSc: 439"
        + ", examinationType: »Kotemåling«, examinationTypeSc: 21"
        + ", unit: »m«, unitSc: 63"
        + ", earliestResult: 2015-12-31T23:01Z, latestResult: 2018-01-01T12:00Z"
        + ")";
    assertEquals(s, a.toString());
}

@Test
void testJson() throws Exception {
    try (InputStream is = getClass().getResourceAsStream("station_61000181_examination_1.json"); Jsonb jsonb = JsonbBuilder.create()) {
        Examination j = jsonb.fromJson(is, JsonExamination.class);
        assertEquals(a, j);
    }
}
}