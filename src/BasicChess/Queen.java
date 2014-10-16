package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

/**
 * Created by hj1012 on 15/10/14.
 */
public class Queen extends ChessPiece {
	public Queen(Board board, PieceType type, Location location) {
		super(board, type, location, "queen");
	}

	public int returnValue() {
		return 9;
	}

	//The queen should not use this function as it overwrites isValidMove entirely.
	@Override
	protected boolean invalidTarget(Location to) {
		return true;
	}

	//The queen should not use this function as it overwrites isValidMove entirely.
	@Override
	protected boolean beingBlocked(Location to) {
		return true;
	}

	//In order: Check if either Bishop or Rook could do the move
	@Override
	public boolean isValidMove(Location targetLocation) {
		return (new Bishop(board, type, cords).isValidMove(targetLocation))
				|| (new Rook(board, type, cords).isValidMove(targetLocation));
	}
}
