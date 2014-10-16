package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

/**
 * Created by hj1012 on 15/10/14.
 */
public class King extends ChessPiece {

	private static int REALLY_HIGH_NUMBER = 999999;

	public King(Board board, PieceType type, Location location) {
		super(board, type, location);
	}

	public int returnValue() {
		return REALLY_HIGH_NUMBER;
	}

	@Override
	protected boolean invalidTarget(Location to) {
		int verticalDistance = Math.abs(cords.getY() - to.getY());
		int horizontalDistance = Math.abs(cords.getX() - to.getX());
		if(verticalDistance > 1 || horizontalDistance > 1) {
			return true;
		}
		if(verticalDistance == 0 && horizontalDistance == 0) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean beingBlocked(Location to) {
		return false;
	}
}
