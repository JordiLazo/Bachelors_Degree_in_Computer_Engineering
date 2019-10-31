import java.util.StringTokenizer;

// Only a rectangle of cells. Do not know hame rules.

public class Board {

    private final int width;
    private final int height;
    private final Cell[][] cells;

    public Board(int width, int height, String board) {
        this.width=width;
        this.height=height;
        cells= new Cell[height][width];

        StringTokenizer token = new StringTokenizer(board,"\n");
        String table;

        for(int i=0; i<width;i++){
                table=token.nextToken();
            for(int j=0;j<height;j++){
                cells[i][j]= Cell.fromChar(table.charAt(j));
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isForbidden(Position pos) {
        if(pos.getY()>getHeight() || pos.getX()>getWidth() || pos.getY()<0 || pos.getX()<0 || cells[pos.getY()][pos.getX()].isForbidden() ){
            return true;
        }else {
            return false;
        }
    }

    public boolean isFilled(Position pos) {
        if(pos.getY()<getHeight() && pos.getX()<getWidth() && pos.getY()>=0 && pos.getX()>=0 && cells[pos.getY()][pos.getX()].isFilled() ){
            return true;
        }else {
            return false;
        }
    }

    public boolean isEmpty(Position pos) {
        if(pos.getY()<getHeight() && pos.getX()<getWidth() && pos.getY()>=0 && pos.getX()>=0 && cells[pos.getY()][pos.getX()].isEmpty() ){
            return true;
        }else {
            return false;
        }
    }

    public void fillPosition(Position pos) {
        if(!isForbidden(pos)){
            cells[pos.getY()][pos.getX()]= Cell.FILLED;
        }
    }

    public void emptyPosition(Position pos) {
        if(!isForbidden(pos)){
            cells[pos.getY()][pos.getX()]= Cell.EMPTY;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(cells[y][x].toString());
            }
            if (y != height - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
