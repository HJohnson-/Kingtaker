package BasicChess;

import pieces.ChessPiece;
import main.Location;
import main.PieceType;
import main.Board;

/**
 * Created by hj1012 on 15/10/14.
 */
public class Rook extends ChessPiece {

	public Rook(Board board, PieceType type, Location cords) {
		super(board, type, cords, "rook");
	}

	public int returnValue() {
		return 5;
	}

	@Override
	public boolean invalidTarget(Location to) {
		int horizontalMovement = to.getX().compareTo(cords.getX());
		int verticalMovement = to.getY().compareTo(cords.getY());
		if(Math.abs(verticalMovement) == Math.abs(horizontalMovement)) {
			return true;
		}
		if(verticalMovement == 0 && horizontalMovement == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean beingBlocked(Location to) {
		return !board.clearLine(cords, to);
	}
}
