package main;

import BasicChess.King;
import pieces.ChessPiece;

import java.util.*;

/**
 * Created by hj1012 on 15/10/14.
 */
abstract public class Board {
	private pieces.ChessPiece[][] pieces;
	private boolean isWhitesTurn;
	private int currentTurn;
    private String winner;
    private boolean gameOver;

	public int getCurrentTurn() {
		return currentTurn;
	}

    public boolean isWhitesTurn() {
        return isWhitesTurn;
    }

    public String getWinner() {
        return winner;
    }

    public boolean gameOver() {
        return gameOver;
    }

    public Board() {
        pieces = new ChessPiece[8][8];
		currentTurn = 1;
        winner = "None";
        gameOver = false;
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

	public Map<ChessPiece, List<Location>> getAllValidMoves() {
		return getAllValidMoves(true);
	}

	public Map<ChessPiece, List<Location>> getAllValidMoves(boolean caresAboutCheck) {
		Map<ChessPiece, List<Location>> allPossibleMoves = new HashMap<ChessPiece, List<Location>>();
		for(ChessPiece piece : allPieces()) {
			PieceType testType = piece.type;
			if(testType != PieceType.EMPTY && (testType == PieceType.WHITE) == isWhitesTurn) {
				allPossibleMoves.put(piece, movesForPiece(piece, caresAboutCheck));
			}
		}
		return allPossibleMoves;
	}


	public List<Location> movesForPiece(ChessPiece piece, boolean caresAboutCheck) {
		List<Location> possibleMoves = new LinkedList<Location>();
		for(int x = 0; x < numCols(); x++) {
			for(int y = 0; y < numRows(); y++) {
				Location testSpace = new Location(x, y);
				if(piece.isValidMove(testSpace, caresAboutCheck)) {
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
        if (gameOver) return false;
		ChessPiece beingMoved = getPiece(pieceLocation);
		ChessPiece movedOnto = getPiece(targetLocation);
		if(!beingMoved.isValidMove(targetLocation)) {
			return false;
		}
		if(!turnPlayersPiece(beingMoved)) {
			return false;
		}
		if(beingMoved.executeMove(targetLocation)) {
            nextPlayersTurn();
            if (checkMate()) {
                endGame();
            }
			return true;
		} else {
			return false;
		}
	}

    protected void endGame() {
        gameOver = true;
        winner = isWhitesTurn ? "White" : "Black";
    }

	protected boolean checkMate() {
		Map<?, List<Location>> moves = getAllValidMoves();
		for (Map.Entry<?, List<Location>> entry : moves.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                return false;
            }
		}
		return true;
	}

	private void nextPlayersTurn() {
        currentTurn++;
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
		Location kingLocation = findKing(checkingForWhite);
		for(List<Location> targets : getAllValidMoves(false).values()) {
			if(targets.contains(kingLocation)) {
				return true;
			}
		}
		return false;
	}

	private Location findKing(boolean checkingForWhite) {
		PieceType type = checkingForWhite ? PieceType.WHITE : PieceType.BLACK;
		for(ChessPiece piece : allPieces()) {
			if(piece instanceof King && piece.type == type) {
				return piece.cords;
			}
		}
		throw new Error("No king on board");
	}

	public boolean isInCheck(PieceType type) {
		return isInCheck(type == PieceType.WHITE);
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
}