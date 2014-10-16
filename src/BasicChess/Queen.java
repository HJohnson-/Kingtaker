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

	@Override
	protected boolean invalidTarget(Location to) {
		int horizontalMovement = to.getX().compareTo(cords.getX());
		int verticalMovement = to.getY().compareTo(cords.getY());
		if(verticalMovement == 0 && horizontalMovement == 0) {
			return true;
		}
		if((Math.abs(to.getX() - cords.getX()) != Math.abs(to.getY() - cords.getY()))
				&& (Math.abs(verticalMovement) == Math.abs(horizontalMovement))
				) {
			return true;
		}
		return false;
	}

	//The queen should not use this function as it overwrites isValidMove entirely.
	@Override
	protected boolean beingBlocked(Location to) {
		int horizontalMovement = to.getX().compareTo(cords.getX());
		int verticalMovement = to.getY().compareTo(cords.getY());
		for(int i = cords.getX() + horizontalMovement, j = cords.getY() + verticalMovement;
			i != to.getX() || j != to.getY();
			i += horizontalMovement, j+= verticalMovement) {
			if(board.getPiece(new Location(i, j)).type != PieceType.EMPTY) {
				return true;
			}
		}
		return false;
	}

}
