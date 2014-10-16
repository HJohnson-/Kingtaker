package main;

import pieces.ChessPiece;

import java.util.LinkedList;

/**
 * Created by hj1012 on 15/10/14.
 */
abstract public class Board {
	private pieces.ChessPiece[][] pieces;
	private boolean isWhitesTurn;

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

	//Places the given piece at targetLocation -important for initializing the board where pieces must be placed without
	//being on the board previously.
	public boolean placePiece(Location targetLocation, pieces.ChessPiece toPlace) {
		try {
			pieces[targetLocation.getX()][targetLocation.getY()] = toPlace;
			return true;
		} catch (Error e) {
			return false;
		}
	}

	//Assumes move is valid
	public boolean executeMove(Location pieceLocation, Location targetLocation) {
		try {
			pieces.ChessPiece beingMoved = pieces[pieceLocation.getX()][pieceLocation.getY()];
			clearSpace(pieceLocation);
			placePiece(targetLocation, beingMoved);
			return true;
		} catch (Error e) {
			System.out.println(e);
			return false;
		}

	}

	//true if the space contains an EmptyPiece;
	public boolean isEmptySpace(Location targetLocation) {
		return getPiece(targetLocation).type == PieceType.EMPTY;
	}

	//replaces the Piece on the location with an EmptyPiece;
	private void clearSpace(Location pieceLocation) {
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

	/*TODO make a function to generate all valid moves so an AI can look over them. (low priority, but would make move validation easy)*/
	//public Moves getAllValidMoves(){}


	//Stores the pieces at the square moved to or from, checks the move is valid, attempts the move, If the move would
	//put the turn player in check, crudely undoes the move. Does not work for En Passant /Castling that would put the
	//turn player in check. TODO make this use the getAllValidMoves function and not break on weird moves.
	public boolean attemptMove(Location pieceLocation, Location targetLocation) {
		ChessPiece beingMoved = getPiece(pieceLocation);
		ChessPiece movedOnto = getPiece(targetLocation);
		if(!beingMoved.isValidMove(pieceLocation, targetLocation)) {
			throw new Error("Invalid move");
		}
		if(!turnPlayersPiece(beingMoved)) {
			throw new Error("Wrong colour piece!");
		}
		executeMove(pieceLocation, targetLocation);
		if(isInCheck(isWhitesTurn)) {
			placePiece(pieceLocation, beingMoved);
			placePiece(targetLocation, movedOnto);
			throw new Error("Cannot move self into check!");
		} else {
			nextPlayersTurn();
			return true;
		}
	}

	private void nextPlayersTurn() {
		isWhitesTurn = !isWhitesTurn;
	}

	private boolean turnPlayersPiece(ChessPiece checkedPiece) {
		return checkedPiece.type == PieceType.WHITE != isWhitesTurn;
	}

	//TODO This. Required to stop players putting themselves in check.
	private boolean isInCheck(boolean checkingForWhite) {
		return false;
	}
}