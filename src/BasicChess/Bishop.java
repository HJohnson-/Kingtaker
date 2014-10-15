package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

/**
 * Created by hj1012 on 15/10/14.
 */
public class Bishop extends ChessPiece {
	public Bishop(Board board, PieceType type) {
		super(board, type);
	}

	public int returnValue() {
		return 3;
	}

	//In order: Check if move is diagonal, check if move is DIRECTLY diagonal, check if there is a piece blocking, check if you are taking your own piece.
	public boolean isValidMove(Location pieceLocation, Location targetLocation) {
		int horizontalMovement = pieceLocation.getX().compareTo(targetLocation.getX());
		int verticalMovement = pieceLocation.getY().compareTo(targetLocation.getY());
		if(verticalMovement == 0 || 0 == horizontalMovement) {
			return false;
		}
		if(Math.abs(targetLocation.getX() - pieceLocation.getX()) != Math.abs(targetLocation.getY() - pieceLocation.getY())) {
			return false;
		}
		for(int i = pieceLocation.getX(), j = pieceLocation.getY();
			i != targetLocation.getX() && j != targetLocation.getY();
			i += horizontalMovement, j+= verticalMovement) {
			if(board.getPiece(new Location(i, j)).type != PieceType.EMPTY) {
				return false;
			}
		}
		if(this.type == board.getPiece(targetLocation).type) {
			return false;
		}
		return true;
	}
}
