// It knows the game rules
public class Game {

    private final Board board;

    public Game(Board board) {
        this.board = board;
    }

    public boolean isValidFrom(Position from) {
        if (board.isFilled(from) && verifyDirection(from)) {
            return true;
        } else {
            return false;
        }
    }
    // Assumes validFrom is a valid starting position
    public boolean isValidTo(Position validFrom, Position to) {
        if (isValidFrom(validFrom) && board.isEmpty(to) && validFrom.colinear(to) && validFrom.distance(to) == 2) {
            Position middle = validFrom.middle(to);
            if (!board.isEmpty(middle)) {
                return true;
            }
        }
        return false;
    }

    // Assumes both positions are valid
    public Position move(Position validFrom, Position validTo) {
        Position middle= validFrom.middle(validTo);
        board.emptyPosition(validFrom);
        board.emptyPosition(middle);
        board.fillPosition(validTo);
        return middle;
    }

    public boolean hasValidMovesFrom() {
        for (int i=0;i<board.getWidth();i++){
            for (int j=0;j<board.getHeight();j++){
                if(isValidFrom(new Position(i,j))){
                    return true;
                }
            }
        }
        return false;
    }

    public int countValidMovesFrom() {
        int validmovesfrom = 0;
            for (int i = 0; i < board.getWidth(); i++) {
                for (int j = 0; j < board.getHeight(); j++) {
                    if (isValidFrom(new Position(i, j))) {
                        validmovesfrom += 1;
                    }
                }
            }
        return validmovesfrom;
        }

    // Assumes validFrom is a valid starting position
    public int countValidMovesTo(Position validFrom) {
        int validmovesto=0;
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                if (isValidTo(validFrom,new Position(i,j))) {
                    validmovesto += 1;
                }
            }
        }
        return validmovesto;
    }

    private boolean verifyDirection(Position from) {
        for (int i = 0; i < Direction.ALL.length; i++) {
            Position cell = Direction.ALL[i].apply(from);
            if (board.isFilled(cell) && board.isEmpty(Direction.ALL[i].apply(cell))) {
                return true;
            }
        }
        return false;
    }
}



