package dk.dmp.vanda.hydro.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HydrometryHttpServiceOperationTest {
    @Mock HttpClient client;

    @Test
    void testConstructionFail() {
        assertThrows(NullPointerException.class, () -> new Service(null, client));
        assertThrows(IllegalArgumentException.class, () -> new Service(new URI("mailto:itd-drift@sdfi.dk"), client));
    }

    @Test
    void testEmpty() throws URISyntaxException {
        Service service = new Service(new URI(""), client);
        Service.Op op = service.new Op(null);
        assertEquals(new URI(""), op.buildURI());
    }

    @Test
    void testNoBasePath() throws URISyntaxException {
        Service service = new Service(new URI("http://localhost"), client);
        Service.Op op = service.new Op("op");
        assertEquals(new URI("http://localhost/op"), op.buildURI());
    }

    @Test
    void testBaseNotDir() throws URISyntaxException {
        Service service = new Service(new URI("http://localhost/api"), client);
        Service.Op op = service.new Op("op");
        assertEquals(new URI("http://localhost/op"), op.buildURI());
    }

    @Test
    void testBaseDir() throws URISyntaxException {
        Service service = new Service(new URI("http://localhost/api/"), client);
        Service.Op op = service.new Op("op");
        assertEquals(new URI("http://localhost/api/op"), op.buildURI());
    }

    @Test
    void testQuery() throws URISyntaxException {
        Service service = new Service(new URI("http://localhost/api/"), client);
        Service.Op op = service.new Op("op");
        op.addQueryParameter("foo", "bar");
        assertEquals(new URI("http://localhost/api/op?foo=bar"), op.buildURI());
    }

    @Test
    void testQueries() throws URISyntaxException {
        Service service = new Service(new URI("http://localhost/api/"), client);
        Service.Op op = service.new Op("op");
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
        Service service = new Service(new URI("http://localhost/api/"), client);
        Service.Op op = service.new Op("op");
        op.addQueryParameter("foo", "bar");
        assertThrows(HttpResponseException.class, op::exec);
    }

    @Test
    void testRequestThrowsSomeBody(@Mock HttpResponse<InputStream> response) throws IOException, InterruptedException, URISyntaxException {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k, v) -> true);
        when(response.statusCode()).thenReturn(400);
        when(response.headers()).thenReturn(headers);
        when(response.body()).thenReturn(InputStream.nullInputStream());
        when(client.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<InputStream>>any())).thenReturn(response);
        Service service = new Service(new URI("http://localhost/api/"), client);
        Service.Op op = service.new Op("op");
        op.addQueryParameter("foo", "bar");
        assertThrows(HttpResponseException.class, op::exec);
    }

    @Test
    void testRequestNullBody(@Mock HttpResponse<InputStream> response) throws IOException, InterruptedException, URISyntaxException {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k, v) -> true);
        when(response.statusCode()).thenReturn(200);
        when(response.headers()).thenReturn(headers);
        when(response.body()).thenReturn(null);
        when(client.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<InputStream>>any())).thenReturn(response);
        Service service = new Service(new URI("http://localhost/api/"), client);
        Service.Op op = service.new Op("op");
        op.addQueryParameter("foo", "bar");
        Iterator<?> iter = op.exec();
        ArgumentCaptor<HttpRequest> req = ArgumentCaptor.forClass(HttpRequest.class);
        verify(client).send(req.capture(), any());
        assertEquals(new URI("http://localhost/api/op?foo=bar"), req.getValue().uri());
        assertEquals("GET", req.getValue().method());
        assertEquals(Optional.of("application/json"), req.getValue().headers().firstValue("Accept"));
        assertFalse(iter.hasNext());
    }

    @Test
    void testRequest(@Mock HttpResponse<InputStream> response) throws IOException, InterruptedException, URISyntaxException {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k, v) -> true);
        when(response.statusCode()).thenReturn(200);
        when(response.headers()).thenReturn(headers);
        when(response.body()).thenReturn(InputStream.nullInputStream());
        when(client.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<InputStream>>any())).thenReturn(response);
        Service service = new Service(new URI("http://localhost/api/"), client);
        Service.Op op = service.new Op("op");
        op.addQueryParameter("foo", "bar");
        Iterator<?> iter = op.exec();
        ArgumentCaptor<HttpRequest> req = ArgumentCaptor.forClass(HttpRequest.class);
        verify(client).send(req.capture(), any());
        assertEquals(new URI("http://localhost/api/op?foo=bar"), req.getValue().uri());
        assertEquals("GET", req.getValue().method());
        assertEquals(Optional.of("application/json"), req.getValue().headers().firstValue("Accept"));
        assertFalse(iter.hasNext());
    }

    static class Service extends HydrometryHttpService {
        public Service(URI apiBase, HttpClient httpClient) {
            super(apiBase, httpClient);
        }

        class Op extends Operation<String> {
            public Op(String path) {
                super(path);
            }
            protected Iterator<String> transform(InputStream body, Charset charset) throws IOException {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(body, charset))) {
                    return reader.lines().toList().iterator();
                }
            }
        }
    }
}
