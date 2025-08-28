package mimi;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    void parseDeadline_validIsoDate_returnsParts() throws MiMiException {
        String[] parts = Parser.parseDeadline("return book /by 2019-10-15");
        assertArrayEquals(new String[]{"return book", "2019-10-15"}, parts);
    }

    @Test
    void parseDeadline_missingBy_throws() {
        MiMiException ex = assertThrows(MiMiException.class,
                () -> Parser.parseDeadline("return book"));
        assertTrue(ex.getMessage().toLowerCase().contains("/by"));
    }

    @Test
    void parseIndex_valid1Based_returns0Based() throws MiMiException {
        assertEquals(0, Parser.parseIndex("1"));
        assertEquals(9, Parser.parseIndex("10"));
    }

    @Test
    void parseIndex_zeroOrNegative_throws() {
        assertThrows(MiMiException.class, () -> Parser.parseIndex("0"));
        assertThrows(MiMiException.class, () -> Parser.parseIndex("-1"));
    }
}
