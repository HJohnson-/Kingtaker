package pieces;
import main.Location;
import main.PieceType;
import main.Board;

/**
 * Created by rp1012 on 15/10/14.
 */
abstract public class ChessPiece {

    protected Board board;
    public PieceType type;
    public Location cords;

    public ChessPiece(Board board, PieceType type, Location cords) {
        this.type = type;
        this.board = board;
        this.cords = cords;
    }

    public ChessPiece(Board board, PieceType type, int x, int y) {
        this.type = type;
        this.board = board;
        this.cords = new Location(x, y);
    }

    abstract public boolean isValidMove(Location from, Location to);

	abstract public int returnValue();

}
