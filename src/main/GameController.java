package main;

import BasicChess.King;
import pieces.ChessPiece;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by hj1012 on 23/10/14.
 */
public class GameController {
	private boolean isWhitesTurn;
	private int currentTurn;
	private String winner;
	private boolean gameOver;
	private Board board;

	public Board getBoard() {
		return board;
	}

	public GameController(Board board) {
		currentTurn = 1;
		this.board = board;
		winner = "None";
		gameOver = false;
	}

	public Map<ChessPiece, List<Location>> getAllValidMoves() {
		return getAllValidMoves(true);
	}

	public Map<ChessPiece, List<Location>> getAllValidMoves(boolean caresAboutCheck) {
		Map<ChessPiece, List<Location>> allPossibleMoves = new HashMap<ChessPiece, List<Location>>();
		for(ChessPiece piece : board.allPieces()) {
			PieceType testType = piece.type;
			if(testType != PieceType.EMPTY && (testType == PieceType.WHITE) == isWhitesTurn) {
				allPossibleMoves.put(piece, movesForPiece(piece, caresAboutCheck));
			}
		}
		return allPossibleMoves;
	}

	public List<Location> movesForPiece(ChessPiece piece, boolean caresAboutCheck) {
		List<Location> allMoves = piece.allUnblockedMoves();
		List<Location> validMoves = new LinkedList<Location>();
		for (Location l : allMoves) {
			if (piece.isValidMove(l, caresAboutCheck)) {
				validMoves.add(l);
			}
		}
		return validMoves;
	}

	//Stores the pieces at the square moved to or from, checks the move is valid, attempts the move, If the move would
	//put the turn player in check, crudely undoes the move. Does not work for En Passant /Castling that would put the
	//turn player in check.
	public boolean attemptMove(Location pieceLocation, Location targetLocation) {
		if (gameOver) return false;
		ChessPiece beingMoved = board.getPiece(pieceLocation);
		ChessPiece movedOnto = board.getPiece(targetLocation);
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

	private boolean turnPlayersPiece(ChessPiece checkedPiece) {
		return checkedPiece.type == PieceType.WHITE != isWhitesTurn;
	}

	public boolean isKing(Location checking) {
		return board.getPiece(checking) instanceof King;
	}

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
		for(ChessPiece piece : board.allPieces()) {
			if(piece instanceof King && piece.type == type) {
				return piece.cords;
			}
		}
		throw new Error("No king on board");
	}

	public boolean isInCheck(PieceType type) {
		return isInCheck(type == PieceType.WHITE);
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public boolean isWhitesTurn() {
		return isWhitesTurn;
	}

	public String getWinner() {
		return winner;
	}

	private void nextPlayersTurn() {
		currentTurn++;
		isWhitesTurn = !isWhitesTurn;
	}

	public boolean gameOver() {
		return gameOver;
	}
}
