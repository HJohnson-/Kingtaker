package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

/**
 * Created by hj1012 on 15/10/14.
 */
public class Bishop extends ChessPiece {
	public Bishop(Board board, PieceType type, Location cords) {
		super(board, type, cords, "bishop");
	}

	public int returnValue() {
		return 3;
	}

	@Override
	protected boolean beingBlocked(Location to) {
		int horizontalMovement = to.getX().compareTo(cords.getX());
		int verticalMovement = to.getY().compareTo(cords.getY());
		for(int i = cords.getX() + horizontalMovement, j = cords.getY() + verticalMovement;
			i != to.getX() && j != to.getY();
			i += horizontalMovement, j+= verticalMovement) {
			if(board.getPiece(new Location(i, j)).type != PieceType.EMPTY) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean invalidTarget(Location to) {
		int horizontalMovement = cords.getX().compareTo(to.getX());
		int verticalMovement = cords.getY().compareTo(to.getY());
		if(verticalMovement == 0 || 0 == horizontalMovement) {
			return true;
		}
		if(Math.abs(to.getX() - cords.getX()) != Math.abs(to.getY() - cords.getY())) {
			return true;
		}
		return false;
	}
}
