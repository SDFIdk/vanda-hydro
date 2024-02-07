package dk.dmp.vanda.hydro.service;

import java.io.*;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class HttpResponseException extends IOException {
    private final HttpResponse<InputStream> response;
    public HttpResponseException(HttpResponse<InputStream> response) {
        super(String.format("Status code: %d. Message: %s.", response.statusCode(), fetchBody(response)));
        this.response = response;
    }

    private static String fetchBody(HttpResponse<InputStream> response) {
        ContentType contentType = ContentType.fromHttpResponse(response);
        Charset contentCharset = contentType.getCharset().orElse(StandardCharsets.UTF_8);
        try (Reader reader = new InputStreamReader(response.body(), contentCharset); BufferedReader buf = new BufferedReader(reader)) {
            return buf.lines().collect(Collectors.joining());
        } catch (IOException e) {
            return String.format("(at attempt to retrieve response body: %s)", e);
        }
    }

    public int statusCode() {
        return response.statusCode();
    }

    public URI uri() {
        return response.uri();
    }

    public HttpHeaders headers() {
        return response.headers();
    }
}
