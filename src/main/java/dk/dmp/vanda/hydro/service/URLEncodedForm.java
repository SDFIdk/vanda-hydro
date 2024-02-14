package dk.dmp.vanda.hydro.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Encode query parameters according to the convensions for encoding
 * HTTP form data.
 * @see <a href="https://www.w3.org/TR/2014/REC-html5-20141028/forms.html#url-encoded-form-data">HTML 5: 4.10.22.6 URL-encoded form data</a>
 */
public class URLEncodedForm {
    private URI url = URI.create("");
    private final List<Map.Entry<String, String>> params = new LinkedList<>();

    /**
     * Resolve a URL relative to the current URL and store in current
     * URL.
     * @param url Absolute or relative URL.
     */
    public void resolve(URI url) {
        this.url = this.url.resolve(url);
    }

    /**
     * Resolve a URL string relative to the current URL and store in
     * current URL.
     * @param url Absolute or relative encoded URL.
     */
    public void resolve(String url) {
        this.url = this.url.resolve(url);
    }

    /**
     * Resolve a path string relative to the current URL and store in
     * current URL.
     * @param path Absolute or relative path, not URL encoded.
     * @throws URISyntaxException If the URL cannot be constructed.
     */
    public void path(String path) throws URISyntaxException {
        resolve(new URI(null, null, path, null));
    }

    /**
     * Add a query parameter.
     * @param parm Query parameter name.
     * @param value Query parameter value.
     */
    public void addQueryParameter(String parm, String value) {
        params.add(Map.entry(parm, value));
    }

    /**
     * Build a query string according to the convensions for encoding
     * HTTP form data.
     * @return The query string, or {@code null} if the query is empty.
     */
    public String buildQueryString() {
        String q = params.stream()
                .map(e ->
                        URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8)
                        + "=" +
                        URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8)
                )
                .collect(Collectors.joining("&"));
        return q.isEmpty() ? null : q;
    }

    /**
     * Make a URL from the stored URL and the query parameters.
     * @return The created URL.
     */
    public URI buildURL() {
        try {
            String qs = buildQueryString();
            URI pq = new URI(url.getRawPath() + (qs != null ? "?" + qs : ""));
            return url.resolve(pq);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Internal error. Relative path or query parameters not properly escaped.", e);
        }
    }
}
