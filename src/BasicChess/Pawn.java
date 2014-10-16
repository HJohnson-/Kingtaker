package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

/**
 * Created by hj1012 on 15/10/14.
 */
public class Pawn extends ChessPiece{
	protected boolean justDidADoubleMove;
	protected int movementDirection;

	public Pawn(Board board, PieceType type, Location location) {
		super(board, type, location, "pawn");
		justDidADoubleMove = false;
		movementDirection = type == PieceType.WHITE ? 1 : -1;
	}

	public int returnValue() {
		return 1;
	}

	//Check1: On a double move, there is an empty space before the target
	//Check2: On a non-taking move, there is an empty space at the target
	@Override
	protected boolean beingBlocked(Location to) {
		if(Math.abs(to.getY() - cords.getY()) == 2) {
			if(board.getPiece(new Location(cords.getX(), cords.getY()+movementDirection)).type != PieceType.EMPTY) {
				return true;
			}
		}
		if(to.getX() != cords.getX()) {
			if(board.getPiece(to).type != PieceType.EMPTY) {
				return true;
			}
		}

		return false;
	}

	//Checks all the weird types of chess moves a pawn can do
	@Override
	protected boolean invalidTarget(Location to) {
		if(validPawnDoubleMove(to)) {
			return false;
		} else if(validBasicPawnTake(to)) {
			return false;
		} else if(validBasicPawnMove(to)) {
			return false;
		} else if(validEnPassant(to)) {
			return false;
		} else {
			return true;
		}
	}

	private boolean validPawnDoubleMove(Location to) {
		int startingRow =  type == PieceType.WHITE ? 1 : board.numRows()-2;

		if(cords.getY() == startingRow) {
			if(to.getY() == startingRow + movementDirection*2) {
				return true;
			}
		}
		return false;
	}

	private boolean validBasicPawnTake(Location to) {
		if(to.getY() - cords.getY() == movementDirection) {
			if(Math.abs(to.getX() - cords.getX()) == 1) {
				PieceType targetType = board.getPiece(to).type;
				if(targetType != PieceType.EMPTY && targetType != type) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean validBasicPawnMove(Location to) {
		if(to.getY() - cords.getY() == movementDirection) {
			if(to.getX() - cords.getX() == 0) {
				return true;
			}
		}
		return false;
	}

	//TODO EnPassant
	private boolean validEnPassant(Location to) {
		return false;
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
			//Panic Excessively
			return false;
		}
		if(Math.abs(targetLocation.getX() - pieceLocation.getX()) == 1) {
			if(movingOnto.type == type || movingOnto.type == PieceType.EMPTY) {
				return false;
			}
		} else {
			return false;
		}

		return true;
	}
}
