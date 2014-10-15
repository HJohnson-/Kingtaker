package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

/**
 * Created by hj1012 on 15/10/14.
 */
public class Knight extends ChessPiece {
	public Knight(Board board, PieceType type) {
		super(board, type);
	}

	public int returnValue() {
		return 3;
	}

	//In order: Check if move is knightish, check if taking your own piece.
	public boolean isValidMove(Location pieceLocation, Location targetLocation) {
		int horizontalMovement = pieceLocation.getX() - targetLocation.getX();
		int verticalMovement = pieceLocation.getY() -targetLocation.getY();

		if((Math.abs(horizontalMovement) == 1 && Math.abs(verticalMovement) == 2)
			|| (Math.abs(verticalMovement) == 1 && Math.abs(horizontalMovement) == 2))	{
			if(this.type == board.getPiece(targetLocation).type) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
}
