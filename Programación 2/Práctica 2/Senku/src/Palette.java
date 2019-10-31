import java.awt.*;

public class Palette {

    public static final Color BACKGROUND = new Color(155, 78, 44); // 100

    public static final Color EMPTY = new Color(135, 68, 43); // 120
    public static final Color HIGHLIGHT_EMPTY = new Color(115, 55, 43); // 140

    public static final Color FILLED = new Color(120, 138, 155); // 100
    public static final Color HIGHLIGHT_FILLED = new Color(100, 118, 135); // 120
    public static final Color SELECTED_FILLED = new Color(80, 98, 115); // 140

    private static boolean isFilled(Color color) {
        return color == FILLED || color == HIGHLIGHT_FILLED || color == SELECTED_FILLED;
    }

    public static Color highlightColor(Color color) {
        return isFilled(color) ? HIGHLIGHT_FILLED : HIGHLIGHT_EMPTY;
    }

    public static Color normalColor(Color color) {
        return isFilled(color) ? FILLED : EMPTY;
    }
}
