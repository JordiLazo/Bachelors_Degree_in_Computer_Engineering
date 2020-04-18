import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private static final String BOARD = "" +
            "##ooo##\n" +
            "#ooooo#\n" +
            "ooooooo\n" +
            "o·o·ooo\n" +
            "ooooooo\n" +
            "#o·ooo#\n" +
            "##ooo##";

    private final Board board = new Board(7, 7, BOARD);
    private final Game game = new Game(board);

    private final int[][] validFromXY = new int[][]{{1, 1}, {3, 1}, {2, 3}, {5, 3}, {1, 5}, {3, 5}, {4, 5}};
    private final int[] validToCount = new int[] {1, 1, 1, 1, 1, 1, 1};

    private boolean inValidFromXY(int x, int y) {
        for (int[] xy : validFromXY) {
            if (xy[0] == x && xy[1] == y) {
                return true;
            }
        }
        return false;
    }

    @Test
    void valid_from() {
        for (int i = 0; i < validFromXY.length; i++) {
            int[] xy = validFromXY[i];
            Position valid = new Position(xy[0], xy[1]);
            assertTrue(game.isValidFrom(valid));
            assertEquals(validToCount[i], game.countValidMovesTo(valid));
        }
        assertEquals(validFromXY.length, game.countValidMovesFrom());


    }

    @Test
    void not_is_valid_from() {
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                if (!inValidFromXY(x, y)) {
                    assertFalse(game.isValidFrom(new Position(x, y)));
                }
            }
        }
    }

    @Test
    void is_valid_to() {
        assertTrue(game.isValidTo(new Position(1, 1), new Position(1, 3)));
        assertTrue(game.isValidTo(new Position(3, 1), new Position(3, 3)));
        assertTrue(game.isValidTo(new Position(2, 3), new Position(2, 5)));
        assertTrue(game.isValidTo(new Position(5, 3), new Position(3, 3)));
        assertTrue(game.isValidTo(new Position(1, 5), new Position(1, 3)));
        assertTrue(game.isValidTo(new Position(3, 5), new Position(3, 3)));
        assertTrue(game.isValidTo(new Position(4, 5), new Position(2, 5)));
    }

    @Test
    void not_is_valid_to() {
        assertFalse(game.isValidTo(new Position(1, 1), new Position(1, -1)));
        assertFalse(game.isValidTo(new Position(1, 1), new Position(3, 1)));
        assertFalse(game.isValidTo(new Position(1, 1), new Position(1, 4)));
        assertFalse(game.isValidTo(new Position(1, 1), new Position(2, 2)));
    }

    @Test
    void has_valid_moves() {
        assertTrue(game.hasValidMovesFrom());
    }

    @Test
    void one_move() {
        String EXPECTED = "" +
                "##ooo##\n" +
                "#·oooo#\n" +
                "o·ooooo\n" +
                "ooo·ooo\n" +
                "ooooooo\n" +
                "#o·ooo#\n" +
                "##ooo##";
        Position middle = game.move(new Position(1, 1), new Position(1, 3));
        assertEquals(new Position(1, 2), middle);
        assertEquals(EXPECTED, board.toString());
    }

    @Test
    void two_moves() {
        String EXPECTED = "" +
                "##ooo##\n" +
                "#·oooo#\n" +
                "o·ooooo\n" +
                "o··oooo\n" +
                "ooooooo\n" +
                "#o·ooo#\n" +
                "##ooo##";
        game.move(new Position(1, 1), new Position(1, 3));
        Position middle = game.move(new Position(1, 3), new Position(3, 3));
        assertEquals(new Position(2, 3), middle);
        assertEquals(EXPECTED, board.toString());
    }

    @Test
    void three_moves() {
        String EXPECTED = "" +
                "##ooo##\n" +
                "#·oooo#\n" +
                "o·ooooo\n" +
                "o··oooo\n" +
                "ooooooo\n" +
                "#oo··o#\n" +
                "##ooo##";
        game.move(new Position(1, 1), new Position(1, 3));
        game.move(new Position(1, 3), new Position(3, 3));
        Position middle = game.move(new Position(4, 5), new Position(2, 5));
        assertEquals(new Position(3, 5), middle);
        assertEquals(EXPECTED, board.toString());
    }

    @Test
    void no_more_moves() {
        String EXPECTED = "" +
                "##··o##\n" +
                "#o····#\n" +
                "····o·o\n" +
                "··o····\n" +
                "o······\n" +
                "#····o#\n" +
                "##ooo##";
        game.move(new Position(1, 1), new Position(1, 3));
        game.move(new Position(1, 3), new Position(3, 3));
        game.move(new Position(4, 5), new Position(2, 5));
        game.move(new Position(3, 1), new Position(1, 1));
        game.move(new Position(3, 3), new Position(3, 1));
        game.move(new Position(3, 0), new Position(3, 2));
        game.move(new Position(2, 5), new Position(2, 3));
        game.move(new Position(2, 3), new Position(2, 1));
        game.move(new Position(2, 0), new Position(2, 2));
        game.move(new Position(3, 2), new Position(1, 2));
        game.move(new Position(1, 5), new Position(1, 3));
        game.move(new Position(0, 2), new Position(2, 2));
        game.move(new Position(5, 1), new Position(3, 1));
        game.move(new Position(4, 4), new Position(2, 4));
        game.move(new Position(0, 3), new Position(2, 3));
        game.move(new Position(2, 3), new Position(2, 1));
        game.move(new Position(5, 2), new Position(3, 2));
        game.move(new Position(3, 1), new Position(3, 3));
        game.move(new Position(4, 3), new Position(2, 3));
        game.move(new Position(2, 4), new Position(2, 2));
        game.move(new Position(2, 1), new Position(2, 3));
        game.move(new Position(5, 4), new Position(5, 2));
        game.move(new Position(6, 2), new Position(4, 2));
        game.move(new Position(6, 4), new Position(6, 2));
        assertEquals(EXPECTED, board.toString());
        assertFalse(game.hasValidMovesFrom());
        assertEquals(0, game.countValidMovesFrom());
    }
}