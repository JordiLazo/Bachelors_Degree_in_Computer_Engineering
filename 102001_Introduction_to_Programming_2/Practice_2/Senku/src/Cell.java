public class Cell {

    private static final char C_FORBIDDEN = '#';
    private static final char C_FILLED = 'o';
    private static final char C_EMPTY = '·';

    public static final Cell FORBIDDEN = new Cell(C_FORBIDDEN);
    public static final Cell FILLED = new Cell(C_FILLED);
    public static final Cell EMPTY = new Cell(C_EMPTY);
    private final char status;

    private Cell(char status) {
        this.status=status;
    }

    public static Cell fromChar(char status) {
        if(status==C_EMPTY || status==C_FILLED || status==C_FORBIDDEN){
            return new Cell(status);
        }else {
            return null;
        }
    }

    public boolean isForbidden() {
        if(C_FORBIDDEN==status){
            return true;
        }else {
            return false;
        }
    }

    public boolean isFilled() {
        if(C_FILLED==status){
            return true;
        }else {
            return false;
        }
    }

    public boolean isEmpty() {
        if(C_EMPTY==status){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(status);
    }
}
