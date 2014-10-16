package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

/**
 * Created by hj1012 on 15/10/14.
 */
public class Knight extends ChessPiece {
	public Knight(Board board, PieceType type, Location cords) {
		super(board, type, cords);
	}

	public int returnValue() {
		return 3;
	}

	@Override
	protected boolean beingBlocked(Location to) {
		return false;
	}

	@Override
	protected boolean invalidTarget(Location to) {
		int horizontalMovement = cords.getX() - to.getX();
		int verticalMovement = cords.getY() -to.getY();

		if((Math.abs(horizontalMovement) == 1 && Math.abs(verticalMovement) == 2)
				|| (Math.abs(verticalMovement) == 1 && Math.abs(horizontalMovement) == 2)) {
			return false;
		} else {
			return true;
		}
	}
}
