package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.HydrometryService;
import dk.dmp.vanda.hydro.Station;
import dk.dmp.vanda.hydro.WaterLevelMeasurement;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import org.apache.commons.io.input.ObservableInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

/**
 * The implementation is immutable, thus thread-safe.
 * However, the operations builders are not.
 */
public class HydrometryServiceClient implements HydrometryService, AutoCloseable {
    private final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Jsonb jsonb = JsonbBuilder.create();
    private final StreamService streamService;

    /**
     * Construct the service client.
     * @param streamService A service that delivers JSON stream data.
     */
    public HydrometryServiceClient(StreamService streamService) {
        this.streamService = Objects.requireNonNull(streamService);
    }

    @Override
    public GetStationsOperation getStations() {
        return new StationsRequest();
    }

    @Override
    public GetWaterLevelsOperation getWaterLevels() {
        return new WaterLevelsRequest();
    }

    /**
     * Closes the internal JSON-B deserializer.
     * @throws IOException If thrown by JSON-B.
     */
    @Override
    public void close() throws Exception {
        jsonb.close();
    }

    private static final Type JsonStationArrayType = new LinkedList<JsonStation>(){}.getClass().getGenericSuperclass();
    private class StationsRequest implements GetStationsOperation {
        private final URLEncodedFormData form = new URLEncodedFormData();
        {
            form.setPath("stations");
        }

        @Override
        public Iterator<Station> exec() throws IOException, InterruptedException {
            return transform(streamService.get(form.getPath(), form.getFormData()));
        }

        @Override
        public GetStationsOperation stationId(String stationId) {
            form.append("stationId", stationId);
            return this;
        }

        @Override
        public GetStationsOperation operatorStationId(String operatorStationId) {
            form.append("operatorStationId", operatorStationId);
            return this;
        }

        @Override
        public GetStationsOperation stationOwnerCvr(String stationOwnerCvr) {
            form.append("stationOwnerCvr", stationOwnerCvr);
            return this;
        }

        @Override
        public GetStationsOperation operatorCvr(String operatorCvr) {
            form.append("operatorCvr", operatorCvr);
            return this;
        }

        @Override
        public GetStationsOperation parameterSc(int parameterSc) {
            form.append("parameterSc", String.valueOf(parameterSc));
            return this;
        }

        @Override
        public GetStationsOperation examinationTypeSc(int examinationTypeSc) {
            form.append("examinationTypeSc", String.valueOf(examinationTypeSc));
            return this;
        }

        @Override
        public GetStationsOperation withResultsAfter(OffsetDateTime pointInTime) {
            form.append("withResultsAfter", formatUTCRFC3339NoSeconds(pointInTime));
            return this;
        }

        @Override
        public GetStationsOperation withResultsCreatedAfter(OffsetDateTime pointInTime) {
            form.append("withResultsCreatedAfter", formatUTCRFC3339NoSeconds(pointInTime));
            return this;
        }

        private Iterator<Station> transform(InputStream body) throws IOException {
            WhitespaceObserver w = new WhitespaceObserver();
            if (body == null) {
                return Collections.emptyIterator();
            } else try (ObservableInputStream is = new ObservableInputStream(body, w)) {
                List<JsonStation> jstations = jsonb.fromJson(is, JsonStationArrayType);
                @SuppressWarnings("unchecked")
                List<Station> stations = (List<Station>)(List<?>)jstations;
                return stations.iterator();
            } catch (JsonbException e) {
                if (w.hasObservedOnlyWhitespace()) return Collections.emptyIterator();
                else throw new IOException("Cannot deserialize response body as JSON", e);
            }
        }
    }

    private static final Type JsonStationWaterLevelArrayType = new LinkedList<JsonStationResults<JsonWaterLevelMeasurement>>(){}.getClass().getGenericSuperclass();
    private class WaterLevelsRequest implements GetWaterLevelsOperation {
        private final URLEncodedFormData form = new URLEncodedFormData();
        {
            form.setPath("water-levels");
        }

        @Override
        public Iterator<WaterLevelMeasurement> exec() throws IOException, InterruptedException {
            List<JsonStationResults<JsonWaterLevelMeasurement>> stations = transform(streamService.get(form.getPath(), form.getFormData()));
            if (stations.isEmpty()) return Collections.emptyIterator();
            if (stations.size() > 1) {
                List<JsonStationId> ids = collectIds(stations);
                log.debug("GetWaterLevels will interpret just the first station in response, got: {}", ids);
            }
            @SuppressWarnings("unchecked")
            List<WaterLevelMeasurement> m = (List<WaterLevelMeasurement>)(List<?>)stations.getFirst().results;
            if (m != null) return m.iterator();
            else return Collections.emptyIterator();
        }

        private static List<JsonStationId> collectIds(List<? extends JsonStationResults<?>> stations) {
            List<JsonStationId> ids = new LinkedList<>();
            for (JsonStationResults<?> s : stations) {
                JsonStationId n = new JsonStationId();
                n.stationId = s.stationId;
                n.operatorStationId = s.operatorStationId;
                ids.add(n);
            }
            return ids;
        }

        @Override
        public GetWaterLevelsOperation stationId(String stationId) {
            form.append("stationId", stationId);
            return this;
        }

        @Override
        public GetWaterLevelsOperation operatorStationId(String operatorStationId) {
            form.append("operatorStationId", operatorStationId);
            return this;
        }

        @Override
        public GetWaterLevelsOperation measurementPointNumber(int measurementPointNumber) {
            form.append("measurementPointNumber", Integer.toString(measurementPointNumber));
            return this;
        }

        @Override
        public GetWaterLevelsOperation from(OffsetDateTime pointInTime) {
            form.append("from", formatUTCRFC3339NoSeconds(pointInTime));
            return this;
        }

        @Override
        public GetWaterLevelsOperation to(OffsetDateTime pointInTime) {
            form.append("to", formatUTCRFC3339NoSeconds(pointInTime));
            return this;
        }

        @Override
        public GetWaterLevelsOperation createdAfter(OffsetDateTime pointInTime) {
            form.append("createdAfter", formatUTCRFC3339NoSeconds(pointInTime));
            return this;
        }

        private List<JsonStationResults<JsonWaterLevelMeasurement>> transform(InputStream body) throws IOException {
            WhitespaceObserver w = new WhitespaceObserver();
            if (body == null) {
                return Collections.emptyList();
            } else try (ObservableInputStream is = new ObservableInputStream(body, w)) {
                return jsonb.fromJson(is, JsonStationWaterLevelArrayType);
            } catch (JsonbException e) {
                if (w.hasObservedOnlyWhitespace()) return Collections.emptyList();
                else throw new IOException("Cannot deserialize response body as JSON", e);
            }
        }
    }

    /**
     * According to the OpenAPI specification of the service in test,
     * all input timestamp arguments must be given as a UTC timestamps in
     * the RFC 3339 date+time format without seconds.
     */
    private static String formatUTCRFC3339NoSeconds(OffsetDateTime t) {
        return t.atZoneSameInstant(ZoneOffset.UTC).format(RFC_3339_NO_SECONDS);
    }
    private static final DateTimeFormatter RFC_3339_NO_SECONDS =
        new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral('T')
            .appendValue(java.time.temporal.ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(java.time.temporal.ChronoField.MINUTE_OF_HOUR, 2)
            .parseLenient()
            .appendOffsetId()
            .parseStrict()
            .toFormatter();

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
