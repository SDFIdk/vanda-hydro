package dk.dmp.vanda.hydro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.Optional;

class ContentType {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String mediaType;
    private final Charset charset;

    public ContentType(String mediaType, Charset charset) {
        this.mediaType = mediaType;
        this.charset = charset;
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

    public static ContentType fromHttpResponse(HttpResponse<?> response) {
        return fromHttpHeaders(response.headers());
    }

    public static ContentType fromHttpHeaders(HttpHeaders headers) {
        Optional<String> contentType = headers.firstValue("Content-Type");
        return contentType.map(ContentType::fromContentTypeHeaderValue)
                .orElseGet(() -> new ContentType(null, null));
    }

    public static ContentType fromContentTypeHeaderValue(String headerValue) {
        log.trace("Interpreting Content-Type value: {}", headerValue);
        String[] components = headerValue.split(";");
        String mediaType = components.length > 0 ? components[0].trim() : null;
        Charset charset = components.length > 1 ? fromCharsetPartOfContentTypeValue(components[1]) : null;
        ContentType result = new ContentType(mediaType, charset);
        log.trace("Decomposed Content-Type value: {}", result);
        return result;
    }

    private static Charset fromCharsetPartOfContentTypeValue(String part) {
        String[] charsetcomp = part.split("=");
        if (charsetcomp.length > 1 && charsetcomp[0].trim().equalsIgnoreCase("charset")) {
            String charsetText = charsetcomp[1].trim();
            return Charset.forName(charsetText);
        } else return null;
    }
}
