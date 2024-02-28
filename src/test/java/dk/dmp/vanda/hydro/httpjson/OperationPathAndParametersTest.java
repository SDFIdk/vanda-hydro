package dk.dmp.vanda.hydro.httpjson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperationPathAndParametersTest {
    @Test
    void testEmpty() {
        OperationPathAndParameters form = new OperationPathAndParameters();
        assertEquals("", form.toString(), "Relative");
    }

    @Test
    void testPath() {
        OperationPathAndParameters form = new OperationPathAndParameters();
        form.setPath("op");
        assertEquals("op", form.toString(), "Relative");
    }

    @Test
    void testIllegalPath() {
        OperationPathAndParameters form = new OperationPathAndParameters();
        assertThrows(IllegalArgumentException.class, () -> form.setPath(":"));
    }

    @Test
    void testIllegalParameter() {
        OperationPathAndParameters form = new OperationPathAndParameters();
        assertThrows(NullPointerException.class, () -> form.addQueryParameter(null, null));
        assertThrows(NullPointerException.class, () -> form.addQueryParameter(null, "bar"));
    }

    @Test
    void testQuery() {
        OperationPathAndParameters form = new OperationPathAndParameters();
        form.setPath("op");
        form.addQueryParameter("foo", "bar");
        assertEquals("op?foo=bar", form.toString(), "Relative");
    }

    @Test
    void testStrangeQueries() {
        OperationPathAndParameters form = new OperationPathAndParameters();
        form.setPath("þ¤? #");
        form.addQueryParameter("foo", null);
        form.addQueryParameter("cr&zy", "$tr@n?€/ {sy=bo|~}");
        form.addQueryParameter("dimmer", "flop");
        assertAll(
                () -> assertEquals("foo&cr%26zy=%24tr%40n%3F%E2%82%AC%2F%20%7Bsy%3Dbo%7C%7E%7D&dimmer=flop", form.getQueryString(), "Query string"),
                () -> assertEquals("þ¤%3F%20%23?foo&cr%26zy=%24tr%40n%3F%E2%82%AC%2F%20%7Bsy%3Dbo%7C%7E%7D&dimmer=flop", form.toString(), "Relative")
        );
    }
}