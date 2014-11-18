package pieces;
import main.Board;
import main.Location;
import main.PieceType;

import java.util.LinkedList;
import java.util.List;

/**
 * A null pattern of a ChessPiece that emulates what an empty square can do: Nothing.
 */
public class EmptyPiece extends ChessPiece {

    public EmptyPiece(Board board, Location loc) {
        super(board, PieceType.EMPTY, loc, "pawn");
    }

	public int returnValue() {return 0;}

    public boolean isValidMove(Location to) {
        return false;
    }

	@Override
	protected boolean validInState(Location to) {
		return false;
	}

	@Override
	public List<Location> allPieceMoves() {
		return new LinkedList<Location>();
	}

	public String getName() {
		return "NOT A PIECE";
	}

    @Override
    public ChessPiece clone() {
        return new EmptyPiece(null, cords.clone());
    }
}
