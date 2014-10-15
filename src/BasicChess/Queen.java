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
		super(board, type, location);
	}

	public int returnValue() {
		return 9;
	}

	//In order: Check if either Bishop or Rook could do the move
	public boolean isValidMove(Location pieceLocation, Location targetLocation) {
		return (new Bishop(board, type, null).isValidMove(pieceLocation, targetLocation))
				|| (new Rook(board, type, null).isValidMove(pieceLocation, targetLocation));
	}
}
