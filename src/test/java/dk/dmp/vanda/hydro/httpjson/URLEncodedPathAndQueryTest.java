package dk.dmp.vanda.hydro.httpjson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class URLEncodedPathAndQueryTest {
    @Test
    void testEmpty() {
        URLEncodedPathAndQuery form = new URLEncodedPathAndQuery();
        assertEquals("", form.toString(), "Relative");
    }

    @Test
    void testPath() {
        URLEncodedPathAndQuery form = new URLEncodedPathAndQuery();
        form.setPath("op");
        assertEquals("op", form.toString(), "Relative");
    }

    @Test
    void testIllegalPath() {
        URLEncodedPathAndQuery form = new URLEncodedPathAndQuery();
        assertThrows(IllegalArgumentException.class, () -> form.setPath(":"));
    }

    @Test
    void testIllegalParameter() {
        URLEncodedPathAndQuery form = new URLEncodedPathAndQuery();
        assertThrows(NullPointerException.class, () -> form.append(null, null));
        assertThrows(NullPointerException.class, () -> form.append(null, "bar"));
    }

    @Test
    void testQuery() {
        URLEncodedPathAndQuery form = new URLEncodedPathAndQuery();
        form.setPath("op");
        form.append("foo", "bar");
        assertEquals("op?foo=bar", form.toString(), "Relative");
    }

    @Test
    void testStrangeQueries() {
        URLEncodedPathAndQuery form = new URLEncodedPathAndQuery();
        form.setPath("þ¤? #");
        form.append("foo", null);
        form.append("cr&zy", "$tr@n?€/ {sy=bo|~}");
        form.append("dimmer", "flop");
        assertAll(
                () -> assertEquals("foo&cr%26zy=%24tr%40n%3F%E2%82%AC%2F%20%7Bsy%3Dbo%7C%7E%7D&dimmer=flop", form.getQuery(), "Query string"),
                () -> assertEquals("þ¤%3F%20%23?foo&cr%26zy=%24tr%40n%3F%E2%82%AC%2F%20%7Bsy%3Dbo%7C%7E%7D&dimmer=flop", form.toString(), "Relative")
        );
    }
}