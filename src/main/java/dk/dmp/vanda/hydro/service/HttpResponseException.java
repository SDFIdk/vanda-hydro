package dk.dmp.vanda.hydro.service;

import java.io.*;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpResponseException extends IOException {
    private final HttpResponse<InputStream> response;
    public HttpResponseException(HttpResponse<InputStream> response) {
        super(String.format("status code: %d, message: %s", response.statusCode(), fetchBody(response)));
        this.response = response;
    }

    private static String fetchBody(HttpResponse<InputStream> response) {
        Optional<ContentType> contentType = ContentType.fromHttpResponse(response);
        Charset contentCharset = contentType.flatMap(ContentType::getCharset).orElse(StandardCharsets.UTF_8);
        try (InputStream responseStream = response.body()) {
            if (responseStream == null)
                return "(no response body)";
            try (BufferedReader buf = new BufferedReader(new InputStreamReader(responseStream, contentCharset))) {
                return buf.lines().collect(Collectors.joining());
            }
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
