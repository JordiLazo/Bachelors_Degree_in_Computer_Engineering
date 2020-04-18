public class Direction {

    public static final Direction UP = new Direction(0,-1);
    public static final Direction RIGHT = new Direction(1,0);
    public static final Direction DOWN = new Direction(0,1);
    public static final Direction LEFT = new Direction(-1,0);

    public static final Direction[] ALL = new Direction[]{UP, RIGHT, DOWN, LEFT};

    private final int dx;
    private final int dy;

    private Direction(int dx, int dy) {
        this.dx=dx;
        this.dy=dy;
    }

    public Position apply(Position from) {
        Position apply= new Position(from.getX()+dx,from.getY()+dy);
        return apply;
    }
}
