package dk.dmp.vanda.hydro.httpjson;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonStationWaterLevelTest {
    @Test
    void testJsonList() throws Exception {
        List<JsonStationResults<JsonWaterLevelMeasurement>> stations;
        try (InputStream is = getClass().getResourceAsStream("water-level_61000181.json"); Jsonb jsonb = JsonbBuilder.create()) {
            Object dummy = new LinkedList<JsonStationResults<JsonWaterLevelMeasurement>>(){};
            Type t = dummy.getClass().getGenericSuperclass();
            stations = jsonb.fromJson(is, t);
        }
        assertAll(
            () -> assertEquals(1, stations.size()),
            () -> assertEquals("61000181", stations.getFirst().stationId),
            () -> assertNull(stations.getFirst().operatorStationId),
            () -> assertEquals(2, stations.getFirst().results.size()),
            () -> assertEquals(OffsetDateTime.parse("2023-10-02T18:05Z"), stations.getFirst().results.get(1).measurementDateTime()),
            () -> assertEquals(OffsetDateTime.parse("2023-10-02T18:10Z"), stations.getFirst().results.getFirst().measurementDateTime())
        );
    }
}