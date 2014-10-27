package pieces;
import graphics.GraphicsControl;
import graphics.tools;
import main.Location;
import main.PieceType;
import main.Board;

import javax.swing.*;
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
    public GraphicsControl graphics;

    public ChessPiece(Board board, PieceType type, Location cords, String img) {
        this.type = type;
        this.board = board;
        this.cords = cords;
		this.img = img;
		lastTurnMovedOn = 0;

        if (cords != null) {
            graphics = new GraphicsControl(cords, cords);
        }
    }

	/**
	 * Assumes move is valid. Behaviour undefined and can be freely overwritten by a piece.
	 * @param targetLocation location that represents the move the piece is going to make. Usually the space it will
	 *                          end up on.
	 * @return if move was successful.
	 */
	public boolean executeMove(Location targetLocation) {
		board.clearSpace(cords);
		board.placePiece(targetLocation, this);
		this.lastTurnMovedOn = board.getController().getCurrentTurn();
		return true;
	}

	/**
	 * same behaviour as executeMove on a generic chess piece, but will not be overwritten by side-effects.
	 * @param loc where to put a piece
	 */
	private void doMove(Location loc) {
		board.clearSpace(cords);
		board.placePiece(loc, this);
	}

	public boolean isWhite() {
		return type == PieceType.WHITE;
	}

	public boolean isValidMove(Location to) {
		return isValidMove(to, true);
	}

	/**
	 * @param to Check if a move to this location is valid. What a piece defines as valid is to be defined by the piece.
	 * @param careAboutCheck  If the function will test on putting its own king in check.
	 * @return if it's valid.
	 */
    public boolean isValidMove(Location to, boolean careAboutCheck) {
		if(!validInState(to)) {
			return false;
		} else if(takingOwnPiece(board.getPiece(to))) {
			return false;
		} else {
			if(careAboutCheck) {
				Location from = cords;
				boolean takingKing = board.getController().isKing(to);
				boolean wouldPutMeInCheck = testIfMoveEndsInCheck(to, from);
				if(wouldPutMeInCheck && !takingKing) {
					return false;
				}
			}
			return true;
		}
	}


	/**
	 * @param to position the move is from, which is just cords. Used to un-do the move when cords changes.
	 * @param from location moving to, which can have any effect depending on the pieces that overwrite this.
	 * @return if the move puts the turn player in check
	 */
	protected boolean testIfMoveEndsInCheck(Location to, Location from) {
		ChessPiece onto = board.getPiece(to);
		doMove(to);
		boolean wouldPutMeInCheck = board.getController().isInCheck(isWhite());
		board.placePiece(from, this);
		board.placePiece(to, onto);
		return wouldPutMeInCheck;
	}

	protected boolean takingOwnPiece(ChessPiece target) {
		return type == target.type;
	}

	abstract protected boolean validInState(Location to);

    /**
     * @return A list of all moves this piece could make from this location on an empty board.
     */
    public List<Location> allPieceMoves() {
        LinkedList<Location> moves = new LinkedList<Location>();
        for (int x = 0; x < board.numCols(); x++) {
            for (int y = 0; y < board.numRows(); y++) {
                moves.add(new Location(x, y));
            }
        }
        return moves;
    }

	/**
	 * @return The value of the piece, typically in pawns. Used to evaluate moves for AIs.
	 */
	abstract public int returnValue();

	public String toString() {
		return "a " + (isWhite() ? "White " : "Black ") + this.getClass().getCanonicalName() + " at " + cords;
	}

}
