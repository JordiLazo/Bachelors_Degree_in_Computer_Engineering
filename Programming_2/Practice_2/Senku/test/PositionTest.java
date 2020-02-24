import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void getter_and_constructor() {
        Position pos = new Position(4, 3);
        assertEquals(4, pos.getX());
        assertEquals(3, pos.getY());
    }

    @Test
    void colinear_horizontal() {
        Position pos1 = new Position(3, 8);
        Position pos2 = new Position(6, 8);
        assertTrue(pos1.colinear(pos2));
        assertTrue(pos2.colinear(pos1));
    }

    @Test
    void colinear_vertical() {
        Position pos1 = new Position(6, 4);
        Position pos2 = new Position(6, 8);
        assertTrue(pos1.colinear(pos2));
        assertTrue(pos2.colinear(pos1));
    }

    @Test
    void not_colinear() {
        Position pos1 = new Position(3, 8);
        Position pos2 = new Position(6, 4);
        assertFalse(pos1.colinear(pos2));
        assertFalse(pos2.colinear(pos1));
    }

    @Test
    void distance() {
        Position pos1 = new Position(3, -2);
        Position pos2 = new Position(6, 4);
        assertEquals(9, pos1.distance(pos2));
        assertEquals(9, pos2.distance(pos1));
    }

    @Test
    void middle() {
        Position pos1 = new Position(3, -2);
        Position pos2 = new Position(6, 4);
        assertEquals(new Position(4, 1), pos1.middle(pos2));
        assertEquals(new Position(4, 1), pos2.middle(pos1));
    }
}