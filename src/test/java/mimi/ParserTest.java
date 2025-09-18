package mimi;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    void parseDeadline_validIsoDate_returnsParts() throws MiMiException {
        String[] parts = Parser.parseDeadline("return book /by 2019-10-15");
        assertArrayEquals(new String[]{"return book", "2019-10-15"}, parts);
    }

    @Test
    void parseDeadline_missingBy_throws() {
        MiMiException ex = assertThrows(MiMiException.class, () -> Parser.parseDeadline("return book"));
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

    @Test
    void parseTodo_blank_throws() {
        assertThrows(MiMiException.class, () -> Parser.parseTodo("   "));
    }

    @Test
    void parseEvent_fromOnlyOrFromTo_ok() throws MiMiException {
        assertArrayEquals(new String[]{"meet", "1400", ""}, Parser.parseEvent("meet /from 1400"));
        assertArrayEquals(new String[]{"meet", "1400", "1600"}, Parser.parseEvent("meet /from 1400 /to 1600"));
    }

    @Test
    void parseWithin_missingFromOrTo_throws() {
        assertThrows(MiMiException.class, () -> Parser.parseWithin("collect cert /from 2025-01-01"));
        assertThrows(MiMiException.class, () -> Parser.parseWithin("collect cert /to 2025-01-10"));
    }


}
