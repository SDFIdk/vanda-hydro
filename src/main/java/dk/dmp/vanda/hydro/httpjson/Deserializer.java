package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.Station;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import org.apache.commons.io.input.ObservableInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Deserializer for VanDa Hydro JSON data.
 * All the methods in this class are safe for use by multiple concurrent threads.
 */
public class Deserializer implements AutoCloseable {
    private final Jsonb jsonb = JsonbBuilder.create();

    /**
     * Closes the internal JSON-B deserializer.
     * @throws IOException If thrown by JSON-B.
     */
    @Override
    public void close() throws Exception {
        jsonb.close();
    }

    private interface JsonStationArray extends List<JsonStation> {}
    private static final Type JsonStationArrayType = JsonStationArray.class.getGenericInterfaces()[0];

    public List<Station> deserializeStations(InputStream body) throws IOException {
        return fromJson(body, JsonStationArrayType);
    }

    private interface JsonStationWaterFlowArray extends List<JsonStationResults<JsonMeasurement>>{}
    private final Type JsonStationWaterFlowArrayType = JsonStationWaterFlowArray.class.getGenericInterfaces()[0];

    public List<JsonStationResults<JsonMeasurement>> deserializeWaterFlows(InputStream body) throws IOException {
        return fromJson(body, JsonStationWaterFlowArrayType);
    }

    private interface JsonStationWaterLevelArray extends List<JsonStationResults<JsonWaterLevelMeasurement>>{}
    private final Type JsonStationWaterLevelArrayType = JsonStationWaterLevelArray.class.getGenericInterfaces()[0];

    public List<JsonStationResults<JsonWaterLevelMeasurement>> deserializeWaterLevels(InputStream body) throws IOException {
        return fromJson(body, JsonStationWaterLevelArrayType);
    }

    private <T> List<T> fromJson(InputStream body, Type jsonType) throws IOException {
        WhitespaceObserver w = new WhitespaceObserver();
        if (body == null) {
            return Collections.emptyList();
        } else try (ObservableInputStream is = new ObservableInputStream(body, w)) {
            return jsonb.fromJson(is, jsonType);
        } catch (JsonbException e) {
            if (w.hasObservedOnlyWhitespace()) return Collections.emptyList();
            else throw new IOException("Cannot deserialize stream as " + jsonType.getTypeName(), e);
        }
    }

    /**
     * Poor man's check for empty input stream: works only for ASCII-like
     * encodings, e.g. UTF-8.
     */
    private static class WhitespaceObserver extends ObservableInputStream.Observer {
        private boolean observedOnlyWhitespace = true;

        public boolean hasObservedOnlyWhitespace() {
            return observedOnlyWhitespace;
        }

        @Override
        public void data(byte[] buffer, int offset, int length) {
            for (int i = offset, n = length; observedOnlyWhitespace && n > 0; ++i, --n)
                data(buffer[i]);
        }

        @Override
        public void data(int value) {
            if (observedOnlyWhitespace && !Character.isWhitespace(value)) observedOnlyWhitespace = false;
        }
    }
}
