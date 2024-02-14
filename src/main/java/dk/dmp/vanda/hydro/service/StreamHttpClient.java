package dk.dmp.vanda.hydro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

/**
 * The implementation is immutable, thus thread-safe.
 */
public class StreamHttpClient {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final HttpClient httpClient;

    /**
     * Construct the client.
     * @param httpClient The client for sending HTTP requests.
     */
    public StreamHttpClient(HttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient);
    }

    public ExtendedHttpResponse<InputStream> submit(URI requestURL) throws IOException, InterruptedException {
        HttpRequest req = buildRequest(requestURL);
        ExtendedHttpResponse<InputStream> response = new ExtendedHttpResponse<>(httpClient.send(req, HttpResponse.BodyHandlers.ofInputStream()));
        if (response.statusCode() != 200) {
            throw new HttpResponseException(response);
        }
        checkMediaType(response);
        return response;
    }

    /**
     * Build a request from the request URL, operation path and
     * query parameters. Let the request accept JSON as return data.
     * @param requestURL The URL that the request shall be sent to.
     * @return The built request.
     */
    protected HttpRequest buildRequest(URI requestURL) {
        return HttpRequest.newBuilder(requestURL)
                .header("Accept", "application/json")
                .build();
    }

    /**
     * Verify that the response media type corresponds to the requested
     * media type.
     * @param response The response to validate.
     */
    protected void checkMediaType(ExtendedHttpResponse<?> response) {
        Optional<ContentType> contentType = response.contentType();
        Optional<String> mediaType = contentType.flatMap(ContentType::getMediaType);
        if (mediaType.isPresent() && ! mediaType.get().equalsIgnoreCase("application/json"))
            log.debug("Unexpected media type in response from {}: {}", response.uri(), mediaType.get());
    }
}
