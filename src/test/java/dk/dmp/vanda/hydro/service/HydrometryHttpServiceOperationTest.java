package dk.dmp.vanda.hydro.service;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.nio.charset.Charset;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

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
