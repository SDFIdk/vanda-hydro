package dk.dmp.vanda.hydro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
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
        Optional<String> contentType = response.headers().firstValue("Content-Type");
        log.trace("Interpreting Content-Type header content: {}", contentType);
        String mediaType = null;
        Charset charset = null;
        if (contentType.isPresent()) {
            String[] components = contentType.get().split(";");
            if (components.length > 0) mediaType = components[0].trim();
            if (components.length > 1) {
                String[] charsetcomp = components[1].split("=");
                if (charsetcomp.length > 1 && charsetcomp[0].trim().equalsIgnoreCase("charset")) {
                    String charsetText = charsetcomp[1].trim();
                    charset = Charset.forName(charsetText);
                }
            }
        }
        ContentType result = new ContentType(mediaType, charset);
        log.trace("Decomposed Content-Type header: {}", result);
        return result;
    }
}
