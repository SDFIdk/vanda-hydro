package dk.dmp.vanda.hydro.httpjson;

import java.io.*;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

public class HttpResponseException extends IOException {
    private final int statusCode;
    private final URI uri;
    private final HttpHeaders headers;

    public HttpResponseException(ExtendedHttpResponse<InputStream> response) {
        super(String.format("status code: %d, message: %s", response.statusCode(), fetchBody(response)));
        statusCode = response.statusCode();
        uri = response.uri();
        headers = response.headers();
    }

    private static String fetchBody(ExtendedHttpResponse<InputStream> response) {
        Charset contentCharset = response.determineCharset();
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
        return statusCode;
    }

    public URI uri() {
        return uri;
    }

    public HttpHeaders headers() {
        return headers;
    }
}
