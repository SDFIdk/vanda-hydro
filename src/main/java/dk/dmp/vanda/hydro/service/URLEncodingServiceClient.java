package dk.dmp.vanda.hydro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The implementation is immutable, thus thread-safe.
 * However, the operations builders are not.
 */
public class URLEncodingServiceClient {
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
    public URLEncodingServiceClient(URI apiBase, HttpClient httpClient) {
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

    /**
     * A mutable, non-thread-safe class for building the path and query
     * string and invoking the remote service operation.
     */
    public class Request {
        private final String opPath;
        private final List<Map.Entry<String, String>> params = new LinkedList<>();

        /**
         * Create the operation builder.
         * @param path Path to operation relative to service API base URI.
         */
        public Request(String path) {
            opPath = path;
        }

        /**
         * Add a query parameter.
         * @param parm Query parameter name.
         * @param value Query parameter value.
         */
        public void addQueryParameter(String parm, String value) {
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

        public ExtendedInputStreamHttpResponse submit() throws IOException, InterruptedException {
            HttpRequest req = buildRequest();
            ExtendedInputStreamHttpResponse response = new ExtendedInputStreamHttpResponse(httpClient.send(req, HttpResponse.BodyHandlers.ofInputStream()));
            if (response.statusCode() != 200) {
                throw new HttpResponseException(response);
            }
            checkMediaType(response);
            return response;
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

        private void checkMediaType(ExtendedInputStreamHttpResponse response) {
            Optional<ContentType> contentType = response.contentType();
            Optional<String> mediaType = contentType.flatMap(ContentType::getMediaType);
            if (mediaType.isPresent() && ! mediaType.get().equalsIgnoreCase("application/json"))
                log.debug("Unexpected media type in response from {}: {}", response.uri(), mediaType.get());
        }
    }
}
