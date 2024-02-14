package dk.dmp.vanda.hydro.service;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class URLEncodedFormTest {
    @Test
    void testEmpty() throws URISyntaxException {
        URLEncodedForm form = new URLEncodedForm();
        assertEquals(new URI(""), form.buildURL());
    }

    @Test
    void testNoBasePath() throws URISyntaxException {
        URLEncodedForm form = new URLEncodedForm();
        form.resolve("http://localhost");
        form.resolve("op");
        assertEquals(new URI("http://localhost/op"), form.buildURL());
    }

    @Test
    void testBaseNotDir() throws URISyntaxException {
        URLEncodedForm form = new URLEncodedForm();
        form.resolve("http://localhost/api");
        form.path("op");
        assertEquals(new URI("http://localhost/op"), form.buildURL());
    }

    @Test
    void testBaseDir() throws URISyntaxException {
        URLEncodedForm form = new URLEncodedForm();
        form.resolve("http://localhost/api/");
        form.path("op");
        assertEquals(new URI("http://localhost/api/op"), form.buildURL());
    }

    @Test
    void testQuery() throws URISyntaxException {
        URLEncodedForm form = new URLEncodedForm();
        form.resolve("http://localhost/api/");
        form.path("op");
        form.addQueryParameter("foo", "bar");
        assertEquals(new URI("http://localhost/api/op?foo=bar"), form.buildURL());
    }

    @Test
    void testQueries() throws URISyntaxException {
        URLEncodedForm form = new URLEncodedForm();
        form.resolve("http://localhost/api/");
        form.path("þ¤?#");
        form.addQueryParameter("foo", "bar");
        form.addQueryParameter("cr&zy", "$tr@n?€/{sy=bo|~}");
        form.addQueryParameter("dimmer", "flop");
        assertEquals("foo=bar&cr%26zy=%24tr%40n%3F%E2%82%AC%2F%7Bsy%3Dbo%7C%7E%7D&dimmer=flop", form.buildQueryString());
        assertEquals(new URI("http://localhost/api/þ¤%3F%23?foo=bar&cr%26zy=%24tr%40n%3F%E2%82%AC%2F%7Bsy%3Dbo%7C%7E%7D&dimmer=flop"), form.buildURL());
    }
}