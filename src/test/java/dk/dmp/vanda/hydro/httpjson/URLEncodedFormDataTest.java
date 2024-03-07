package dk.dmp.vanda.hydro.httpjson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class URLEncodedFormDataTest {
    @Test
    void testEmpty() {
        URLEncodedFormData form = new URLEncodedFormData();
        assertEquals("", form.toString(), "Relative");
    }

    @Test
    void testPath() {
        URLEncodedFormData form = new URLEncodedFormData();
        form.setPath("op");
        assertEquals("op", form.toString(), "Relative");
    }

    @Test
    void testIllegalPath() {
        URLEncodedFormData form = new URLEncodedFormData();
        assertThrows(IllegalArgumentException.class, () -> form.setPath(":"));
    }

    @Test
    void testIllegalParameter() {
        URLEncodedFormData form = new URLEncodedFormData();
        assertThrows(NullPointerException.class, () -> form.append(null, null));
        assertThrows(NullPointerException.class, () -> form.append(null, "bar"));
    }

    @Test
    void testQuery() {
        URLEncodedFormData form = new URLEncodedFormData();
        form.setPath("op");
        form.append("foo", "bar");
        assertEquals("op?foo=bar", form.toString(), "Relative");
    }

    @Test
    void testStrangeQueries() {
        URLEncodedFormData form = new URLEncodedFormData();
        form.setPath("þ¤? #");
        form.append("foo", null);
        form.append("cr&zy", "$tr@n?€/ {sy=bo|~}");
        form.append("dimmer", "flop");
        assertAll(
                () -> assertEquals("foo&cr%26zy=%24tr%40n%3F%E2%82%AC%2F%20%7Bsy%3Dbo%7C%7E%7D&dimmer=flop", form.getFormData(), "Query string"),
                () -> assertEquals("þ¤%3F%20%23?foo&cr%26zy=%24tr%40n%3F%E2%82%AC%2F%20%7Bsy%3Dbo%7C%7E%7D&dimmer=flop", form.toString(), "Relative")
        );
    }
}