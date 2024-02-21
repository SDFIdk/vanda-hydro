package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.Examination;
import dk.dmp.vanda.hydro.MeasurementPoint;
import dk.dmp.vanda.hydro.Station;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JsonStationTest {
    GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 25832);

    @Test
    void testGetters() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("station_61000181.json"); Jsonb jsonb = JsonbBuilder.create()) {
            assertNotNull(is);
            Station station = jsonb.fromJson(is, JsonStation.class);
            assertAll(
                    () -> assertEquals(UUID.fromString("2e76caf9-d772-4c07-a6f1-0b7b4cf4d187"), station.stationUid()),
                    () -> assertEquals("61000181", station.stationId()),
                    () -> assertNull(station.operatorStationId()),
                    () -> assertEquals("61000399", station.oldStationNumber()),
                    () -> assertEquals("Vandløb", station.locationType()),
                    () -> assertEquals(1, station.locationTypeSc()),
                    () -> assertEquals("DK25798376", station.stationOwnerCvr()),
                    () -> assertEquals("Miljøstyrelsen", station.stationOwnerName()),
                    () -> assertNull(station.operatorCvr()),
                    () -> assertNull(station.operatorName()),
                    () -> assertEquals("Tt Vålse Vig, Vålse Vig", station.name()),
                    () -> assertEquals("Opland = 27,01 km2", station.description()),
                    () -> assertEquals("41662", station.loggerId()),
                    () -> assertEquals(gf.createPoint(new Coordinate(679796.2734,6091352.6536)), station.location()),
                    () -> assertEquals(1, station.measurementPoints().length)
            );
            MeasurementPoint m = station.measurementPoints()[0];
            assertAll(
                    () -> assertEquals(1, m.number()),
                    () -> assertEquals("Sted 1", m.name()),
                    () -> assertEquals("Vandløb", m.measurementPointType()),
                    () -> assertEquals(1, m.measurementPointTypeSc()),
                    () -> assertNull(m.description()),
                    () -> assertEquals(gf.createPoint(new Coordinate(679796.2734,6091352.6536)), m.location()),
                    () -> assertEquals(3, m.examinations().length)
            );
            Examination e = m.examinations()[0];
            assertAll(
                    () -> assertEquals("", e.parameter()),
                    () -> assertEquals(1, e.parameterSc()),
                    () -> assertEquals("", e.examinationType()),
                    () -> assertEquals(1, e.examinationTypeSc()),
                    () -> assertEquals("", e.unit()),
                    () -> assertEquals(1, e.unitSc()),
                    () -> assertEquals(OffsetDateTime.now(), e.earliestResult()),
                    () -> assertEquals(OffsetDateTime.now(), e.latestResult())
            );
        }
    }
}