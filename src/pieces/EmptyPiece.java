package pieces;
import main.Board;
import main.Location;
import main.PieceType;

/**
 * Created by rp1012 on 15/10/14.
 */
public class EmptyPiece extends ChessPiece {

    public EmptyPiece(Board board) {
        super(board, PieceType.EMPTY);
    }

    @Override
    public int returnValue() {
        return 0;
    }

    public boolean isValidMove(Location from, Location to) {
        return false;
    }

}
