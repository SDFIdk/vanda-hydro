package dk.dmp.vanda.hydro.service;

import dk.dmp.vanda.hydro.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.http.HttpClient;
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
    private final URLEncodingServiceClient httpLayer;

    /**
     * Construct the service client.
     * @param apiBase Base URL of VanDa Hydro service, e.g.
     *         {@code https://vandah.miljoeportal.dk/api/},
     *         {@code https://vandah.test.miljoeportal.dk/api/} or
     *         {@code https://vandah.demo.miljoeportal.dk/api/}
     * @param httpClient The client for sending HTTP requests.
     */
    public HydrometryHttpService(URI apiBase, HttpClient httpClient) {
        this(new URLEncodingServiceClient(apiBase, httpClient));
    }

    public HydrometryHttpService(URLEncodingServiceClient urlEncodingServiceClient) {
        httpLayer = urlEncodingServiceClient;
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
        private final URLEncodingServiceClient.Request request =
                httpLayer.new Request("stations");

        @Override
        public Iterator<Station> exec() throws IOException, InterruptedException {
            ExtendedInputStreamHttpResponse response = request.submit();
            return transform(response.body(), response.assumedCharset());
        }

        @Override
        public GetStationsOperation stationId(String stationId) {
            request.addQueryParameter("stationId", stationId);
            return this;
        }

        @Override
        public GetStationsOperation operatorStationId(String operatorStationId) {
            request.addQueryParameter("operatorStationId", operatorStationId);
            return this;
        }

        @Override
        public GetStationsOperation stationOwnerCvr(String stationOwnerCvr) {
            request.addQueryParameter("stationOwnerCvr", stationOwnerCvr);
            return this;
        }

        @Override
        public GetStationsOperation operatorCvr(String operatorCvr) {
            request.addQueryParameter("operatorCvr", operatorCvr);
            return this;
        }

        @Override
        public GetStationsOperation parameterSc(int parameterSc) {
            request.addQueryParameter("parameterSc", String.valueOf(parameterSc));
            return this;
        }

        @Override
        public GetStationsOperation examinationTypeSc(int examinationTypeSc) {
            request.addQueryParameter("examinationTypeSc", String.valueOf(examinationTypeSc));
            return this;
        }

        @Override
        public GetStationsOperation withResultsAfter(OffsetDateTime pointInTime) {
            request.addQueryParameter("pointInTime", pointInTime.format(RFC_3339_NO_SECONDS));
            return this;
        }

        private Iterator<Station> transform(InputStream body, Charset charset) throws IOException {
            try (BufferedReader buf = new BufferedReader(new InputStreamReader(body, charset))) {
                log.trace("Input stream: {}", buf.lines().collect(Collectors.joining()));
            }
            return Collections.emptyIterator();
        }
    }
}
