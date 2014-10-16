package pieces;
import main.Board;
import main.Location;
import main.PieceType;

/**
 * Created by rp1012 on 15/10/14.
 */
public class EmptyPiece extends ChessPiece {

    public EmptyPiece(Board board, Location loc) {
        super(board, PieceType.EMPTY, loc);
    }

	public int returnValue() {return 0;}

    public boolean isValidMove(Location to) {
        return false;
    }

	@Override
	protected boolean beingBlocked(Location to) {
		return false;
	}

	@Override
	protected boolean invalidTarget(Location to) {
		return false;
	}

}
