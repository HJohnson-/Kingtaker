package main;

import pieces.ChessPiece;
import pieces.EmptyPiece;

import java.util.*;

/**
 * Stores and manipulates where on the board pieces are. Takes commands from pieces and a GameController, and exposes
 * the GameController it is linked to so its pieces can make queries such as if a move would put their king in check
 * while checking move validation.
 */
abstract public class Board {
	private pieces.ChessPiece[][] pieces;
	private GameController game;

	//returns true if human player goes first in offline mode or the lobby host goes first in online mode.
	abstract public boolean initializeBoard();

    public Board() {
        pieces = new ChessPiece[8][8];
        for (ChessPiece[] row : pieces) {
            Arrays.fill(row, new EmptyPiece(this, null));
        }
		this.initializeBoard();
    }

	/**
	 * Used to link the board and controller right after board is created. Game is null before this so the board can be
	 * created before the game, then the game created using the existing board.
	 */
	public void setController(GameController game) {
		this.game = game;
	}

	/**
	 *
	 * @return The controller linked with the board, for pieces to use functions such as isInCheck
	 */
	public GameController getController() {
		return game;
	}

	/**
	 * @param pieceLocation The Location object representing the space the piece is on. If not a valid Location for the
	 *                      board, should return null.
	 * @return The piece at the location if the location was valid, including an EmptySquare piece if it was at the
	 * location. null if invalid location.
	 */
	public pieces.ChessPiece getPiece(Location pieceLocation) {
		if(!onBoard(pieceLocation)) {
			return null;
		}
		return pieces[pieceLocation.getX()][pieceLocation.getY()];
	}

	/**
	 * @return The list of non-empty pieces on the board,starting from 0,0 and increasing the x first.
	 */
    public LinkedList<ChessPiece> allPieces() {
        LinkedList<ChessPiece> pieceList = new LinkedList <ChessPiece>();
        for (ChessPiece[] row : pieces) {
            for (ChessPiece piece : row) {
                if (piece.type != PieceType.EMPTY) {
                    pieceList.add(piece);
                }
            }
        }
        return pieceList;
    }

	/**
	 * @param from Unchecked location on the board
	 * @param to Unchecked location on the board
	 * @return if all spaces between to and from were empty, false if an input was invalid.
	 */
	public boolean clearLine(Location from, Location to) {
		if(!onBoard(from) || !onBoard(to)) {
			return false;
		}
		int horizontalMovement = to.getX().compareTo(from.getX());
		int verticalMovement = to.getY().compareTo(from.getY());
		for(int i = from.getX() + horizontalMovement, j = from.getY() + verticalMovement;
			i != to.getX() || j != to.getY();
			i += horizontalMovement, j+= verticalMovement) {
			if(getPiece(new Location(i, j)).type != PieceType.EMPTY) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param from location containing piece to be moved, to end empty
	 * @param to location to contain piece after function, previous piece annihilated
	 */
	public void movePiece(Location from, Location to) {
		placePiece(to, getPiece(from));
		clearSpace(from);
	}

	/**
	 * @param targetLocation location to place piece
	 * @param toPlace piece to place there
	 */
	public void placePiece(Location targetLocation, pieces.ChessPiece toPlace) {
		pieces[targetLocation.getX()][targetLocation.getY()] = toPlace;
		toPlace.cords = targetLocation;
	}

	/**
	 * @param targetLocation space to check
	 * @return if the space had PieceType empty;
	 */
	public boolean isEmptySpace(Location targetLocation) {
        ChessPiece p = getPiece(targetLocation);
        if (p == null) {
			p = new EmptyPiece(this, targetLocation);
			placePiece(targetLocation, p);
		}
		return p.type == PieceType.EMPTY;
	}

	/**
	 * @param pieceLocation the location on which a piece to be annihilated
	 */
	public void clearSpace(Location pieceLocation) {
		placePiece(pieceLocation, new pieces.EmptyPiece(this, pieceLocation));
	}

	/**
	 * param targetLocation possibly valid location
	 * @return if the location is valid
	 */
	public boolean onBoard(Location targetLocation) {
		return onBoard(targetLocation.getX(), targetLocation.getY());
	}

    private boolean onBoard(int x, int y) {
        if(x < 0 || y < 0){
            return false;
        }
        if(x >= numCols() || y >= numRows()) {
            return false;
        }
        return true;
    }

	/**
	 * @return number of rows on the board
	 */
	public int numRows() {
		return pieces.length;
	}

	/**
	 * @return length of first row of the board. Unspecified if the board doesn't have any rows. Why would you do that.
	 */
	public int numCols() { return pieces[0].length; }

}

