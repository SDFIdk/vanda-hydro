package dk.dmp.vanda.hydro.service;

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
        ContentType ct = new ContentType(null, null);
        assertEquals(Optional.empty(), ct.getMediaType());
        assertEquals(Optional.empty(), ct.getCharset());
    }

    @Test
    void getMediaType() {
        ContentType ct = new ContentType("text/html", Charset.forName("Latin1"));
        assertEquals(Optional.of("text/html"), ct.getMediaType());
    }

    @Test
    void getCharset() {
        ContentType ct = new ContentType("text/html", Charset.forName("Latin1"));
        assertEquals(Optional.of(StandardCharsets.ISO_8859_1), ct.getCharset());
    }

    @Test
    void fromHttpResponse() {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k,v) -> true);
        HttpResponse<?> response = mock(HttpResponse.class);
        when(response.headers()).thenReturn(headers);
        ContentType ct = ContentType.fromHttpResponse(response);
        assertEquals(Optional.of("artificial/mediatype"), ct.getMediaType());
        assertEquals(Optional.of(StandardCharsets.UTF_8), ct.getCharset());
    }

    @Test
    void fromHttpResponseMissing() {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k,v) -> false);
        HttpResponse<?> response = mock(HttpResponse.class);
        when(response.headers()).thenReturn(headers);
        ContentType ct = ContentType.fromHttpResponse(response);
        assertEquals(Optional.empty(), ct.getMediaType());
        assertEquals(Optional.empty(), ct.getCharset());
    }

    @Test
    void fromHttpResponseNoCharset1() {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; =utf-8")), (k,v) -> true);
        HttpResponse<?> response = mock(HttpResponse.class);
        when(response.headers()).thenReturn(headers);
        ContentType ct = ContentType.fromHttpResponse(response);
        assertEquals(Optional.of("artificial/mediatype"), ct.getMediaType());
        assertEquals(Optional.empty(), ct.getCharset());
    }

    @Test
    void fromHttpResponseNoCharset2() {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype")), (k,v) -> true);
        HttpResponse<?> response = mock(HttpResponse.class);
        when(response.headers()).thenReturn(headers);
        ContentType ct = ContentType.fromHttpResponse(response);
        assertEquals(Optional.of("artificial/mediatype"), ct.getMediaType());
        assertEquals(Optional.empty(), ct.getCharset());
    }
}