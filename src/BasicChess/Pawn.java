package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import pieces.EmptyPiece;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hj1012 on 15/10/14.
 */
public class Pawn extends ChessPiece{
	protected boolean justDidADoubleMove;
	protected int movementDirection;

	public Pawn(Board board, PieceType type, Location location) {
		super(board, type, location, "pawn");
		justDidADoubleMove = false;
		movementDirection = type == PieceType.WHITE ? -1 : 1;
	}

	public int returnValue() {
		return 1;
	}

	@Override
	protected boolean validInState(Location to) {
		return validBasicPawnMove(to) || validBasicPawnTake(to) || validPawnDoubleMove(to) || validEnPassant(to);
	}

	private boolean validBasicPawnMove(Location to) {
		if(to.getX() - cords.getX() == movementDirection) {
			if(to.getY() - cords.getY() == 0) {
				if(board.isEmptySpace(to)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean validBasicPawnTake(Location to) {
		if(to.getX() - cords.getX() == movementDirection) {
			if(Math.abs(to.getY() - cords.getY()) == 1) {
				PieceType targetType = board.getPiece(to).type;
				if(targetType != PieceType.EMPTY && targetType != type) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean validPawnDoubleMove(Location to) {
		int startingCol =  type == PieceType.WHITE ? board.numRows()-2 : 1;
		if(cords.getX() == startingCol && cords.getY() == to.getY()) {
			if(to.getX() == startingCol + movementDirection*2) {
				if(board.isEmptySpace(to) && board.isEmptySpace(new Location(cords.getX()+movementDirection, cords.getY()))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean validEnPassant(Location to) {
		if(!board.isEmptySpace(to)) {
			return false;
		}
		if(cords.getX() + movementDirection != to.getX()) {
			return false;
		}
		if(Math.abs(cords.getY() - to.getY()) != 1) {
			return false;
		}
		ChessPiece toTake = board.getPiece(new Location(cords.getX(), to.getY()));
		if(!(toTake instanceof Pawn)) {
			return false;
		}
		if(!((Pawn) toTake).justDidADoubleMove) {
			return false;
		}
		if(toTake.type == type) {
			return false;
		}
		if(toTake.lastTurnMovedOn != board.getController().getCurrentTurn() - 1) {
			return false;
		}
		return true;
	}

	@Override
	public boolean executeMove(Location to) {
		justDidADoubleMove = false;
		if(validBasicPawnMove(to) || validBasicPawnTake(to)) {
			return super.executeMove(to);
		} else if(validPawnDoubleMove(to)) {
			justDidADoubleMove = true;
			return super.executeMove(to);
		} else if(validEnPassant(to)) {
			board.placePiece(new Location(cords.getX(), to.getY()), new EmptyPiece(board, new Location(cords.getX(), to.getY())));
			return super.executeMove(to);
		} else {
			return false;
		}
	}

	@Override
	protected boolean testIfMoveEndsInCheck(Location to, Location from) {
		if(validEnPassant(to)) {
			Location pieceLocation = new Location(from.getX(), to.getY());
			Pawn toTake = (Pawn) board.getPiece(pieceLocation);
			board.placePiece(pieceLocation, new EmptyPiece(board, pieceLocation));
			boolean wouldPutMeInCheck = super.testIfMoveEndsInCheck(to, from);
			board.placePiece(pieceLocation, toTake);
			return wouldPutMeInCheck;
		} else {
			return super.testIfMoveEndsInCheck(to, from);
		}
	}

    @Override
    public List<Location> allPieceMoves() {
        LinkedList<Location> moves = new LinkedList<Location>();
        int newX = cords.getX() + movementDirection;
        if (newX >= 0 && newX < board.numCols()) {
            moves.add(new Location(newX, cords.getY()));
            if (cords.getY() > 0) {
                moves.add(new Location(newX, cords.getY() - 1));
            }
            if (cords.getY() < board.numRows() - 2) {
                moves.add(new Location(newX, cords.getY() + 1));
            }
        }
        int startX = type == PieceType.WHITE ? 6 : 1;
        if (cords.getX() == startX) {
            moves.add(new Location(startX + (2 * movementDirection), cords.getY()));
        }

        return moves;
    }
}
