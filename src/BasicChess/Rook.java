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
		super(board, type, cords);
	}

	public int returnValue() {
		return 5;
	}

	@Override
	public boolean invalidTarget(Location to) {
		int horizontalMovement = cords.getX().compareTo(to.getX());
		int verticalMovement = cords.getY().compareTo(to.getY());
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
		int horizontalMovement = cords.getX().compareTo(to.getX());
		int verticalMovement = cords.getY().compareTo(to.getY());
		for(int i = cords.getX() + horizontalMovement; i != to.getX(); i += horizontalMovement) {
			if(board.getPiece(new Location(i, cords.getY())).type != PieceType.EMPTY) {
				return true;
			}
		}
		for(int i = cords.getY() + verticalMovement; i != to.getY(); i += verticalMovement) {
			if(board.getPiece(new Location(cords.getX(), i)).type != PieceType.EMPTY) {
				return true;
			}
		}
		return false;
	}
}
