import acm.graphics.*;

import java.awt.*;
import java.util.StringTokenizer;

public class Display {

    private final String board;
    private final GCanvas gCanvas;
    private final Geometry geometry;

    private GObject highlighted;
    private GObject selected;

    public Display(Geometry geometry, String board, GCanvas gCanvas) {
        this.board = board;
        this.gCanvas = gCanvas;
        this.geometry = geometry;
    }

    public void initializeDisplay() {
        gCanvas.setBackground(Palette.BACKGROUND);
        // We assume the string corresponds to a valid map of given dimensions
        StringTokenizer st = new StringTokenizer(board, "\n");
        for (int y = 0; y < geometry.getRows(); y++) {
            initializeRow(y, st.nextToken());
        }
    }

    private void initializeRow(int y, String row) {
        for (int x = 0; x < geometry.getColumns(); x++) {
            initializePosition(row.charAt(x), new Position(x, y));
        }
    }

    private void initializePosition(char status, Position pos) {
        if (status == 'o') {
            paintFilled(pos);
        } else if (status == '·') {
            paintEmpty(pos);
        }
    }

    private void paintEmpty(Position pos) {
        GOval oval = createOval(pos);
        oval.setColor(Palette.EMPTY);
        gCanvas.add(oval);
    }

    private void paintFilled(Position pos) {
        GOval oval = createOval(pos);
        oval.setColor(Palette.FILLED);
        gCanvas.add(oval);
    }

    private GOval createOval(Position pos) {
        GDimension dimension = geometry.tokenDimension();
        GPoint topLeft = geometry.tokenTopLeft(pos.getX(), pos.getY());
        GOval oval = createOval(topLeft, dimension);
        oval.setFilled(true);
        return oval;
    }

    private GOval createOval(GPoint topLeft, GDimension dimension) {
        return new GOval(
                topLeft.getX(),
                topLeft.getY(),
                dimension.getWidth(),
                dimension.getHeight());
    }

    public void highlight(double x, double y) {
        GObject gObject = gCanvas.getElementAt(x, y);
        if (ignoreHighlight(gObject)) return;
        if (gObject != highlighted) clearHighlight();
        highlighted = gObject;
        Color highlightColor = Palette.highlightColor(gObject.getColor());
        highlighted.setColor(highlightColor);
    }

    private boolean ignoreHighlight(GObject gObject) {
        return !(gObject instanceof GOval) || gObject == highlighted;
    }

    public void clearHighlight() {
        if (highlighted == null) return;
        Color normalColor = Palette.normalColor(highlighted.getColor());
        highlighted.setColor(normalColor);
        highlighted = null;
    }

    public void select(double x, double y) {
        GObject gObject = gCanvas.getElementAt(x, y);
        if (ignoreSelection(gObject)) return;
        selected = gObject;
        highlighted = null;
        selected.setColor(Palette.SELECTED_FILLED);
    }

    private boolean ignoreSelection(GObject gObject) {
        return !(gObject instanceof GOval);
    }

    public void clearSelect() {
        if (selected == null) return;
        selected.setColor(Palette.FILLED);
        selected = null;
    }

    public void setEmpty(Position position) {
        setColor(position, Palette.EMPTY);
    }

    public void setFilled(Position position) {
        setColor(position, Palette.FILLED);
    }

    private void setColor(Position position, Color color) {
        GPoint center = geometry.centerAt(position.getX(), position.getY());
        GObject gObject = gCanvas.getElementAt(center);
        if (gObject == selected) selected = null;
        if (gObject == highlighted) highlighted = null;
        gObject.setColor(color);
    }

    public static String getStatusMessage(int validMoves, int movesCounter) {
        return validMovesMessage(validMoves) + " / " + movesCounterMessage(movesCounter);
    }

    private static String validMovesMessage(int validMoves) {
        if (validMoves == 0) {
            return "No hi ha cap moviment vàlid. GAME OVER.";
        } else if (validMoves == 1) {
            return "Hi ha un moviment vàlid";
        } else {
            return String.format("Hi ha %d moviments vàlids", validMoves);
        }
    }

    private static String movesCounterMessage(int movesCounter) {
        if (movesCounter == 0) {
            return "No has fet cap moviment";
        } else if (movesCounter == 1) {
            return "Has fet un moviment";
        } else {
            return String.format("Has fet %d moviments", movesCounter);
        }
    }

}

