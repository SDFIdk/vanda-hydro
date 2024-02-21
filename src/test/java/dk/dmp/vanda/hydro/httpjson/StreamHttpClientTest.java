package dk.dmp.vanda.hydro.httpjson;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StreamHttpClientTest {
    @Mock HttpClient client;

    @Test
    void testConstructionFail() {
        assertThrows(NullPointerException.class, () -> new StreamHttpClient(null));
    }

    @Test
    void testRequest400ThrowsNullBody(@Mock HttpResponse<InputStream> response) throws IOException, InterruptedException {
        HttpHeaders headers = HttpHeaders.of(Collections.emptyMap(), (k,v) -> true);
        when(response.statusCode()).thenReturn(400);
        when(response.headers()).thenReturn(headers);
        when(response.body()).thenReturn(null);
        when(client.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<InputStream>>any())).thenReturn(response);
        StreamHttpClient service = new StreamHttpClient(client);
        assertThrows(HttpResponseException.class, () -> service.submit(new URI("http://localhost/api/op?foo=bar")));
    }

    @Test
    void testRequest400ThrowsSomeBody(@Mock HttpResponse<InputStream> response) throws IOException, InterruptedException {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k, v) -> true);
        when(response.statusCode()).thenReturn(400);
        when(response.headers()).thenReturn(headers);
        when(response.body()).thenReturn(InputStream.nullInputStream());
        when(client.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<InputStream>>any())).thenReturn(response);
        StreamHttpClient service = new StreamHttpClient(client);
        assertThrows(HttpResponseException.class, () -> service.submit(new URI("http://localhost/api/op?foo=bar")));
    }

    @Test
    void testRequestNullBody(@Mock HttpResponse<InputStream> response) throws IOException, InterruptedException, URISyntaxException {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k, v) -> true);
        when(response.statusCode()).thenReturn(200);
        when(response.headers()).thenReturn(headers);
        when(response.body()).thenReturn(null);
        when(client.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<InputStream>>any())).thenReturn(response);
        StreamHttpClient service = new StreamHttpClient(client);
        ExtendedHttpResponse<InputStream> received = service.submit(new URI("http://localhost/api/op?foo=bar"));
        ArgumentCaptor<HttpRequest> req = ArgumentCaptor.forClass(HttpRequest.class);
        verify(client).send(req.capture(), any());
        assertEquals(new URI("http://localhost/api/op?foo=bar"), req.getValue().uri());
        assertEquals("GET", req.getValue().method());
        assertEquals(Optional.of("application/json"), req.getValue().headers().firstValue("Accept"));
        try (InputStream is = received.body()) {
            assertNull(is);
        }
    }

    @Test
    void testRequest(@Mock HttpResponse<InputStream> response) throws IOException, InterruptedException, URISyntaxException {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k, v) -> true);
        when(response.statusCode()).thenReturn(200);
        when(response.headers()).thenReturn(headers);
        when(response.body()).thenReturn(InputStream.nullInputStream());
        when(client.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<InputStream>>any())).thenReturn(response);
        StreamHttpClient service = new StreamHttpClient(client);
        ExtendedHttpResponse<InputStream> received = service.submit(new URI("http://localhost/api/op?foo=bar"));
        ArgumentCaptor<HttpRequest> req = ArgumentCaptor.forClass(HttpRequest.class);
        verify(client).send(req.capture(), any());
        assertEquals(new URI("http://localhost/api/op?foo=bar"), req.getValue().uri());
        assertEquals("GET", req.getValue().method());
        assertEquals(Optional.of("application/json"), req.getValue().headers().firstValue("Accept"));
        try (InputStream is = received.body()) {
            assertEquals(-1, is.read());
        }
    }
}
