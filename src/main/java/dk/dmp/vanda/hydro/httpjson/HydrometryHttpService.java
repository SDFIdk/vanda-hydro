package dk.dmp.vanda.hydro.httpjson;

import dk.dmp.vanda.hydro.HydrometryService;
import dk.dmp.vanda.hydro.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The implementation is immutable, thus thread-safe.
 * However, the operations builders are not.
 */
public class HydrometryHttpService implements HydrometryService {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final URI apiBase;
    private final StreamHttpClient httpClient;

    /**
     * Construct the service client.
     * @param apiBase Base URL of VanDa Hydro service, e.g.
     *         {@code https://vandah.miljoeportal.dk/api/},
     *         {@code https://vandah.test.miljoeportal.dk/api/} or
     *         {@code https://vandah.demo.miljoeportal.dk/api/}
     * @param httpClient The client for sending HTTP requests.
     */
    public HydrometryHttpService(URI apiBase, StreamHttpClient httpClient) {
        if (apiBase.getPath() == null) {
            throw new IllegalArgumentException(String.format("Given VanDa Hydrometry API base URL has no path component: %s", apiBase));
        }
        else if (! apiBase.getPath().endsWith("/")) {
            log.warn("Given VanDa Hydrometry API base URL does not end with '/': {}", apiBase);
        }
        else if (! apiBase.getPath().equals("/api/")) {
            log.debug("Given VanDa Hydrometry API base URL does not end with '/api/': {}", apiBase);
        }
        this.apiBase = apiBase;
        this.httpClient = httpClient;
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

    private class StationsHttpRequest implements GetStationsOperation {
        private final OperationPathAndParameters form = new OperationPathAndParameters();
        {
            form.setPath("stations");
        }

        @Override
        public Iterator<Station> exec() throws IOException, InterruptedException {
            ExtendedHttpResponse<InputStream> response = httpClient.submit(form.appendToURL(apiBase));
            return transform(response.body(), response.determineCharset());
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
            form.addQueryParameter("pointInTime", pointInTime.format(RFC_3339_NO_SECONDS));
            return this;
        }

        private Iterator<Station> transform(InputStream body, Charset charset) throws IOException {
            if (body == null) {
                log.trace("Input stream: null");
            } else try (BufferedReader buf = new BufferedReader(new InputStreamReader(body, charset))) {
                log.trace("Input stream: {}", buf.lines().collect(Collectors.joining()));
            }
            return Collections.emptyIterator();
        }
    }
}
