package dk.dmp.vanda.hydro.service;

import dk.dmp.vanda.hydro.Station;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JsonStationTest {
    GeometryFactory gfac = new GeometryFactory(new PrecisionModel(), 25832);

    @Test
    void testOne() throws Exception {
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
                    () -> assertEquals(gfac.createPoint(new Coordinate(679796.2734,6091352.6536)), station.location()),
                    () -> assertEquals("XXX", Arrays.toString(station.measurementPoints()))
            );
        }
    }
}