package pieces;
import graphics.AnimationControl;
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
abstract public class ChessPiece implements Runnable {

    protected Board board;
    public String img;
    public PieceType type;
    public Location cords;
	public int lastTurnMovedOn;
    public AnimationControl animation;
    public JPanel panel;

    public ChessPiece(Board board, PieceType type, Location cords, String img) {
        this.type = type;
        this.board = board;
        this.cords = cords;
		this.img = img;
		lastTurnMovedOn = 0;
        animation = new AnimationControl();
        if (cords != null) {
            animation.curCords = animation.endCords =
                    new Location(cords.getX() * tools.CELL_WIDTH, cords.getY() * tools.CELL_HEIGHT);
        }
    }

	//Assumes move is totally valid, doesn't have to check anything, just does the move.
	//Returns true if nothing goes around throwing errors at it
	//By default, just puts the piece at the target location and leaves nothing where the piece used to be
	//Some variant pieces will override that. And the King when it's castling. So will pawns.
	//Pawns make everything difficult.
	public boolean executeMove(Location targetLocation) {
		board.clearSpace(cords);
		board.placePiece(targetLocation, this);
		this.lastTurnMovedOn = board.getController().getCurrentTurn();
		return true;
	}

	//Like an execute move that stuff doesn't overwrite
	private void doMove(Location loc) {
		board.clearSpace(cords);
		board.placePiece(loc, this);
	}

	public boolean isWhite() {
		return type == PieceType.WHITE;
	}

	//Returns true if the piece could move to to location on its turn. Doesn't check if
	//the piece is owned by the turn player. Uses 'testIfMoveEndsInCheck' which executes then un-does the move
	//and returns if it put the moving player in check.

	public boolean isValidMove(Location to) {
		return isValidMove(to, true);
	}

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

	//This makes then un-does the move, but makes the assumption that a move only affects the square the piece started
	//in and moved towards, and affects them in a specific way. Pieces with moves such as Pawn Promotion, En Passant,
	//and castling will need to override this function or checking validity of moves will alter the board state.
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

    /*
     *  @return A list of all moves this piece could make from this location on an empty board.
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

	abstract public int returnValue();

	public String toString() {
		return "a " + (isWhite() ? "White " : "Black ") + this.getClass().getCanonicalName() + " at " + cords;
	}

    @Override
    public void run() {

        int animationXStep = (animation.endCords.getX() - animation.curCords.getX()) / animation.totalSteps;
        int animationYStep = (animation.endCords.getY() - animation.curCords.getY()) / animation.totalSteps;
        animation.animating = true;

        while (!animation.curCords.equals(animation.endCords)) {

            animation.curCords.incrX(animationXStep);
            animation.curCords.incrY(animationYStep);
            panel.repaint();

            try {
                Thread.sleep(animation.animationTime);
            } catch (InterruptedException e) {
                System.err.println(e);
            }

        }

        animation.animating = false;
    }

}
