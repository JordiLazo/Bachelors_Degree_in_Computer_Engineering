import acm.graphics.GDimension;
import acm.graphics.GPoint;

public class Geometry {

    private final int windowWidth;
    private final int windowHeight;
    private final int numCols;
    private final int numRows;
    private final double boardPadding;
    private final double cellPadding;

    public Geometry(int windowWidth, int windowHeight, int numCols, int numRows, double boardPadding, double cellPadding) {
        this.windowHeight=windowHeight;
        this.windowWidth=windowWidth;
        this.numCols=numCols;
        this.numRows=numRows;
        this.boardPadding=boardPadding;
        this.cellPadding=cellPadding;
    }

    public int getRows() {
        return numRows;
    }

    public int getColumns() {
        return numCols;
    }

    public GDimension boardDimension() {
        double xboard=windowWidth-2*(windowWidth*boardPadding);
        double yboard=windowHeight-2*(windowHeight*boardPadding);
        return new GDimension(xboard,yboard);
    }

    public GPoint boardTopLeft() {
        double xboardtopleft=windowWidth*boardPadding;
        double yboardtopleft=windowHeight*boardPadding;
        return new GPoint(xboardtopleft,yboardtopleft);
    }

    public GDimension cellDimension() {
        double xcell=boardDimension().getWidth()/getColumns();
        double ycell=boardDimension().getHeight()/getRows();
        return new GDimension(xcell,ycell);
    }

    public GPoint cellTopLeft(int x, int y) {
        double xcelltopleft=boardTopLeft().getX()+cellDimension().getWidth()*x;
        double ycelltopleft=boardTopLeft().getY()+cellDimension().getHeight()*y;
        return new GPoint(xcelltopleft,ycelltopleft);
    }

    public GDimension tokenDimension() {
        double xoval=cellDimension().getWidth()-2*(cellDimension().getWidth()*cellPadding);
        double yoval=cellDimension().getHeight()-2*(cellDimension().getHeight()*cellPadding);
        return new GDimension(xoval,yoval);
    }

    public GPoint tokenTopLeft(int x, int y) {
        double xovaltopleft=cellTopLeft(x,y).getX()+cellDimension().getWidth()*cellPadding;
        double yovaltopleft=cellTopLeft(x,y).getY()+cellDimension().getHeight()*cellPadding;
        return new GPoint(xovaltopleft,yovaltopleft);
    }

    public GPoint centerAt(int x, int y) {
        double xcellmiddle=cellTopLeft(x,y).getX()+cellDimension().getWidth()/2;
        double ycellmiddle=cellTopLeft(x,y).getY()+cellDimension().getHeight()/2;
        return new GPoint(xcellmiddle,ycellmiddle);
    }

    public Position xyToCell(double x, double y) {
        GPoint boardTopLeft = boardTopLeft();
        GDimension cellDimension = cellDimension();
        return new Position(
                (int) ((x - boardTopLeft.getX()) / cellDimension.getWidth()),
                (int) ((y - boardTopLeft.getY()) / cellDimension.getHeight()));
    }
}
