package dk.dmp.vanda.hydro.service;

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
class URLEncodingServiceClientTest {
    @Mock HttpClient client;

    @Test
    void testConstructionFail() {
        assertThrows(NullPointerException.class, () -> new URLEncodingServiceClient(null, client));
        assertThrows(IllegalArgumentException.class, () -> new URLEncodingServiceClient(new URI("mailto:itd-drift@sdfi.dk"), client));
    }

    @Test
    void testEmpty() throws URISyntaxException {
        URLEncodingServiceClient service = new URLEncodingServiceClient(new URI(""), client);
        URLEncodingServiceClient.Request op = service.new Request(null);
        assertEquals(new URI(""), op.buildURI());
    }

    @Test
    void testNoBasePath() throws URISyntaxException {
        URLEncodingServiceClient service = new URLEncodingServiceClient(new URI("http://localhost"), client);
        URLEncodingServiceClient.Request op = service.new Request("op");
        assertEquals(new URI("http://localhost/op"), op.buildURI());
    }

    @Test
    void testBaseNotDir() throws URISyntaxException {
        URLEncodingServiceClient service = new URLEncodingServiceClient(new URI("http://localhost/api"), client);
        URLEncodingServiceClient.Request op = service.new Request("op");
        assertEquals(new URI("http://localhost/op"), op.buildURI());
    }

    @Test
    void testBaseDir() throws URISyntaxException {
        URLEncodingServiceClient service = new URLEncodingServiceClient(new URI("http://localhost/api/"), client);
        URLEncodingServiceClient.Request op = service.new Request("op");
        assertEquals(new URI("http://localhost/api/op"), op.buildURI());
    }

    @Test
    void testQuery() throws URISyntaxException {
        URLEncodingServiceClient service = new URLEncodingServiceClient(new URI("http://localhost/api/"), client);
        URLEncodingServiceClient.Request op = service.new Request("op");
        op.addQueryParameter("foo", "bar");
        assertEquals(new URI("http://localhost/api/op?foo=bar"), op.buildURI());
    }

    @Test
    void testQueries() throws URISyntaxException {
        URLEncodingServiceClient service = new URLEncodingServiceClient(new URI("http://localhost/api/"), client);
        URLEncodingServiceClient.Request op = service.new Request("op");
        op.addQueryParameter("foo", "bar");
        op.addQueryParameter("crazy", "$tr@n?€/{symbo|~}");
        op.addQueryParameter("dimmer", "flop");
        assertEquals(new URI("http://localhost/api/op?foo=bar&crazy=$tr@n?€/%7Bsymbo%7C~%7D&dimmer=flop"), op.buildURI());
    }

    @Test
    void testRequestThrowsNullBody(@Mock HttpResponse<InputStream> response) throws IOException, InterruptedException, URISyntaxException {
        HttpHeaders headers = HttpHeaders.of(Collections.emptyMap(), (k,v) -> true);
        when(response.statusCode()).thenReturn(400);
        when(response.headers()).thenReturn(headers);
        when(response.body()).thenReturn(null);
        when(client.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<InputStream>>any())).thenReturn(response);
        URLEncodingServiceClient service = new URLEncodingServiceClient(new URI("http://localhost/api/"), client);
        URLEncodingServiceClient.Request op = service.new Request("op");
        op.addQueryParameter("foo", "bar");
        assertThrows(HttpResponseException.class, op::submit);
    }

    @Test
    void testRequestThrowsSomeBody(@Mock HttpResponse<InputStream> response) throws IOException, InterruptedException, URISyntaxException {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k, v) -> true);
        when(response.statusCode()).thenReturn(400);
        when(response.headers()).thenReturn(headers);
        when(response.body()).thenReturn(InputStream.nullInputStream());
        when(client.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<InputStream>>any())).thenReturn(response);
        URLEncodingServiceClient service = new URLEncodingServiceClient(new URI("http://localhost/api/"), client);
        URLEncodingServiceClient.Request op = service.new Request("op");
        op.addQueryParameter("foo", "bar");
        assertThrows(HttpResponseException.class, op::submit);
    }

    @Test
    void testRequestNullBody(@Mock HttpResponse<InputStream> response) throws IOException, InterruptedException, URISyntaxException {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k, v) -> true);
        when(response.statusCode()).thenReturn(200);
        when(response.headers()).thenReturn(headers);
        when(response.body()).thenReturn(null);
        when(client.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<InputStream>>any())).thenReturn(response);
        URLEncodingServiceClient service = new URLEncodingServiceClient(new URI("http://localhost/api/"), client);
        URLEncodingServiceClient.Request op = service.new Request("op");
        op.addQueryParameter("foo", "bar");
        ExtendedInputStreamHttpResponse received = op.submit();
        ArgumentCaptor<HttpRequest> req = ArgumentCaptor.forClass(HttpRequest.class);
        verify(client).send(req.capture(), any());
        assertEquals(new URI("http://localhost/api/op?foo=bar"), req.getValue().uri());
        assertEquals("GET", req.getValue().method());
        assertEquals(Optional.of("application/json"), req.getValue().headers().firstValue("Accept"));
        try (InputStream is = received.body()) {
            assertEquals(-1, is.read());
        }
    }

    @Test
    void testRequest(@Mock HttpResponse<InputStream> response) throws IOException, InterruptedException, URISyntaxException {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k, v) -> true);
        when(response.statusCode()).thenReturn(200);
        when(response.headers()).thenReturn(headers);
        when(response.body()).thenReturn(InputStream.nullInputStream());
        when(client.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<InputStream>>any())).thenReturn(response);
        URLEncodingServiceClient service = new URLEncodingServiceClient(new URI("http://localhost/api/"), client);
        URLEncodingServiceClient.Request op = service.new Request("op");
        op.addQueryParameter("foo", "bar");
        ExtendedInputStreamHttpResponse received = op.submit();
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
