package main;

import pieces.ChessPiece;
import pieces.EmptyPiece;

import java.util.*;

/**
 * Created by hj1012 on 15/10/14.
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

	public void setController(GameController game) {
		this.game = game;
	}

	public GameController getController() {
		return game;
	}

	public pieces.ChessPiece getPiece(Location pieceLocation) {
		if(!onBoard(pieceLocation)) {
			return null;
		}
		return pieces[pieceLocation.getX()][pieceLocation.getY()];
	}

    public LinkedList<ChessPiece> allPieces() {
        LinkedList<ChessPiece> pieceList = new LinkedList <ChessPiece>();

        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if (pieces[i][j].type != PieceType.EMPTY) {
                    pieceList.add(pieces[i][j]);
                }
            }
        }

        return pieceList;
    }

	//Checks if every space between from and to is empty, not including from or to. Works in the 8 directions a Queen
	//can move.
	public boolean clearLine(Location from, Location to) {
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

	public void movePiece(Location from, Location to) {
		placePiece(to, getPiece(from));
		clearSpace(from);
	}

	//Places the given piece at targetLocation -important for initializing the board where pieces must be placed without
	//being on the board previously.
	public boolean placePiece(Location targetLocation, pieces.ChessPiece toPlace) {
		try {
			pieces[targetLocation.getX()][targetLocation.getY()] = toPlace;
			toPlace.cords = targetLocation;
			return true;
		} catch (Error e) {
			return false;
		}
	}

	//true if the space contains an EmptyPiece;
	public boolean isEmptySpace(Location targetLocation) {
        ChessPiece p = getPiece(targetLocation);
        if (p == null) placePiece(targetLocation, new EmptyPiece(this, targetLocation));
		return p.type == PieceType.EMPTY;
	}

	//replaces the Piece on the location with an EmptyPiece;
	public void clearSpace(Location pieceLocation) {
		placePiece(pieceLocation, new pieces.EmptyPiece(this, pieceLocation));
	}

	//Returns true if the targetLocation is within the confines of the board.
	public boolean onBoard(Location targetLocation) {
		return onBoard(targetLocation.getX(), targetLocation.getY());
	}

    public boolean onBoard(int x, int y) {
        if(x < 0 || y < 0){
            return false;
        }
        if(x >= numCols() || y >= numRows()) {
            return false;
        }
        return true;
    }

	public int numRows() {
		return pieces.length;
	}

	public int numCols() { return pieces[0].length; }

}

