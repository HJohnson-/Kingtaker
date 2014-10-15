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

	public King(Board board, PieceType type) {
		super(board, type);
	}

	public int returnValue() {
		return REALLY_HIGH_NUMBER;
	}

	public boolean isValidMove(Location pieceLocation, Location targetLocation) {
		int verticalDistance = Math.abs(pieceLocation.getY() - targetLocation.getY());
		int horizontalDistance = Math.abs(pieceLocation.getX() - targetLocation.getX());
		if(verticalDistance > 1 || horizontalDistance > 1) {
			return false;
		}
		if(verticalDistance == 0 && horizontalDistance == 0) {
			return false;
		}
		if(board.getPiece(targetLocation).type == this.type) {
			return false;
		}
		
		return true;
	}
}
