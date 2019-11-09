import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void forbidden_cell() {
        Cell forbidden = Cell.FORBIDDEN;
        assertTrue(forbidden.isForbidden());
        assertFalse(forbidden.isEmpty());
        assertFalse(forbidden.isFilled());
    }

    @Test
    void empty_cell() {
        Cell empty = Cell.EMPTY;
        assertTrue(empty.isEmpty());
        assertFalse(empty.isForbidden());
        assertFalse(empty.isFilled());
    }

    @Test
    void filled_cell() {
        Cell filled = Cell.FILLED;
        assertTrue(filled.isFilled());
        assertFalse(filled.isForbidden());
        assertFalse(filled.isEmpty());
    }

    @Test
    void cell_from_char() {
        Cell forbidden = Cell.fromChar('#');
        assertTrue(forbidden.isForbidden());
        Cell empty = Cell.fromChar('·');
        assertTrue(empty.isEmpty());
        Cell filled = Cell.fromChar('o');
        assertTrue(filled.isFilled());
        Cell invalid = Cell.fromChar('?');
        assertNull(invalid);
    }

    @Test
    void cell_to_string() {
        assertEquals("#", Cell.FORBIDDEN.toString());
        assertEquals("o", Cell.FILLED.toString());
        assertEquals("·", Cell.EMPTY.toString());
    }
}