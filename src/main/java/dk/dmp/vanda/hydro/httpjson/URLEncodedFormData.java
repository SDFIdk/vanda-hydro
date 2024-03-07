package dk.dmp.vanda.hydro.httpjson;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Encode parameters according to the convensions for encoding
 * HTML form data.
 * @see <a href="https://www.w3.org/TR/2014/REC-html5-20141028/forms.html#url-encoded-form-data">HTML 5: 4.10.22.6 URL-encoded form data</a>
 */
public class URLEncodedFormData {
    private String path;
    private final StringJoiner params = new StringJoiner("&");

    /**
     * Set and encode the target path, a.k.a. relative action URL.
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

    public String getPath() {
        return path;
    }

    /**
     * Append parameter.
     * @param name Parameter name.
     * @param value Parameter value.
     */
    public void append(String name, String value) {
        Objects.requireNonNull(name, "Parameter name cannot be null");
        StringBuilder b = new StringBuilder(urlEncode(name));
        if (value != null) b.append("=").append(urlEncode(value));
        params.add(b.toString());
    }
    private static String urlEncode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

    /**
     * Build a form data string according to the convensions for encoding
     * HTTP form data.
     * @return The form data string, or {@code null} if no parameters are appended.
     */
    public String getFormData() {
        return params.toString();
    }

    /**
     * Make a relative URL with path and query.
     * @return The created relative URL.
     */
    public String toString() {
        String q = getFormData();
        return (path != null ? path : "") + (q.isEmpty() ? "" : "?") + q;
    }
}
