package dk.dmp.vanda.hydro.httpjson;

import java.io.IOException;
import java.io.InputStream;

public interface StreamService {
    /**
     * Submit a request for the given request path and query parameters.
     * @param path The URL-encoded operation path that the request shall be sent to.
     * @param query The URL-encoded query.
     * @return The response body.
     * @throws IllegalArgumentException If the path and query violates RFC 2396.
     * @throws IOException If an I/O error occurs when sending or receiving, or the client has shut down.
     * @throws InterruptedException If the operation is interrupted.
     */
    InputStream get(String path, String query) throws IOException, InterruptedException, IllegalArgumentException;
}
