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
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
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

    private class StationsRequest implements GetStationsOperation {
        private static final Type JsonStationArrayType = new LinkedList<JsonStation>(){}.getClass().getGenericSuperclass();
        private final URLEncodedFormData form = new URLEncodedFormData();
        {
            form.setPath("stations");
        }

        @Override
        public Iterator<Station> exec() throws IOException, InterruptedException {
            return transform(streamService.get(form.getPath(), form.getFormData()));
        }

        @Override
        public void stationId(String stationId) {
            form.append("stationId", stationId);
        }

        @Override
        public void operatorStationId(String operatorStationId) {
            form.append("operatorStationId", operatorStationId);
        }

        @Override
        public void stationOwnerCvr(String stationOwnerCvr) {
            form.append("stationOwnerCvr", stationOwnerCvr);
        }

        @Override
        public void operatorCvr(String operatorCvr) {
            form.append("operatorCvr", operatorCvr);
        }

        @Override
        public void parameterSc(int parameterSc) {
            form.append("parameterSc", String.valueOf(parameterSc));
        }

        @Override
        public void examinationTypeSc(int examinationTypeSc) {
            form.append("examinationTypeSc", String.valueOf(examinationTypeSc));
        }

        @Override
        public void withResultsAfter(OffsetDateTime pointInTime) {
            form.append("withResultsAfter", formatUTCRFC3339NoSeconds(pointInTime));
        }

        @Override
        public void withResultsCreatedAfter(OffsetDateTime pointInTime) {
            form.append("withResultsCreatedAfter", formatUTCRFC3339NoSeconds(pointInTime));
        }

        private Iterator<Station> transform(InputStream body) throws IOException {
            WhitespaceObserver w = new WhitespaceObserver();
            if (body == null) {
                return Collections.emptyIterator();
            } else try (ObservableInputStream is = new ObservableInputStream(body, w)) {
                List<Station> stations = jsonb.fromJson(is, JsonStationArrayType);
                return stations.iterator();
            } catch (JsonbException e) {
                if (w.hasObservedOnlyWhitespace()) return Collections.emptyIterator();
                else throw new IOException("Cannot deserialize response body as JSON", e);
            }
        }
    }

    private class WaterLevelsRequest extends MeasurementsRequest<WaterLevelMeasurement> implements GetWaterLevelsOperation {
        private static final Type JsonStationWaterLevelArrayType = new LinkedList<JsonStationResults<JsonWaterLevelMeasurement>>(){}.getClass().getGenericSuperclass();
        public WaterLevelsRequest() {
            super(JsonStationWaterLevelArrayType);
            form.setPath("water-levels");
        }
    }

    private class MeasurementsRequest<T> implements GetWatercourseMeasurements<T> {
        protected final URLEncodedFormData form = new URLEncodedFormData();
        private final Type JsonStationResultsArrayType;

        public MeasurementsRequest(Type jsonStationResultsArrayType) {
            JsonStationResultsArrayType = jsonStationResultsArrayType;
        }

        @Override
        public Iterator<T> exec() throws IOException, InterruptedException {
            List<JsonStationResults<T>> stations = transform(streamService.get(form.getPath(), form.getFormData()));
            if (stations.isEmpty()) return Collections.emptyIterator();
            if (stations.size() > 1) {
                List<JsonStationId> ids = collectIds(stations);
                log.debug("Will interpret just the first station in response {}", ids);
            }
            List<T> m = stations.getFirst().results;
            if (m != null) return m.iterator();
            else return Collections.emptyIterator();
        }

        private List<JsonStationResults<T>> transform(InputStream body) throws IOException {
            WhitespaceObserver w = new WhitespaceObserver();
            if (body == null) {
                return Collections.emptyList();
            } else try (ObservableInputStream is = new ObservableInputStream(body, w)) {
                return jsonb.fromJson(is, JsonStationResultsArrayType);
            } catch (JsonbException e) {
                if (w.hasObservedOnlyWhitespace()) return Collections.emptyList();
                else throw new IOException("Cannot deserialize response body as JSON", e);
            }
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
        public void stationId(String stationId) {
            form.append("stationId", stationId);
        }

        @Override
        public void operatorStationId(String operatorStationId) {
            form.append("operatorStationId", operatorStationId);
        }

        @Override
        public void measurementPointNumber(int measurementPointNumber) {
            form.append("measurementPointNumber", Integer.toString(measurementPointNumber));
        }

        @Override
        public void from(OffsetDateTime pointInTime) {
            form.append("from", formatUTCRFC3339NoSeconds(pointInTime));
        }

        @Override
        public void to(OffsetDateTime pointInTime) {
            form.append("to", formatUTCRFC3339NoSeconds(pointInTime));
        }

        @Override
        public void createdAfter(OffsetDateTime pointInTime) {
            form.append("createdAfter", formatUTCRFC3339NoSeconds(pointInTime));
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
