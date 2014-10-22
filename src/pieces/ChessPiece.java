package pieces;
import main.Location;
import main.PieceType;
import main.Board;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rp1012 on 15/10/14.
 */
abstract public class ChessPiece {

    protected Board board;
    public String img;
    public PieceType type;
    public Location cords;
	public int lastTurnMovedOn;

    public ChessPiece(Board board, PieceType type, Location cords, String img) {
        this.type = type;
        this.board = board;
        this.cords = cords;
		this.img = img;
		lastTurnMovedOn = 0;
    }

	//Assumes move is totally valid, doesn't have to check anything, just does the move.
	//Returns true if nothing goes around throwing errors at it
	//By default, just puts the piece at the target location and leaves nothing where the piece used to be
	//Some variant pieces will override that. And the King when it's castling. So will pawns.
	//Pawns make everything difficult.
	public boolean executeMove(Location targetLocation) {
		board.clearSpace(cords);
		board.placePiece(targetLocation, this);
		return true;
	}

	//Returns true if the piece could move to to location on its turn. Doesn't check if
	//the piece is owned by the turn player. Uses 'testIfMoveEndsInCheck' which executes then un-does the move
	//and returns if it put the moving player in check.

	public boolean isValidMove(Location to) {
		return isValidMove(to, true);
	}

    public boolean isValidMove(Location to, boolean careAboutCheck) {
		if(invalidTarget(to)) {
			return false;
		} else if(beingBlocked(to)) {
			return false;
		} else if(takingOwnPiece(board.getPiece(to))) {
			return false;
		} else {
			if(careAboutCheck) {
				Location from = cords;
				boolean takingKing = board.isKing(to);
				boolean wouldPutMeInCheck = testIfMoveEndsInCheck(to, from);
				if(wouldPutMeInCheck && !takingKing) {
					return false;
				}
			}
			return true;
		}
	}

	//This makes then un-does the move, but makes the assumption that a move only affects the square the piece started
	//in and moved towards, and affects them in a specific way. Pieces with moves such as Pawn Promotion, En Passant,
	//and castling will need to override this function or checking validity of moves will alter the board state.
	protected boolean testIfMoveEndsInCheck(Location to, Location from) {
		ChessPiece onto = board.getPiece(to);
		executeMove(to);
		boolean wouldPutMeInCheck = board.isInCheck(type == PieceType.WHITE);
		board.placePiece(from, this);
		board.placePiece(to, onto);
		return wouldPutMeInCheck;
	}

	protected boolean takingOwnPiece(ChessPiece target) {
		return type == target.type;
	}

	abstract protected boolean invalidTarget(Location to);

	abstract protected boolean beingBlocked(Location to);

    public List<Location> allUnblockedMoves() {
        LinkedList<Location> moves = new LinkedList<Location>();
        for (int x = 0; x < board.numCols(); x++) {
            for (int y = 0; y < board.numRows(); y++) {
                moves.add(new Location(x, y));
            }
        }
        return moves;
    }

	abstract public int returnValue();

	public String toString() {
		return "a " + (type == PieceType.WHITE ? "White " : "Black ") + this.getClass().getCanonicalName() + " at " + cords + "\n";
	}

}
