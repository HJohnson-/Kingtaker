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
		super(board, type, location, "king");
	}

	public int returnValue() {
		return REALLY_HIGH_NUMBER;
	}

	@Override
	protected boolean invalidTarget(Location to) {
		if(isAttemptingCastling(to)) {
			return false;
		}
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

	protected boolean invalidTargetNoCastle(Location to) {
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


	/*To castle:
	- The target space must be two squares from the King

	- The target space must be in the direction of an allied rook.

	- Both pieces must be on the first rank

	- Neither piece can have moved

	- There must be a clear line between the pieces

	- The King cannot be in check, move through a threatened square, or end in check

	In all valid castling attempts, the for loop will catch if the actual move of castling ends in check.
	If a new piece moves in an especially unusual way, the variant might need to override the king piece with something
	that can check for it and undo a failed castle attempt correctly for the new situations
	 */
	private boolean isAttemptingCastling(Location to) {
		boolean debugS = to.equals(new Location(7,2));
		int myFirstRank = (this.type == PieceType.WHITE ? board.numCols()-1 : 0);
		int movementDirection = to.getY().compareTo(cords.getY());
		int rookY = (movementDirection == 1 ? board.numRows()-1 : 0 );
		ChessPiece targetRook = board.getPiece(new Location(cords.getX(), rookY));

		if(Math.abs(cords.getY() - to.getY()) != 2) {
			return false;
		}

		if(!(targetRook instanceof Rook)) {
			return false;
		}

		if(cords.getX() != myFirstRank || myFirstRank != to.getX()) {
			return false;
		}

		if(lastTurnMovedOn != 0 || targetRook.lastTurnMovedOn != 0) {
			return false;
		}

		if(!board.clearLine(cords, to)) {
			return false;
		}

		if(board.isInCheck(this.type)) {
			return false;
		}

		/*for(int i = cords.getY(); i != to.getY(); i += movementDirection) {
			if(testIfMoveEndsInCheck(cords, new Location(cords.getX(), i))) {
				return false;
			}
		}*/

		return true;
	}

	@Override
	public boolean isValidMove(Location to, boolean careAboutCheck) {
		if (careAboutCheck ? invalidTarget(to) : invalidTargetNoCastle(to)) {
			return false;
		} else if (beingBlocked(to)) {
			return false;
		} else if (takingOwnPiece(board.getPiece(to))) {
			return false;
		} else {
			if (careAboutCheck) {
				Location from = cords;
				boolean takingKing = board.isKing(to);
				boolean wouldPutMeInCheck = testIfMoveEndsInCheck(to, from);
				if (wouldPutMeInCheck && !takingKing) {
					return false;
				}
			}
			return true;
		}
	}

	@Override
	protected boolean beingBlocked(Location to) {
		return false;
	}
}
