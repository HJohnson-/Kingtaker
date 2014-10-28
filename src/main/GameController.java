package main;

import BasicChess.King;
import pieces.ChessPiece;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Handles game logic
 */
public class GameController {
	private boolean isWhitesTurn;
	private int currentTurn;
	private String winner;
	private boolean gameOver;
	private Board board;
	private String gameType;

	public Board getBoard() {
		return board;
	}

	/**
	 * @param board board
	 */
	public GameController(Board board, String gameType) {
		currentTurn = 1;
		this.board = board;
		winner = "None";
		gameOver = false;
		this.gameType = gameType;
	}

	public Map<ChessPiece, List<Location>> getAllValidMoves() {
		return getAllValidMoves(true);
	}

	/**
	 * @param caresAboutCheck false if we want to get moves that leave the mover in check, useful for seeing if any of those moves take the opposing king
	 * @return Map of pieces to the locations representing the moves they can attempt
	 */
	public Map<ChessPiece, List<Location>> getAllValidMoves(boolean caresAboutCheck) {
		Map<ChessPiece, List<Location>> allPossibleMoves = new HashMap<ChessPiece, List<Location>>();
		for(ChessPiece piece : board.allPieces()) {
			if(piece.type != PieceType.EMPTY && piece.isWhite() == isWhitesTurn) {
				allPossibleMoves.put(piece, movesForPiece(piece, caresAboutCheck));
			}
		}
		return allPossibleMoves;
	}

	/**
	 * @param piece a piece to get moves for
	 * @param caresAboutCheck false if we want to get moves that leave the mover in check
	 * @return list of locations given piece can move to
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
	 * @param pieceLocation location of the piece you are attempting to move
	 * @param targetLocation location representing the move
	 * @return if the move was successful and the game-state modified
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
			if (checkMate()) {
				endGame();
			} else {
                nextPlayersTurn();
            }
			return true;
		} else {
			return false;
		}
	}

	/**
	 * set the game state to over
	 */
	protected void endGame() {
		gameOver = true;
		winner = isWhitesTurn ? "Black" : "White";
	}

	/**
	 * @return true if turn player is in checkmate
	 */
	protected boolean checkMate() {
		Map<ChessPiece, List<Location>> moves = getAllValidMoves();
		for (Map.Entry<ChessPiece, List<Location>> entry : moves.entrySet()) {
			if (!entry.getValue().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param checkedPiece piece to check
	 * @return if the piece belongs to the turn player
	 */
	private boolean turnPlayersPiece(ChessPiece checkedPiece) {
		return checkedPiece.type == PieceType.WHITE != isWhitesTurn;
	}

	/**
	 * @param checking a location
	 * @return if there's a king on the location
	 */
	public boolean isKing(Location checking) {
		return board.getPiece(checking) instanceof King;
	}

	/**
	 * @param checkingForWhite true if we're checking that White is in check, false if checking for Black
	 * @return if that player's in check
	 */
	public boolean isInCheck(boolean checkingForWhite) {
		Location kingLocation = findKing(checkingForWhite);
        Boolean oldVal = isWhitesTurn;
        isWhitesTurn = !checkingForWhite;
		for(List<Location> targets : getAllValidMoves(false).values()) {
			if(targets.contains(kingLocation)) {
                isWhitesTurn = oldVal;
				return true;
			}
		}
        isWhitesTurn = oldVal;
		return false;
	}

	public boolean isInCheck(PieceType type) {
		return isInCheck(type == PieceType.WHITE);
	}

	/**
	 * @param checkingForWhite if we're looking for white's king, otherwise looking for black
	 * @return the location of a king of that player. Multiple kings not handled
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
	 * advances the turn,changes the turn player
	 */
	private void nextPlayersTurn() {
		currentTurn++;
		isWhitesTurn = !isWhitesTurn;
	}

	public boolean gameOver() {
		return gameOver;
	}
}
