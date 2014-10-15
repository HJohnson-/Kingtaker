package pieces;
import main.Location;
import main.PieceType;
import main.Board;

/**
 * Created by rp1012 on 15/10/14.
 */
abstract public class ChessPiece {

    private Board board;
    public PieceType type;

    public ChessPiece(Board board, PieceType type) {
        this.type = type;
        this.board = board;
    }

    abstract public boolean isValidMove(Location from, Location to);

}
