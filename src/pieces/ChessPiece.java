package pieces;
import main.Location;
import main.PieceType;
import main.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

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
		lastTurnMovedOn = board.getCurrentTurn();
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
	//the piece is owned by the turn player. After executing the move, crudely backtracks if it would put the user into
	//check. Pieces with moves such as Castling and En Passant that can affect squares other than the To and From will
	//need to overwrite this function to add more accurate backtracking for those cases.
    public boolean isValidMove(Location to) {
		if(invalidTarget(to)) {
			return false;
		} else if(beingBlocked(to)) {
			return false;
		} else if(takingOwnPiece(board.getPiece(to))) {
			return false;
		} else {
			Location from = cords;
			ChessPiece onto = board.getPiece(to);
			executeMove(to);
			if(board.isInCheck(type == PieceType.WHITE)) {
				board.placePiece(from, this);
				board.placePiece(to, onto);
				return false;
			}
			return true;
		}
	}

	private boolean takingOwnPiece(ChessPiece target) {
		if(type == target.type) {
			return true;
		} else {
			return false;
		}
	}

	abstract protected boolean invalidTarget(Location to);

	abstract protected boolean beingBlocked(Location to);

	abstract public int returnValue();

}
