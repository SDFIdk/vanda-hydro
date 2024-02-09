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
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * The implementation is immutable, thus thread-safe.
 * However, the operations builders are not.
 */
public class HydrometryHttpService implements HydrometryService {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final URI apiBase;
    private final HttpClient httpClient;

    /**
     * Construct the service client.
     * @param apiBase Base URL of VanDa Hydro service, e.g.
     *         {@code https://vandah.miljoeportal.dk/api/},
     *         {@code https://vandah.test.miljoeportal.dk/api/} or
     *         {@code https://vandah.demo.miljoeportal.dk/api/}
     * @param httpClient The client for sending HTTP requests.
     */
    public HydrometryHttpService(URI apiBase, HttpClient httpClient) throws URISyntaxException {
        if (apiBase.getPath() == null) {
            throw new URISyntaxException(apiBase.toString(), "Given VanDa Hydrometry API base URL has no path component.");
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

    private class Operation<T> {
        private final URIBuilder uriBuilder;
        private final BiFunction<InputStream,Charset,Iterator<T>> transformer;

        private Operation(String path, BiFunction<InputStream,Charset,Iterator<T>> transformer) {
            uriBuilder = new URIBuilder(apiBase, path);
            this.transformer = transformer;
        }

        /**
         * Add a query parameter.
         * @param parm Query parameter name.
         * @param value Query parameter value.
         */
        protected void addQueryParameter(String parm, String value) {
            uriBuilder.addQueryParameter(parm, value);
        }

        public Iterator<T> exec() throws IOException, InterruptedException {
            HttpRequest req = buildRequest();
            HttpResponse<InputStream> response = httpClient.send(req, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() != 200) {
                throw new HttpResponseException(response);
            }
            checkMediaType(response);
            Charset contentCharset = getCharset(response);
            return transformer.apply(response.body(), contentCharset);
        }

        /**
         * Build a request from the API base URI, operation path and
         * query parameters. Let the request accept JSON as return data.
         * @return The built request.
         */
        private HttpRequest buildRequest() {
            try {
                return HttpRequest.newBuilder(uriBuilder.buildURI())
                        .header("Accept", "application/json")
                        .build();
            } catch (URISyntaxException e) {
                throw new RuntimeException("Internal error, query parameters not properly validated.", e);
            }
        }

        private void checkMediaType(HttpResponse<?> response) {
            Optional<ContentType> contentType = ContentType.fromHttpResponse(response);
            Optional<String> mediaType = contentType.flatMap(ContentType::getMediaType);
            if (mediaType.isPresent() && ! mediaType.get().equalsIgnoreCase("application/json"))
                log.debug("Unexpected media type, {}, in response from {}.", mediaType.get(), response.uri());
        }

        private Charset getCharset(HttpResponse<?> response) {
            Optional<ContentType> contentType = ContentType.fromHttpResponse(response);
            Charset contentCharset = contentType.flatMap(ContentType::getCharset).orElse(StandardCharsets.UTF_8);
            log.trace("Using charset {} for {}", contentCharset, response.uri());
            return contentCharset;
        }
    }

    private static class LoggingTransformer<T> implements BiFunction<InputStream,Charset,Iterator<T>> {
        public Iterator<T> apply(InputStream jsonData, Charset charset) {
            try (BufferedReader buf = new BufferedReader(new InputStreamReader(jsonData, charset))) {
                log.trace("Input stream: {}", buf.lines().collect(Collectors.joining()));
            } catch (IOException e) {
                log.debug("Unable to read input stream.", e);
            }
            return null;
        }
    }

    private class StationsHttpRequest extends Operation<Station> implements GetStationsOperation {
        public StationsHttpRequest() {
            super("stations", new LoggingTransformer<>());
        }

        @Override
        public GetStationsOperation stationId(String stationId) {
            addQueryParameter("stationId", stationId);
            return this;
        }

        @Override
        public GetStationsOperation operatorStationId(String operatorStationId) {
            addQueryParameter("operatorStationId", operatorStationId);
            return this;
        }

        @Override
        public GetStationsOperation stationOwnerCvr(String stationOwnerCvr) {
            addQueryParameter("stationOwnerCvr", stationOwnerCvr);
            return this;
        }

        @Override
        public GetStationsOperation operatorCvr(String operatorCvr) {
            addQueryParameter("operatorCvr", operatorCvr);
            return this;
        }

        @Override
        public GetStationsOperation parameterSc(int parameterSc) {
            addQueryParameter("parameterSc", String.valueOf(parameterSc));
            return this;
        }

        @Override
        public GetStationsOperation examinationTypeSc(int examinationTypeSc) {
            addQueryParameter("examinationTypeSc", String.valueOf(examinationTypeSc));
            return this;
        }

        @Override
        public GetStationsOperation withResultsAfter(OffsetDateTime pointInTime) {
            addQueryParameter("pointInTime", pointInTime.format(RFC_3339_NO_SECONDS));
            return this;
        }
    }
}
