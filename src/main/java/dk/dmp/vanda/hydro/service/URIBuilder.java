package dk.dmp.vanda.hydro.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A mutable, non-thread-safe class for building the path and query string.
 */
class URIBuilder {
    private final URI apiBase;
    private final String path;
    private final List<Map.Entry<String, String>> params = new LinkedList<>();

    /**
     * Create a query builder.
     *
     * @param apiBase Base URL of API.
     * @param path    Relative path to operation.
     */
    public URIBuilder(URI apiBase, String path) {
        this.apiBase = Objects.requireNonNull(apiBase, "apiBase missing");
        this.path = path;
    }

    /**
     * Add a query parameter.
     *
     * @param parm  Query parameter name.
     * @param value Query parameter value.
     */
    public void addQueryParameter(String parm, String value) {
        params.add(Map.entry(parm, value));
    }

    /**
     * Make a URI with path and query from this builder.
     *
     * @return The created URI.
     * @throws URISyntaxException if the URI string constructed from the given components violates RFC 2396.
     */
    public URI buildRelativeURI() throws URISyntaxException {
        String q = params.stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        if (q.isEmpty()) q = null;
        return new URI(null, null, path, q, null);
    }

    public URI buildURI() throws URISyntaxException {
        return apiBase.resolve(buildRelativeURI());
    }

    public String toString() {
        return String.format("%s {apiBase: %s, path: %s, params: %s}", getClass().getName(), apiBase, path, params);
    }
}
