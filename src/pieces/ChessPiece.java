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

	//Returns true if the piece could move from from location to to location on its turn. Doesn't check if move would put
	//the piece's team's king in check or if the piece is owned by the turn player.
    abstract public boolean isValidMove(Location from, Location to);

	abstract public int returnValue();

}
