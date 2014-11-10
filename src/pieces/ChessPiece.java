package pieces;
import graphics.GraphicsControl;
import graphics.tools;
import main.Location;
import main.PieceType;
import main.Board;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a square on the board: With a chess piece that understands what moves it can make, or as an empty square.
 */
abstract public class ChessPiece {

    public Board board;
    public PieceType type;
    public Location cords;
	public int lastTurnMovedOn;
    public GraphicsControl graphics;
    public String image;

    public ChessPiece(Board board, PieceType type, Location cords, String img) {
        this.type = type;
        this.board = board;
        this.cords = cords;
        this.image = img;
        if (!tools.imageMap.containsKey(img)) {
            tools.loadPiece(img);
        }
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
        if (board.doDrawing) graphics.setGoal(targetLocation);
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

	/**
	 * @param to space to test if the piece can move to
	 * @return if the move is valid considering other pieces - the piece is not blocked, ect
	 */
	abstract protected boolean validInState(Location to);

    /**
     * @return A list of all moves this piece could make from this location on an empty board.
     */
    abstract public List<Location> allPieceMoves();

	/**
	 * @return The value of the piece, typically in pawns. Used to evaluate moves for AIs.
	 */
	abstract public int returnValue();

	public String toString() {
		return "a " + (isWhite() ? "White " : "Black ") + this.getClass().getCanonicalName() + " at " + cords;
	}

	abstract public String getName();

	protected String getMisc() {
		return "";
	}

	/**
	 * @return a string from which the piece can be reconstructed, given a board. Form is:
	 * string begins and ends with a '|'
	 * fields are separated with a '~'
	 * fields begin with 'X:' where X is a single character used for human-readability
	 * fields are, in order:
	 *  - N, for name such as 'Pawn',
	 *  - L, for last turn moved on
	 *  - X, for x coordinate
	 *  - Y, for y coordinate
	 *  - T, for type, 'White' or 'Black'
	 *  - M, for misc. This can contain anything including '~' and is only ended by '|'
	 */
	public String toCode() {
		StringBuilder code = new StringBuilder();
		code.append("|");
		code.append("N:" + this.getName());
		code.append("~");
		code.append("L:" + this.lastTurnMovedOn);
		code.append("~");
		code.append("X:" + cords.getX());
		code.append("~");
		code.append("Y:" + cords.getY());
		code.append("~");
		code.append("T:" + this.type.string());
		code.append("~");
		code.append("M:" + this.getMisc());
		code.append("|");
		return code.toString();
	}

	public void finishGen(int lastMovedOn, String miscFields) {
		this.lastTurnMovedOn = lastMovedOn;
		//ignore substring
	}
}
