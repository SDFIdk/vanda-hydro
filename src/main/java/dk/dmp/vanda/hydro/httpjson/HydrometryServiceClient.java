package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.HydrometryService;
import dk.dmp.vanda.hydro.Measurement;
import dk.dmp.vanda.hydro.Station;
import dk.dmp.vanda.hydro.WaterLevelMeasurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Stream;

/**
 * A VanDa Hydro service client that expects data to be returned as JSON.
 * All the methods in this class are safe for use by multiple concurrent threads.
 * However, the operations builders are not.
 */
public class HydrometryServiceClient implements HydrometryService, AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final StreamService streamService;
    private final Deserializer deserializer = new Deserializer();

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

    @Override
    public GetWaterFlowsOperation getWaterFlows() {
        return new WaterFlowsRequest();
    }

    /**
     * Closes the internal JSON-B deserializer.
     * @throws IOException If thrown by JSON-B.
     */
    @Override
    public void close() throws Exception {
        deserializer.close();
    }

    private class StationsRequest implements GetStationsOperation {
        private final URLEncodedFormData form = new URLEncodedFormData();
        {
            form.setPath("stations");
        }

        @Override
        public Iterator<Station> exec() throws IOException, InterruptedException {
            List<Station> stations = deserializer.deserializeStations(streamService.get(form.getPath(), form.getFormData()));
            return stations.iterator();
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
            form.append("withResultsAfter", RFC3339NoSecondsFormatter.formatUTC(pointInTime));
        }

        @Override
        public void withResultsCreatedAfter(OffsetDateTime pointInTime) {
            form.append("withResultsCreatedAfter", RFC3339NoSecondsFormatter.formatUTC(pointInTime));
        }
    }

    private class WaterLevelsRequest extends MeasurementsRequest<WaterLevelMeasurement, JsonWaterLevelMeasurement> implements GetWaterLevelsOperation {
        {
            form.setPath("water-levels");
        }

        @Override
        protected List<JsonStationResults<JsonWaterLevelMeasurement>> deserialize(InputStream body) throws IOException {
            return deserializer.deserializeWaterLevels(body);
        }

        @Override
        protected WaterLevelMeasurement cast(JsonWaterLevelMeasurement result) {
            return result;
        }
    }

    private class WaterFlowsRequest extends MeasurementsRequest<Measurement, JsonMeasurement> implements GetWaterFlowsOperation {
        {
            form.setPath("water-flows");
        }

        @Override
        protected List<JsonStationResults<JsonMeasurement>> deserialize(InputStream body) throws IOException {
            return deserializer.deserializeWaterFlows(body);
        }

        @Override
        protected Measurement cast(JsonMeasurement result) {
            return result;
        }
    }

    private abstract class MeasurementsRequest<T, J extends JsonMeasurement> implements GetMeasurements<T> {
        protected final URLEncodedFormData form = new URLEncodedFormData();

        @Override
        public Iterator<T> exec() throws IOException, InterruptedException {
            List<JsonStationResults<J>> stations = deserialize(streamService.get(form.getPath(), form.getFormData()));
            if (stations.isEmpty()) return Collections.emptyIterator();
            if (stations.size() > 1) {
                List<JsonStationId> ids = collectIds(stations);
                log.debug("Multiple stations in response from {}: {}", form, ids);
            }
            Stream<T> m = stations.stream().flatMap(JsonStationResults::denormalize).map(this::cast);
            return m.iterator();
        }

        protected abstract List<JsonStationResults<J>> deserialize(InputStream body) throws IOException;

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

        protected abstract T cast(J result);

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
            form.append("from", RFC3339NoSecondsFormatter.formatUTC(pointInTime));
        }

        @Override
        public void to(OffsetDateTime pointInTime) {
            form.append("to", RFC3339NoSecondsFormatter.formatUTC(pointInTime));
        }

        @Override
        public void createdAfter(OffsetDateTime pointInTime) {
            form.append("createdAfter", RFC3339NoSecondsFormatter.formatUTC(pointInTime));
        }
    }
}
