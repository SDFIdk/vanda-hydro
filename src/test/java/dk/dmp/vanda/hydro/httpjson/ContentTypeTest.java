package dk.dmp.vanda.hydro.httpjson;

import org.junit.jupiter.api.Test;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContentTypeTest {
    @Test
    void constructNull() {
        ContentType ct = new ContentType("");
        assertEquals(Optional.empty(), ct.getMediaType());
        assertEquals(Optional.empty(), ct.getCharset());
    }

    @Test
    void fromContentTypeHeaderValue() {
        String headerValue = "application/json; charset=utf-8";
        ContentType ct = new ContentType(headerValue);
        assertEquals(Optional.of("application/json"), ct.getMediaType());
        assertEquals(Optional.of(StandardCharsets.UTF_8), ct.getCharset());
    }

    @Test
    void fromContentTypeHeaderValueFirst() {
        String headerValue = " text/html ; charset = Latin1 ; boundary=flup";
        ContentType ct = new ContentType(headerValue);
        assertEquals(Optional.of("text/html"), ct.getMediaType());
        assertEquals(Optional.of(StandardCharsets.ISO_8859_1), ct.getCharset());
    }

    @Test
    void fromContentTypeHeaderValueLast() {
        String headerValue = "text/html; boundary=flup; charset=Latin2";
        ContentType ct = new ContentType(headerValue);
        assertEquals(Optional.of("text/html"), ct.getMediaType());
        assertEquals(Optional.of(Charset.forName("Latin2")), ct.getCharset());
    }

    @Test
    void fromContentTypeHeaderValueEmpty() {
        String headerValue = "text/html; charset=";
        ContentType ct = new ContentType(headerValue);
        assertEquals(Optional.of("text/html"), ct.getMediaType());
        assertEquals(Optional.empty(), ct.getCharset());
    }

    @Test
    void fromContentTypeHeaderValueIllegal() {
        String headerValue = "text/html; charset=gonøf";
        ContentType ct = new ContentType(headerValue);
        assertEquals(Optional.of("text/html"), ct.getMediaType());
        assertEquals(Optional.empty(), ct.getCharset());
    }

    @Test
    void fromContentTypeHeaderValueUnknown() {
        String headerValue = "text/html; charset=artificial";
        ContentType ct = new ContentType(headerValue);
        assertEquals(Optional.of("text/html"), ct.getMediaType());
        assertEquals(Optional.empty(), ct.getCharset());
    }

    @Test
    void fromHttpResponse() {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k,v) -> true);
        HttpResponse<?> response = mock(HttpResponse.class);
        when(response.headers()).thenReturn(headers);
        ContentType ct = ContentType.fromHttpResponse(response).get();
        assertEquals(Optional.of("artificial/mediatype"), ct.getMediaType());
        assertEquals(Optional.of(StandardCharsets.UTF_8), ct.getCharset());
    }

    @Test
    void fromHttpResponseMissing() {
        HttpHeaders headers = HttpHeaders.of(Collections.emptyMap(), (k,v) -> true);
        HttpResponse<?> response = mock(HttpResponse.class);
        when(response.headers()).thenReturn(headers);
        Optional<ContentType> ct = ContentType.fromHttpResponse(response);
        assertEquals(Optional.empty(), ct);
    }

    @Test
    void fromHttpResponseNoCharset1() {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; =utf-8")), (k,v) -> true);
        HttpResponse<?> response = mock(HttpResponse.class);
        when(response.headers()).thenReturn(headers);
        ContentType ct = ContentType.fromHttpResponse(response).get();
        assertEquals(Optional.of("artificial/mediatype"), ct.getMediaType());
        assertEquals(Optional.empty(), ct.getCharset());
    }

    @Test
    void fromHttpResponseNoCharset2() {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype")), (k,v) -> true);
        HttpResponse<?> response = mock(HttpResponse.class);
        when(response.headers()).thenReturn(headers);
        ContentType ct = ContentType.fromHttpResponse(response).get();
        assertEquals(Optional.of("artificial/mediatype"), ct.getMediaType());
        assertEquals(Optional.empty(), ct.getCharset());
    }
}