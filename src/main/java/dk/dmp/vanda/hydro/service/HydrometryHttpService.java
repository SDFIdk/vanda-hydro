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
    public HydrometryHttpService(URI apiBase, HttpClient httpClient) {
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

    /**
     * A mutable, non-thread-safe class for building the path and query
     * string and invoking the remote service operation.
     */
    protected abstract class Operation<T> {
        private final String opPath;
        private final List<Map.Entry<String, String>> params = new LinkedList<>();

        /**
         * Create the operation builder.
         * @param path Path to operation relative to service API base URI.
         */
        public Operation(String path) {
            opPath = path;
        }

        /**
         * Add a query parameter.
         * @param parm Query parameter name.
         * @param value Query parameter value.
         */
        protected void addQueryParameter(String parm, String value) {
            params.add(Map.entry(parm, value));
        }

        /**
         * Make a URI with path and query from this builder.
         * @return The created URI.
         */
        protected URI buildURI() {
            try {
                String q = params.stream()
                        .map(e -> e.getKey() + "=" + e.getValue())
                        .collect(Collectors.joining("&"));
                if (q.isEmpty()) q = null;
                URI relative = new URI(null, null, opPath, q, null);
                return apiBase.resolve(relative);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Internal error, query parameters not properly validated.", e);
            }
        }

        public Iterator<T> exec() throws IOException, InterruptedException {
            HttpRequest req = buildRequest();
            HttpResponse<InputStream> response = httpClient.send(req, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() != 200) {
                throw new HttpResponseException(response);
            }
            Charset contentCharset = checkMediaTypeAndDetermineCharset(response);
            try (InputStream body = response.body()) {
                InputStream b = body == null ? InputStream.nullInputStream() : body;
                return transform(b, contentCharset);
            }
        }

        /**
         * Build a request from the API base URI, operation path and
         * query parameters. Let the request accept JSON as return data.
         * @return The built request.
         */
        protected HttpRequest buildRequest() {
            return HttpRequest.newBuilder(buildURI())
                    .header("Accept", "application/json")
                    .build();
        }

        private Charset checkMediaTypeAndDetermineCharset(HttpResponse<?> response) {
            Optional<ContentType> contentType = ContentType.fromHttpResponse(response);
            Optional<String> mediaType = contentType.flatMap(ContentType::getMediaType);
            if (mediaType.isPresent() && ! mediaType.get().equalsIgnoreCase("application/json"))
                log.debug("Unexpected media type in response from {}: {}", response.uri(), mediaType.get());
            Charset contentCharset = contentType.flatMap(ContentType::getCharset).orElse(StandardCharsets.UTF_8);
            log.trace("Using charset {} for {}", contentCharset, response.uri());
            return contentCharset;
        }

        /**
         * Make objects out of the response body.
         * @param body Response body, presumably containing JSON data. The given stream is not {@code null}, but might be empty.
         * @param charset The character set fetched from the Content-type or a fall-back character set.
         * @return Objects decoded from the response body. The method shall not return {@code null}, instead return an empty iterator.
         * @throws IOException If the input stream fails, or decoding fails.
         */
        protected abstract Iterator<T> transform(InputStream body, Charset charset) throws IOException;
    }

    private class StationsHttpRequest extends Operation<Station> implements GetStationsOperation {
        public StationsHttpRequest() {
            super("stations");
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

        @Override
        protected Iterator<Station> transform(InputStream body, Charset charset) throws IOException {
            try (BufferedReader buf = new BufferedReader(new InputStreamReader(body, charset))) {
                log.trace("Input stream: {}", buf.lines().collect(Collectors.joining()));
            }
            return Collections.emptyIterator();
        }
    }
}
