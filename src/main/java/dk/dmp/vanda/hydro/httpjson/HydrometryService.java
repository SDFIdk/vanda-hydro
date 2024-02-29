package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.Station;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import org.apache.commons.io.input.ObservableInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

/**
 * The implementation is immutable, thus thread-safe.
 * However, the operations builders are not.
 */
public class HydrometryService implements dk.dmp.vanda.hydro.HydrometryService, AutoCloseable {
    private final Jsonb jsonb = JsonbBuilder.create();
    private final JsonStreamService streamService;

    /**
     * Construct the service client.
     * @param streamService A basic service layer.
     */
    public HydrometryService(JsonStreamService streamService) {
        this.streamService = Objects.requireNonNull(streamService);
    }

    @Override
    public GetStationsOperation getStations() {
        return new StationsHttpRequest();
    }

    @Override
    public GetWaterLevelsOperation getWaterLevels() {
        return null;
    }

    /**
     * According to the OpenAPI specification of the service in test,
     * withResultsAfter, from and to must be given as a UTC timestamp in
     * the RFC 3339 date+time format without seconds.
     */
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
     * Closes the internal JSON-B deserializer.
     * @throws IOException If thrown by JSON-B.
     */
    @Override
    public void close() throws Exception {
        jsonb.close();
    }

    private static final Type JsonStationArrayType = new LinkedList<JsonStation>(){}.getClass().getGenericSuperclass();
    private class StationsHttpRequest implements GetStationsOperation {
        private final OperationPathAndParameters form = new OperationPathAndParameters();
        {
            form.setPath("stations");
        }

        @Override
        public Iterator<Station> exec() throws IOException, InterruptedException {
            try {
                return transform(streamService.submit(form.toString()));
            } catch (URISyntaxException e) {
                throw new RuntimeException("Internal error. Relative path or query parameters not properly escaped.", e);
            }
        }

        @Override
        public GetStationsOperation stationId(String stationId) {
            form.addQueryParameter("stationId", stationId);
            return this;
        }

        @Override
        public GetStationsOperation operatorStationId(String operatorStationId) {
            form.addQueryParameter("operatorStationId", operatorStationId);
            return this;
        }

        @Override
        public GetStationsOperation stationOwnerCvr(String stationOwnerCvr) {
            form.addQueryParameter("stationOwnerCvr", stationOwnerCvr);
            return this;
        }

        @Override
        public GetStationsOperation operatorCvr(String operatorCvr) {
            form.addQueryParameter("operatorCvr", operatorCvr);
            return this;
        }

        @Override
        public GetStationsOperation parameterSc(int parameterSc) {
            form.addQueryParameter("parameterSc", String.valueOf(parameterSc));
            return this;
        }

        @Override
        public GetStationsOperation examinationTypeSc(int examinationTypeSc) {
            form.addQueryParameter("examinationTypeSc", String.valueOf(examinationTypeSc));
            return this;
        }

        @Override
        public GetStationsOperation withResultsAfter(OffsetDateTime pointInTime) {
            form.addQueryParameter("withResultsAfter", pointInTime.format(RFC_3339_NO_SECONDS));
            return this;
        }

        private Iterator<Station> transform(InputStream body) throws IOException {
            WhitespaceObserver w = new WhitespaceObserver();
            if (body == null) {
                return Collections.emptyIterator();
            } else try {
                ObservableInputStream is = new ObservableInputStream(body, w);
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
