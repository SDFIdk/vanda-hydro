package dk.dmp.vanda.hydro.httpjson;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Encode query parameters according to the convensions for encoding
 * HTTP form data.
 * @see <a href="https://www.w3.org/TR/2014/REC-html5-20141028/forms.html#url-encoded-form-data">HTML 5: 4.10.22.6 URL-encoded form data</a>
 */
public class OperationPathAndParameters {
    private String path;
    private final StringJoiner params = new StringJoiner("&");

    /**
     * Set and encode the target path.
     * @param path Absolute or relative path.
     * @throws IllegalArgumentException If the path is not parseable by
     * {@link URI#URI(String, String, String, String, String)}.
     */
    public void setPath(String path) {
        if (path != null) try {
            this.path = new URI(null, null, path, null, null).toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Path not accepted by URI constructor", e);
        } else  {
            this.path = null;
        }
    }

    /**
     * Add and encode query parameter.
     * @param parm Query parameter name.
     * @param value Query parameter value.
     */
    public void addQueryParameter(String parm, String value) {
        Objects.requireNonNull(parm, "parm cannot be null");
        StringBuilder b = new StringBuilder(urlEncode(parm));
        if (value != null) b.append("=").append(urlEncode(value));
        params.add(b.toString());
    }
    private static String urlEncode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

    /**
     * Build a query string according to the convensions for encoding
     * HTTP form data.
     * @return The query string, or {@code null} if the query is empty.
     */
    public String getQueryString() {
        return params.toString();
    }

    /**
     * Make a relative URL from the path and the query parameters.
     * @return The created relative URL.
     */
    public String toString() {
            String qs = getQueryString();
            return (path != null ? path : "") + (qs.isEmpty() ? "" : "?") + qs;
    }

    /**
     * Append the path and query to a given API base URL. This is not
     * the same as {@link URI#resolve(URI)}, since the whole base URL is
     * preserved in the result.
     * @param apiBase The URL on which to append the path and query.
     * @return The combined URL with base, path and query.
     * @throws IllegalArgumentException If the given URI is not a URL
     * @throws RuntimeException If the combined URL violates RFC 2396.
     */
    public URI appendToURL(URI apiBase) {
        if (apiBase.isOpaque())
            throw new IllegalArgumentException("apiBase is not a URL: " + apiBase);
        String base = apiBase.toString();
        if (base.endsWith("/")) base = base.substring(0, base.length() - 1);
        String pathAndQuery = toString();
        if (pathAndQuery.startsWith("/")) pathAndQuery = pathAndQuery.substring(1);
        try {
            return new URI(base + "/" + pathAndQuery);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Internal error. Relative path or query parameters not properly escaped.", e);
        }
    }
}
