import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void up() {
        Position from = new Position(4, 3);
        assertEquals(new Position(4, 2), Direction.UP.apply(from));
    }

    @Test
    void right() {
        Position from = new Position(4, 3);
        assertEquals(new Position(5, 3), Direction.RIGHT.apply(from));
    }

    @Test
    void down() {
        Position from = new Position(4, 3);
        assertEquals(new Position(4, 4), Direction.DOWN.apply(from));
    }

    @Test
    void left() {
        Position from = new Position(4, 3);
        assertEquals(new Position(3, 3), Direction.LEFT.apply(from));
    }

}