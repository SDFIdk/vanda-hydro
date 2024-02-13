package dk.dmp.vanda.hydro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.Optional;

public class ContentType {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String mediaType;
    private final Charset charset;

    public ContentType(String headerValue) {
        String[] components = headerValue.split(";");
        mediaType = components[0].transform(s -> s.isBlank() ? null : s.trim());
        charset = charsetFromContentTypeValueParameters(components);
    }

    private static Charset charsetFromContentTypeValueParameters(String[] parameters) {
        Optional<String> value = Arrays.stream(parameters)
                .skip(1) // First parameter is the media-type
                .map(p -> p.split("=", 2))
                .filter(p -> p[0].trim().equalsIgnoreCase("charset"))
                .map(p -> p[1].trim())
                .findAny();
        try {
            return value.map(Charset::forName)
                    .orElse(null);
        } catch(IllegalCharsetNameException | UnsupportedCharsetException e) {
            log.debug("Cannot determine charset for \"{}\".", value.get(), e);
            return null;
        }
    }

    public Optional<String> getMediaType() {
        return Optional.ofNullable(mediaType);
    }

    public Optional<Charset> getCharset() {
        return Optional.ofNullable(charset);
    }

    public String toString() {
        return String.format("ContentType {mediaType: %s, charset: %s}", mediaType, charset);
    }

    public static Optional<ContentType> fromHttpResponse(HttpResponse<?> response) {
        return fromHttpHeaders(response.headers());
    }

    public static Optional<ContentType> fromHttpHeaders(HttpHeaders headers) {
        return headers.firstValue("Content-Type").map(ContentType::new);
    }
}
