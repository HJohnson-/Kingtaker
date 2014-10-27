package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import java.util.LinkedList;
import java.util.List;

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

	/*
	@Override
	protected boolean invalidTarget(Location to) {
		if(validCastleAttempt(to)) {
			return false;
		}
		int verticalDistance = Math.abs(cords.getY() - to.getY());
		int horizontalDistance = Math.abs(cords.getX() - to.getX());
		if(verticalDistance > 1 || horizontalDistance > 1) {
			return true;
		}
		return (verticalDistance == 0 && horizontalDistance == 0);
	}

	protected boolean invalidTargetNoCastle(Location to) {
		int verticalDistance = Math.abs(cords.getY() - to.getY());
		int horizontalDistance = Math.abs(cords.getX() - to.getX());
		if(verticalDistance > 1 || horizontalDistance > 1) {
			return true;
		}
		return (verticalDistance == 0 && horizontalDistance == 0);
	}
	*/


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
	private boolean validCastleAttempt(Location to) {
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

		if(board.getController().isInCheck(this.type)) {
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
	public boolean executeMove(Location to) {
		if(validCastleAttempt(to)) {
			int movementDirection = to.getY().compareTo(cords.getY());
			int rookY = (movementDirection == 1 ? board.numRows()-1 : 0 );
			board.movePiece(new Location(cords.getX(), rookY), new Location(cords.getX(), cords.getY()+movementDirection));
			return super.executeMove(to);
		} else {
			return super.executeMove(to);
		}
	}

	@Override
	public boolean isValidMove(Location to, boolean careAboutCheck) {
		if (careAboutCheck ? validInState(to) : validInStateNoCastle(to)) {
			return false;
		} else if (takingOwnPiece(board.getPiece(to))) {
			return false;
		} else {
			if (careAboutCheck) {
				Location from = cords;
				boolean takingKing = board.getController().isKing(to);
				boolean wouldPutMeInCheck = testIfMoveEndsInCheck(to, from);
				if (wouldPutMeInCheck && !takingKing) {
					return false;
				}
			}
			return true;
		}
	}

	protected boolean validInStateNoCastle(Location to) {
		return adjacent(to);
	}

	@Override
	protected boolean validInState(Location to) {
		return validCastleAttempt(to) || adjacent(to);
	}

	private boolean adjacent(Location to) {
		return Math.abs(cords.getX() - to.getX()) < 2 && Math.abs(cords.getY() - to.getY()) < 2;
	}

    @Override
    public List<Location> allPieceMoves() {
        List<Location> moves = new LinkedList<Location>();
        for (int i = -1; i <= 1; i++) {
            int newX = cords.getX() + i;
            if (newX < 0 || newX >= board.numCols()) continue;
            for (int j = -1; j <= 1; j++) {
                int newY = cords.getY() + j;
                if (newY < 0 || newY >= board.numRows()) continue;
                moves.add(new Location(newX, newY));
            }
        }

        return moves;
    }
}
