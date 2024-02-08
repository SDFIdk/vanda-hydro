package dk.dmp.vanda.hydro.service;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class URIBuilderTest {
    @Test
    void testConstructionFail() {
        assertThrows(NullPointerException.class, () -> new URIBuilder(null, null));
        assertThrows(NullPointerException.class, () -> new URIBuilder(null, "op"));
    }

    @Test
    void testEmpty() throws URISyntaxException {
        URIBuilder test = new URIBuilder(URI.create(""), null);
        URI built = test.buildURI();
        assertEquals(URI.create(""), built);
    }

    @Test
    void testNoBasePath() throws URISyntaxException {
        URIBuilder test = new URIBuilder(URI.create("http://localhost"), "op");
        URI built = test.buildURI();
        assertEquals(URI.create("http://localhost/op"), built);
    }

    @Test
    void testBaseNotDir() throws URISyntaxException {
        URIBuilder test = new URIBuilder(URI.create("http://localhost/api"), "op");
        URI built = test.buildURI();
        assertEquals(URI.create("http://localhost/op"), built);
    }

    @Test
    void testBaseDir() throws URISyntaxException {
        URIBuilder test = new URIBuilder(URI.create("http://localhost/api/"), "op");
        URI built = test.buildURI();
        assertEquals(URI.create("http://localhost/api/op"), built);
    }

    @Test
    void testQuery() throws URISyntaxException {
        URIBuilder test = new URIBuilder(URI.create("http://localhost/api/"), "op");
        test.addQueryParameter("foo", "bar");
        URI built = test.buildURI();
        assertEquals(URI.create("http://localhost/api/op?foo=bar"), built);
    }

    @Test
    void testQueries() throws URISyntaxException {
        URIBuilder test = new URIBuilder(URI.create("http://localhost/api/"), "op");
        test.addQueryParameter("foo", "bar");
        test.addQueryParameter("crazy", "$tr@n?€/{symbo|~}");
        test.addQueryParameter("dimmer", "flop");
        URI built = test.buildURI();
        assertEquals(new URI("http://localhost/api/op?foo=bar&crazy=$tr@n?€/%7Bsymbo%7C~%7D&dimmer=flop"), built);
    }
}
