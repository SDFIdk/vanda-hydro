package dk.dmp.vanda.hydro.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.InputStream;
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

class HydrometryHttpServiceOperationTest {
    HttpClient client = HttpClient.newHttpClient();

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
    void testRequestThrowsNullBody() throws IOException, InterruptedException, URISyntaxException {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k, v) -> true);
        HttpResponse<InputStream> response = mock(HttpResponse.class);
        when(response.headers()).thenReturn(headers);
        HttpClient client = mock(HttpClient.class);
        when(client.send(any(), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        Service service = new Service(new URI("http://localhost/api/"), client);
        Service.Op op = service.new Op("op");
        op.addQueryParameter("foo", "bar");
        assertThrows(HttpResponseException.class, () -> op.exec());
        ArgumentCaptor<HttpRequest> req = ArgumentCaptor.forClass(HttpRequest.class);
        verify(client).send(req.capture(), any());
        assertEquals(new URI("http://localhost/api/op?foo=bar"), req.getValue().uri());
        assertEquals("GET", req.getValue().method());
        assertEquals(Optional.of("application/json"), req.getValue().headers().firstValue("Accept"));
    }

    @Test
    void testRequestThrows() throws IOException, InterruptedException, URISyntaxException {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k, v) -> true);
        HttpResponse<InputStream> response = mock(HttpResponse.class);
        when(response.headers()).thenReturn(headers);
        when(response.body()).thenReturn(InputStream.nullInputStream());
        HttpClient client = mock(HttpClient.class);
        when(client.send(any(), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        Service service = new Service(new URI("http://localhost/api/"), client);
        Service.Op op = service.new Op("op");
        op.addQueryParameter("foo", "bar");
        assertThrows(HttpResponseException.class, () -> op.exec());
        ArgumentCaptor<HttpRequest> req = ArgumentCaptor.forClass(HttpRequest.class);
        verify(client).send(req.capture(), any());
        assertEquals(new URI("http://localhost/api/op?foo=bar"), req.getValue().uri());
        assertEquals("GET", req.getValue().method());
        assertEquals(Optional.of("application/json"), req.getValue().headers().firstValue("Accept"));
    }

    @Test
    void testRequest() throws IOException, InterruptedException, URISyntaxException {
        HttpHeaders headers = HttpHeaders.of(Map.of("Content-Type", Collections.singletonList("artificial/mediatype; charset=utf-8")), (k, v) -> true);
        HttpResponse<InputStream> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.headers()).thenReturn(headers);
        when(response.body()).thenReturn(InputStream.nullInputStream());
        HttpClient client = mock(HttpClient.class);
        when(client.send(any(), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        Service service = new Service(new URI("http://localhost/api/"), client);
        Service.Op op = service.new Op("op");
        op.addQueryParameter("foo", "bar");
        op.exec();
        ArgumentCaptor<HttpRequest> req = ArgumentCaptor.forClass(HttpRequest.class);
        verify(client).send(req.capture(), any());
        assertEquals(new URI("http://localhost/api/op?foo=bar"), req.getValue().uri());
        assertEquals("GET", req.getValue().method());
        assertEquals(Optional.of("application/json"), req.getValue().headers().firstValue("Accept"));
    }

    static class Service extends HydrometryHttpService {
        public Service(URI apiBase, HttpClient httpClient) {
            super(apiBase, httpClient);
        }

        class Op extends Operation<String> {
            public Op(String path) {
                super(path);
            }
            protected Iterator<String> transform(InputStream jsonData, Charset charset) {
                return null;
            }
        }
    }
}
