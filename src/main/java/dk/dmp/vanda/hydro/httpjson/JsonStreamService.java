package dk.dmp.vanda.hydro.httpjson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public interface JsonStreamService {
    /**
     * Submit a request for the given request path and query parameters.
     * @param requestPathAndQuery The operation that the request shall be sent to.
     * @return The response body.
     * @throws URISyntaxException If the path and query violates RFC 2396.
     * @throws IOException If an I/O error occurs when sending or receiving,
     * or the client has shut down.
     * @throws InterruptedException If the operation is interrupted.
     */
    InputStream submit(String requestPathAndQuery) throws IOException, InterruptedException, URISyntaxException;
}
