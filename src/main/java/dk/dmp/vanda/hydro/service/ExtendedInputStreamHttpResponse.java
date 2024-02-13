package dk.dmp.vanda.hydro.service;

import javax.net.ssl.SSLSession;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class ExtendedInputStreamHttpResponse implements HttpResponse<InputStream> {
    private final HttpResponse<InputStream> inputStreamHttpResponse;
    private final Optional<ContentType> contentType;

    public ExtendedInputStreamHttpResponse(HttpResponse<InputStream> inputStreamHttpResponse) {
        this.inputStreamHttpResponse = inputStreamHttpResponse;
        contentType = ContentType.fromHttpResponse(inputStreamHttpResponse);
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
    public Charset assumedCharset() {
        return contentType.flatMap(ContentType::getCharset).orElse(StandardCharsets.UTF_8);
    }

    @Override
    public int statusCode() {
        return inputStreamHttpResponse.statusCode();
    }

    @Override
    public HttpRequest request() {
        return inputStreamHttpResponse.request();
    }

    @Override
    public Optional<HttpResponse<InputStream>> previousResponse() {
        return inputStreamHttpResponse.previousResponse();
    }

    @Override
    public HttpHeaders headers() {
        return inputStreamHttpResponse.headers();
    }

    /**
     * <p>Never returns {@code null}. But the stream may be empty.</p>
     * {@inheritDoc}
     */
    @Override
    public InputStream body() {
        InputStream body = inputStreamHttpResponse.body();
        return body == null ? InputStream.nullInputStream() : body;
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return inputStreamHttpResponse.sslSession();
    }

    @Override
    public URI uri() {
        return inputStreamHttpResponse.uri();
    }

    @Override
    public HttpClient.Version version() {
        return inputStreamHttpResponse.version();
    }
}
