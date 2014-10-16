package main;

import BasicChess.King;
import pieces.ChessPiece;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by hj1012 on 15/10/14.
 */
abstract public class Board {
	private pieces.ChessPiece[][] pieces;
	private boolean isWhitesTurn;
	private int currentTurn;

	public int getCurrentTurn() {
		return currentTurn;
	}

    public Board() {
        pieces = new ChessPiece[8][8];
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

	//Ignores everything and moves a piece. For testing purposes only!!!
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
			toPlace.lastTurnMovedOn = currentTurn;
			return true;
		} catch (Error e) {
			return false;
		}
	}

	//true if the space contains an EmptyPiece;
	public boolean isEmptySpace(Location targetLocation) {
		return getPiece(targetLocation).type == PieceType.EMPTY;
	}

	//replaces the Piece on the location with an EmptyPiece;
	public void clearSpace(Location pieceLocation) {
		placePiece(pieceLocation, new pieces.EmptyPiece(this, pieceLocation));
	}

	//Returns true if the targetLocation is within the confines of the board.
	private boolean onBoard(Location targetLocation) {
		if(targetLocation.getX() < 0 || targetLocation.getY() < 0){
			return false;
		}
		if(targetLocation.getX() >= pieces.length || targetLocation.getY() >= pieces[0].length) {
			return false;
		}
		return true;
	}

	public int numRows() {
		return pieces.length;
	}

	public int numCols() { return pieces[0].length; }

	public Map<Location, List<Location>> getAllValidMoves() {
		Map<Location, List<Location>> allPossibleMoves = new HashMap<Location, List<Location>>();
		for(int x = 0; x < numCols(); x++) {
			for(int y = 0; y < numRows(); y++) {
				Location testSpace = new Location(x, y);
				PieceType testType = getPiece(testSpace).type;
				if(testType != PieceType.EMPTY && (testType == PieceType.WHITE) == isWhitesTurn) {
					allPossibleMoves.put(testSpace, movesForPiece(testSpace));
				}
			}
		}
		return allPossibleMoves;
	}

	public List<Location> movesForPiece(Location from) {
		List<Location> possibleMoves = new LinkedList<Location>();
		ChessPiece beingMoved = getPiece(from);
		for(int x = 0; x < numCols(); x++) {
			for(int y = 0; y < numRows(); y++) {
				Location testSpace = new Location(x, y);
				if(beingMoved.isValidMove(testSpace)) {
					possibleMoves.add(testSpace);
				}
			}
		}
		return possibleMoves;
	}


	//Stores the pieces at the square moved to or from, checks the move is valid, attempts the move, If the move would
	//put the turn player in check, crudely undoes the move. Does not work for En Passant /Castling that would put the
	//turn player in check. TODO make this use the getAllValidMoves function and not break on weird moves.
	public boolean attemptMove(Location pieceLocation, Location targetLocation) {
		ChessPiece beingMoved = getPiece(pieceLocation);
		ChessPiece movedOnto = getPiece(targetLocation);
		if(!beingMoved.isValidMove(targetLocation)) {
			throw new Error("Invalid move");
		}
		if(!turnPlayersPiece(beingMoved)) {
			throw new Error("Wrong colour piece!");
		}
		if(beingMoved.executeMove(targetLocation)) {
			nextPlayersTurn();
			return true;
		} else {
			return false;
		}
	}

	private void nextPlayersTurn() {
		isWhitesTurn = !isWhitesTurn;
	}

	private boolean turnPlayersPiece(ChessPiece checkedPiece) {
		return checkedPiece.type == PieceType.WHITE != isWhitesTurn;
	}

	public boolean isKing(Location checking) {
		return getPiece(checking) instanceof King;
	}

	//TODO This. Required to stop players putting themselves in check.
	public boolean isInCheck(boolean checkingForWhite) {
		return false;
	}
}