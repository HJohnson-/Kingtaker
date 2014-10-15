package BasicChess;

import pieces.ChessPiece;
import main.Location;
import main.PieceType;
import main.Board;

/**
 * Created by hj1012 on 15/10/14.
 */
public class Rook extends ChessPiece {

	public Rook(Board board, PieceType type) {
		super(board, type);
	}

	public int returnValue() {
		return 5;
	}


	//In order: Check if move is orthogonal, check if there is a piece blocking, check if you are taking your own piece.
	public boolean isValidMove(Location pieceLocation, Location targetLocation) {
		int verticalMovement = pieceLocation.getX().compareTo(targetLocation.getX());
		int horizontalMovement = pieceLocation.getY().compareTo(targetLocation.getY());
		if(Math.abs(horizontalMovement) == Math.abs(verticalMovement)) {
			return false;
		}
		for(int i = pieceLocation.getX(); i != targetLocation.getX(); i += verticalMovement) {
			if(board.getPiece(new Location(i, pieceLocation.getY())).type != PieceType.EMPTY) {
				return false;
			}
		}
		for(int i = pieceLocation.getY(); i != targetLocation.getY(); i += horizontalMovement) {
			if(board.getPiece(new Location(pieceLocation.getX(), i)).type != PieceType.EMPTY) {
				return false;
			}
		}
		if(this.type == board.getPiece(targetLocation).type) {
			return false;
		}
		return true;
	}
}
