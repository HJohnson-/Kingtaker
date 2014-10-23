package main;

import BasicChess.King;
import pieces.ChessPiece;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Controls game logic that does not fit on the board.
 */
public class GameController {
	private boolean isWhitesTurn;
	private int currentTurn;
	private String winner;
	private boolean gameOver;
	private Board board;

	/**
	 * @return board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @param board board
	 */
	public GameController(Board board) {
		currentTurn = 1;
		this.board = board;
		winner = "None";
		gameOver = false;
	}

	/**
	 * @return map of piece to list of locations the piece can move to.
	 */
	public Map<ChessPiece, List<Location>> getAllValidMoves() {
		return getAllValidMoves(true);
	}

	/**
	 * @param caresAboutCheck if we are allowing pieces to move themselves into Check. Used to see if game ends.
	 * @return map of piece to list of locations the piece can move to.
	 */
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

	/**
	 * @param piece piece to get moves for
	 * @param caresAboutCheck if we allow the piece to move self into check to take opposed king
	 * @return moves piece can make
	 */
	public List<Location> movesForPiece(ChessPiece piece, boolean caresAboutCheck) {
		List<Location> allMoves = piece.allPieceMoves();
		List<Location> validMoves = new LinkedList<Location>();
		for (Location l : allMoves) {
			if (piece.isValidMove(l, caresAboutCheck)) {
				validMoves.add(l);
			}
		}
		return validMoves;
	}

	/**
	 * @param pieceLocation location of piece attempting to be moved
	 * @param targetLocation location piece is trying to move to
	 * @return if move succeeded
	 */
	public boolean attemptMove(Location pieceLocation, Location targetLocation) {
		if (gameOver) return false;
		ChessPiece beingMoved = board.getPiece(pieceLocation);
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


	/**
	 * sets the game to be over;
	 */
	protected void endGame() {
		gameOver = true;
		winner = isWhitesTurn ? "White" : "Black";
	}

	/**
	 * @return True if the turn player is in checkmate
	 */
	protected boolean checkMate() {
		Map<?, List<Location>> moves = getAllValidMoves();
		for (Map.Entry<?, List<Location>> entry : moves.entrySet()) {
			if (!entry.getValue().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param checkedPiece piece being queried
	 * @return if the piece is the turn player's
	 */
	private boolean turnPlayersPiece(ChessPiece checkedPiece) {
		return checkedPiece.type == PieceType.WHITE != isWhitesTurn;
	}

	/**
	 * @param checking a piece
	 * @return if the piece is a king
	 */
	public boolean isKing(Location checking) {
		return board.getPiece(checking) instanceof King;
	}

	/**
	 * @param checkingForWhite if we're looking for white's king, otherwise black
	 * @return the location of the selected player's king. If multiple kings: first found. If no king: throw error.
	 */
	private Location findKing(boolean checkingForWhite) {
		PieceType type = checkingForWhite ? PieceType.WHITE : PieceType.BLACK;
		for(ChessPiece piece : board.allPieces()) {
			if(piece instanceof King && piece.type == type) {
				return piece.cords;
			}
		}
		throw new Error("No king on board");
	}

	/**
	 * @param type colour of the player being checked for
	 * @return if said player is in check.
	 */
	public boolean isInCheck(PieceType type) {
		return isInCheck(type == PieceType.WHITE);
	}


	/**
	 * @param checkingForWhite if we're checking that white is in check, otherwise for black.
	 * @return if said player is in check.
	 */
	public boolean isInCheck(boolean checkingForWhite) {
		Location kingLocation = findKing(checkingForWhite);
		for(List<Location> targets : getAllValidMoves(false).values()) {
			if(targets.contains(kingLocation)) {
				return true;
			}
		}
		return false;
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

	/**
	 * advances turn count and changes the turn player.
	 */
	private void nextPlayersTurn() {
		currentTurn++;
		isWhitesTurn = !isWhitesTurn;
	}

	public boolean gameOver() {
		return gameOver;
	}
}
