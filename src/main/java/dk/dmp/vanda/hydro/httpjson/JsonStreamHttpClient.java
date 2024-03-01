package dk.dmp.vanda.hydro.httpjson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * A {@link StreamService} that interacts with a remote endpoint over
 * HTTP and expects JSON response content.
 * The implementation is immutable, thus thread-safe.
 */
public class JsonStreamHttpClient implements StreamService {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final HttpClient httpClient;
    private final URI apiBase;

    /**
     * Construct the service client.
     * @param apiBase Base URL of VanDa Hydro service, e.g.
     *         {@code https://vandah.miljoeportal.dk/api/},
     *         {@code https://vandah.test.miljoeportal.dk/api/} or
     *         {@code https://vandah.demo.miljoeportal.dk/api/}
     * @param httpClient The client for sending HTTP requests.
     * @throws IllegalArgumentException If the API base URI is not a URL
     * or includes query or fragment components.
     */
    public JsonStreamHttpClient(URI apiBase, HttpClient httpClient) throws IllegalArgumentException {
        this.httpClient = Objects.requireNonNull(httpClient);
        if (apiBase.getPath() == null) {
            throw new IllegalArgumentException(String.format("API base URL has no path component: %s", apiBase));
        }
        if (apiBase.getQuery() != null) {
            throw new IllegalArgumentException(String.format("API base URL should not have a query component: %s", apiBase));
        }
        if (apiBase.getFragment() != null) {
            throw new IllegalArgumentException(String.format("API base URL should not have a fragment component: %s", apiBase));
        }
        if (! apiBase.getPath().endsWith("/")) {
            log.warn("API base URL does not end with '/': {}", apiBase);
        } else if (! apiBase.getPath().equals("/api/")) {
            log.debug("Given API base URL does not end with '/api/': {}", apiBase);
        }
        this.apiBase = apiBase;
    }

    /**
     * Submit a GET request built from the API base URL and the given
     * request path and query parameters. This is not the same as
     * {@link URI#resolve(URI)}, since the whole base URL is preserved
     * in the result.
     * @param path The URL-encoded operation path that the request shall be sent to.
     * @param query The URL-encoded query.
     * @return The response body.
     * @throws IllegalArgumentException If the combined URL violates RFC 2396.
     * @throws IOException If an I/O error occurs when sending or receiving, or the client has shut down.
     * @throws InterruptedException If the operation is interrupted.
     * @throws HttpResponseException If the response is not success.
     */
    @Override
    public InputStream get(String path, String query)
        throws IOException, InterruptedException, IllegalArgumentException
    {
        HttpRequest req = buildRequest(path, query);
        ExtendedHttpResponse<InputStream> response =
            new ExtendedHttpResponse<>(httpClient.send(
                req, HttpResponse.BodyHandlers.ofInputStream()
            ));
        if (response.statusCode() != 200) {
            throw new HttpResponseException(response);
        }
        checkContentType(response);
        return response.body();
    }

    /**
     * Build a request from the API base URL, operation path and
     * query parameters. This is not the same as {@link URI#resolve(URI)},
     * since the whole base URL is preserved in the result.
     * <p>Let the request accept JSON as return data.</p>
     * @param path The URL-encoded request path.
     * @param query The URL-encoded request query.
     * @return The built request.
     * @throws IllegalArgumentException If the combined URL violates RFC 2396.
     */
    protected HttpRequest buildRequest(String path, String query)
        throws IllegalArgumentException
    {
        if (path == null) path = "";
        if (query == null || query.isEmpty()) query = "";
        else query = "?" + query;
        URI requestURL = URI.create(apiBase.toString() + path + query);
        return HttpRequest.newBuilder(requestURL)
                .header("Accept", "application/json")
                .build();
    }

    /**
     * Verify that the response content type corresponds to the expected
     * content type for JSON.
     * @param response The response to validate.
     */
    protected void checkContentType(ExtendedHttpResponse<?> response) {
        Optional<ContentType> contentType = response.contentType();
        Optional<String> mediaType = contentType.flatMap(ContentType::getMediaType);
        if (mediaType.isPresent() && ! mediaType.get().equalsIgnoreCase("application/json"))
            log.debug("Unexpected media type in response from {}: {}", response.uri(), mediaType.get());
        Optional<Charset> charset = contentType.flatMap(ContentType::getCharset);
        if (charset.isPresent() && ! charset.get().equals(StandardCharsets.UTF_8))
            log.debug("Unexpected charset in in response from {}: {}", response.uri(), charset.get());
    }
}
