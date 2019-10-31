import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private static final String BOARD = "" +
            "##ooo##\n" +
            "#ooooo#\n" +
            "ooooooo\n" +
            "ooo·ooo\n" +
            "ooooooo\n" +
            "#ooooo#\n" +
            "##ooo##";

    private final Board board = new Board(7, 7, BOARD);

    private final Position outOfBounds = new Position(4, 9);
    private final Position forbidden = new Position(0, 1);
    private final Position filled = new Position(2, 4);
    private final Position empty = new Position(3, 3);

    @Test
    void is_forbidden() {
        assertTrue(board.isForbidden(outOfBounds));
        assertTrue(board.isForbidden(forbidden));
        assertFalse(board.isForbidden(filled));
        assertFalse(board.isForbidden(empty));
    }

    @Test
    void is_filled() {
        assertFalse(board.isFilled(outOfBounds));
        assertFalse(board.isFilled(forbidden));
        assertTrue(board.isFilled(filled));
        assertFalse(board.isFilled(empty));
    }

    @Test
    void is_empty() {
        assertFalse(board.isEmpty(outOfBounds));
        assertFalse(board.isEmpty(forbidden));
        assertFalse(board.isEmpty(filled));
        assertTrue(board.isEmpty(empty));
    }

    @Test
    void fill_position() {
        board.fillPosition(empty);
        assertTrue(board.isFilled(empty));
    }

    @Test
    void empty_position() {
        board.emptyPosition(filled);
        assertTrue(board.isEmpty(filled));
    }

    @Test
    void roundtrip() {
        assertEquals(BOARD, board.toString());
    }
}