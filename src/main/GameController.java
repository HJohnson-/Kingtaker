package main;

import BasicChess.King;
import ai.BasicAI;
import ai.ChessAI;
import ai.MinimaxAI;
import pieces.ChessPiece;
import pieces.PieceDecoder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Handles game logic
 */
public class GameController {
	private boolean isWhitesTurn = true; //white always starts
	private int currentTurn;
	private String winner;
	private boolean gameOver;
	private Board board;
	private String gameVariant;
	private PieceDecoder decoder;
    private ChessAI ai;
    public GameMode gameMode = GameMode.MULTIPLAYER_LOCAL;
    private boolean playerIsWhite = true;

	public Board getBoard() {
		return board;
	}

	/**
	 * @param board board
	 */
	public GameController(Board board, String gameVariant, PieceDecoder decoder, GameMode mode) {
        currentTurn = 1;
		this.board = board;
		winner = "None";
		gameOver = false;
		this.gameVariant = gameVariant;
		this.decoder = decoder;
        this.gameMode = mode;

        if (gameMode == GameMode.SINGLE_PLAYER) {
            ai = new MinimaxAI(board, false, 10);
        }
    }

	public GameController(Board board, PieceDecoder decoder, String code, GameMode mode) {
        this.board = board;
		winner = "None";
		gameOver = false;
		this.decoder = decoder;
		int startOfValue = 4;
		int endOfValue = code.indexOf('~', startOfValue);
		currentTurn = Integer.decode(code.substring(startOfValue, endOfValue));
		startOfValue = endOfValue+3;
		endOfValue = code.indexOf('$', startOfValue);
		gameVariant = code.substring(startOfValue, endOfValue);
		startOfValue = endOfValue+1;
		endOfValue = code.indexOf('#', startOfValue);
		String pieces = code.substring(startOfValue, endOfValue);
		board.populateFromCode(pieces, decoder);

        this.gameMode = mode;

        if (gameMode == GameMode.SINGLE_PLAYER) {
            ai = new MinimaxAI(board, false, 10);
        }
	}

	public Map<ChessPiece, List<Location>> getAllValidMoves(boolean whitePieces) {
		return getAllValidMoves(true, whitePieces);
	}

	/**
	 * @param caresAboutCheck false if we want to get moves that leave the mover in check, useful for seeing if any of those moves take the opposing king
	 * @return Map of pieces to the locations representing the moves they can attempt
	 */
	public Map<ChessPiece, List<Location>> getAllValidMoves(boolean caresAboutCheck, boolean whitePieces) {
		Map<ChessPiece, List<Location>> allPossibleMoves = new HashMap<ChessPiece, List<Location>>();
		for(ChessPiece piece : board.allPieces()) {
			if(piece.type != PieceType.EMPTY && piece.isWhite() == whitePieces) {
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
     * @param local true if the move was executed by a local user (via mouse click)
	 * @return if the move was successful and the game-state modified
	 */
	public boolean attemptMove(Location pieceLocation, Location targetLocation, boolean local) {
        //Cannot make moves once the game has ended.
        if (gameOver) return false;

		ChessPiece beingMoved = board.getPiece(pieceLocation);
		ChessPiece movedOnto = board.getPiece(targetLocation);

        //Cannot perform a move that violates the variant's rules.
        //Cannot move a black piece when it's white's turn, and vice versa.
		if (!beingMoved.isValidMove(targetLocation) || !turnPlayersPiece(beingMoved)) {
            return false;
        }

        //Cannot perform a move if the user is not white or black.
        //Does not apply to local multiplayer games, where fullInteractivity=true.
		if (!userCanInteractWithPiece(beingMoved, local)) {

			return false;
        }

        //Executes move, unless the piece... TODO: why is this a boolean? Does it always return true!
		//If checkmate is detected, the game ends, otherwise the active player is switched.
        //If the move was executed locally, it is sent to the remote player via launcher.
        if (beingMoved.executeMove(targetLocation)) {
            if (checkMate()) {
				endGame();
			} else {
                nextPlayersTurn();
            }

            if (local) {
                GameLauncher.currentGameLauncher.broadcastMove(pieceLocation, targetLocation, "");
            }

            if (!isWhitesTurn && gameMode == GameMode.SINGLE_PLAYER) {
                ai.getBestMove();
            }

			return true;
		}

        return false;
	}

    /**
     * Co-ordinates of move and optional extra field for pawn promotion.
     * Function is used to handle moves from a remote or AI user.
     */
    public boolean handleRemoteMove(int oldX, int oldY, int newX, int newY, String extra) {
        Location oldL = new Location(oldX, oldY);
        Location newL = new Location(newX, newY);

        return attemptMove(oldL, newL, false);
    }

	/**
	 * set the game state to over
	 */
	protected void endGame() {
		gameOver = true;
		winner = isWhitesTurn ? "White" : "Black";
	}

	/**
	 * @return true if turn player is in checkmate
	 */
	protected boolean checkMate() {
		Map<ChessPiece, List<Location>> moves = getAllValidMoves(!isWhitesTurn);
		for (Map.Entry<ChessPiece, List<Location>> entry : moves.entrySet()) {
			if (!entry.getValue().isEmpty()) {
                System.out.println(entry.getValue());
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
		return (checkedPiece.type == PieceType.WHITE) == isWhitesTurn;
	}

    /**
     * @param checkedPiece piece to check
     * @param localUser whether this move was attempted by the local user
     *                  (as opposed to a remote user, in which case
     * @return if the piece belongs to the interactive user.
     */
    private boolean userCanInteractWithPiece(ChessPiece checkedPiece, boolean localUser) {
        return gameMode == GameMode.MULTIPLAYER_LOCAL ||
                (checkedPiece.isWhite() == playerIsWhite)
                        == localUser;
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
		for(List<Location> targets : getAllValidMoves(false, !checkingForWhite).values()) {
			if(targets.contains(kingLocation)) {
				return true;
			}
		}
		return false;
	}

	public boolean isInCheck(PieceType type) {
		return isInCheck(type == PieceType.WHITE);
	}

	/**
	 * @param checkingForWhite if we're looking for white's king, otherwise looking for black
	 * @return the location of a king of that player. Multiple kings not handled
	 */
	public Location findKing(boolean checkingForWhite) {
		for(ChessPiece piece : board.allPieces()) {
			if (piece instanceof King && piece.isWhite() == checkingForWhite) {
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

	/**
	 * @return A string representation of the board
	 * Format is #$ followed by fields in the form X:<value> where X is a letter identifying the field and fields are
	 * separated by a ~. This is followed by a $, and then the code for each non-empty piece on the board. The string
	 * is terminated with a #.
	 * The fields are
	 * T: for the current Turn
	 * V: for the game Variant
	 * Variants can add any fields to this string as long as they also include something to decode them
	 */
	public String toCode() {
		StringBuilder code = new StringBuilder();
		code.append("#$").append("T:").append(currentTurn).append("~").append("V:").append(gameVariant).append("$");
		for(ChessPiece p : board.allPieces()) {
			code.append(p.toCode());
		}
		code.append("#");
		return code.toString();
	}

    @Override
    public GameController clone() {
        return new GameController(board, gameVariant, decoder, gameMode);
    }
}
