package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

/**
 * Created by hj1012 on 15/10/14.
 */
public class Pawn extends ChessPiece{
	public Pawn(Board board, PieceType type) {
		super(board, type);
	}

	public int returnValue() {
		return 1;
	}

	//In order: Check if making doubleMove
	public boolean isValidMove(Location pieceLocation, Location targetLocation) {
		boolean isWhite = type == PieceType.WHITE;
		int movementDirection = isWhite ? 1 : -1;
		int startingRow = isWhite ? 1 : board.numRows()-2;


		ChessPiece movingOnto = board.getPiece(targetLocation);

		if(pieceLocation.getY() == startingRow) {
			if(targetLocation.getY() == startingRow + movementDirection*2) {
				if(movingOnto.type == PieceType.EMPTY &&
						board.getPiece(new Location(pieceLocation.getX(), pieceLocation.getY()+1)).type == PieceType.EMPTY) {
					return true;
				}
			}
		}


		if(targetLocation.getY() - pieceLocation.getY() != movementDirection) {
			return false;
		}
		if(Math.abs(targetLocation.getX() - pieceLocation.getX()) == 1) {
			if(movingOnto.type == type || movingOnto.type == PieceType.EMPTY) {
				return false;
			}
		} else {
			if(Math.abs(targetLocation.getX() - pieceLocation.getX()) != 0 || movingOnto.type != PieceType.EMPTY) {
				return false;
			}
		}

		return true;
	}
}
