import acm.program.GraphicsProgram;

import java.awt.event.MouseEvent;

public class Senku extends GraphicsProgram {

    public static final int APPLICATION_WIDTH = 600;
    public static final int APPLICATION_HEIGHT = 600;

    private static final double OUTER = 0.05;
    private static final double INNER = 0.03;

    private static final int ROWS = 7;
    private static final int COLUMNS = 7;
    private static final String BOARD = "" +
            "##ooo##\n" +
            "#ooooo#\n" +
            "ooooooo\n" +
            "ooo·ooo\n" +
            "ooooooo\n" +
            "#ooooo#\n" +
            "##ooo##";

    private Geometry geometry;
    private Display display;
    private Board board;
    private Game game;

    private Position selectedFrom;

    private int movesCounter;

    @Override
    public void mouseMoved(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        if (selectedFrom == null) {
            highlightOrClearFrom(x, y);
        } else {
            highlightOrClearTo(x, y);
        }
    }

    private void highlightOrClearFrom(double x, double y) {
        Position position = geometry.xyToCell(x, y);
        if (game.isValidFrom(position)) {
            display.highlight(x, y);
        } else {
            display.clearHighlight();
        }
    }

    private void highlightOrClearTo(double x, double y) {
        Position position = geometry.xyToCell(x, y);
        if (game.isValidTo(selectedFrom, position)) {
            display.highlight(x, y);
        } else if (!position.equals(selectedFrom)) {
            display.clearHighlight();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        Position position = geometry.xyToCell(x, y);
        if (selectedFrom == null) {
            selectIfValid(x, y);
        } else if (position.equals(selectedFrom)) {
            unselect();
        } else if (game.isValidTo(selectedFrom, position)) {
            move(position);
        }
    }

    private void selectIfValid(double x, double y) {
        Position position = geometry.xyToCell(x, y);
        if (game.isValidFrom(position)) {
            selectedFrom = position;
            display.select(x, y);
            updateTitle(game.countValidMovesTo(selectedFrom));
        }
    }

    private void unselect() {
        selectedFrom = null;
        display.clearSelect();
        updateTitle(game.countValidMovesFrom());
    }

    private void move(Position position) {
        Position middle = game.move(selectedFrom, position);
        display.setEmpty(selectedFrom);
        selectedFrom = null;
        display.setEmpty(middle);
        display.setFilled(position);
        movesCounter += 1;
        updateTitle(game.countValidMovesFrom());
    }

    private void updateTitle(int validMoves) {
        setTitle(Display.getStatusMessage(validMoves, movesCounter));
    }

    @Override
    public void run() {
        addMouseListeners();
        runGame();
    }

    private void runGame() {
        geometry = new Geometry(APPLICATION_WIDTH, APPLICATION_HEIGHT, COLUMNS, ROWS, OUTER, INNER);
        display = new Display(geometry, BOARD, getGCanvas());
        board = new Board(COLUMNS, ROWS, BOARD);
        game = new Game(board);
        display.initializeDisplay();
        movesCounter = 0;
        updateTitle(game.countValidMovesFrom());
    }

    public static void main(String[] args) {
        new Senku().start(args);
    }

}
