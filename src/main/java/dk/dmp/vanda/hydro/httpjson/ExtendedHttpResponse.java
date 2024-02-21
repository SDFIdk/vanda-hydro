package dk.dmp.vanda.hydro.httpjson;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Extends a {@link HttpResponse} with interpretation of the Content-Type
 * header.
 * @param <T> The type of response body.
 */
public class ExtendedHttpResponse<T> implements HttpResponse<T> {
    private final HttpResponse<T> httpResponse;
    private final Optional<ContentType> contentType;

    public ExtendedHttpResponse(HttpResponse<T> httpResponse) {
        this.httpResponse = httpResponse;
        contentType = ContentType.fromHttpResponse(httpResponse);
    }

    /**
     * Fetch interpretation of Content-type header.
     * @return The interpreted Content-type header, if found.
     */
    public Optional<ContentType> contentType() {
        return contentType;
    }

    /**
     * Fetch the character set from the Content-type header.
     * If not found or not supported, fall back to UTF-8.
     * @return Found or fall-back character set.
     */
    public Charset determineCharset() {
        return contentType.flatMap(ContentType::getCharset).orElse(StandardCharsets.UTF_8);
    }

    @Override
    public int statusCode() {
        return httpResponse.statusCode();
    }

    @Override
    public HttpRequest request() {
        return httpResponse.request();
    }

    @Override
    public Optional<HttpResponse<T>> previousResponse() {
        return httpResponse.previousResponse();
    }

    @Override
    public HttpHeaders headers() {
        return httpResponse.headers();
    }

    @Override
    public T body() {
        return httpResponse.body();
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return httpResponse.sslSession();
    }

    @Override
    public URI uri() {
        return httpResponse.uri();
    }

    @Override
    public HttpClient.Version version() {
        return httpResponse.version();
    }
}
