package main;

import ai.ChessAI;
import ai.MinimaxAI;
import pieces.PawnPromotion;
import pieces.PromotablePiece;
import pieces.ChessPiece;
import pieces.PieceDecoder;
import variants.BasicChess.Bishop;
import variants.BasicChess.King;
import variants.BasicChess.Knight;
import variants.BasicChess.Pawn;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles game logic
 */
public class GameController {
	protected boolean isWhitesTurn = true; //white always starts
	protected int currentTurn;
    protected GameResult gameResult = GameResult.IN_PROGRESS;
	private Board board;

	protected int gameID;
	public GameMode gameMode = GameMode.MULTIPLAYER_LOCAL;
	protected PieceDecoder decoder;

	protected ChessAI ai;
	private boolean AIWorking = false;
	public boolean playerIsWhite;
	public boolean animating = false;
	public boolean promoting = false;

    private ExecutorService executorForAI = Executors.newSingleThreadExecutor();
	private List<String> previousTurns;
    public long lastMoveTime = System.currentTimeMillis();

    public Board getBoard() {
		return board;
	}

    public void setBoard(Board board) {
        this.board = board;
    }

	public GameController() {}

	//Used for local multiplayer and single player games.
	public GameController(Board board, int gameID, PieceDecoder decoder, GameMode mode, boolean playerIsWhite) {
        currentTurn = 1;
		this.board = board;
		this.gameID = gameID;
		this.decoder = decoder;
        this.gameMode = mode;
		previousTurns = new ArrayList<String>();
		this.playerIsWhite = playerIsWhite;

		if (mode == GameMode.SINGLE_PLAYER) {
			initialiseAI();
		}
    }

    //Used exclusively by OnlineGameLauncher.
	public GameController(Board board, PieceDecoder decoder, String boardLayoutCode, GameMode mode) {
        this.board = board;
		this.decoder = decoder;
		int startOfValue = 4;
		int endOfValue = boardLayoutCode.indexOf('~', startOfValue);
		currentTurn = Integer.decode(boardLayoutCode.substring(startOfValue, endOfValue));
		startOfValue = endOfValue+3;
		endOfValue = boardLayoutCode.indexOf('$', startOfValue);
		gameID = Integer.decode(boardLayoutCode.substring(startOfValue, endOfValue));
		startOfValue = endOfValue+1;
		endOfValue = boardLayoutCode.indexOf('#', startOfValue);
		String pieces = boardLayoutCode.substring(startOfValue, endOfValue);
		board.populateFromCode(pieces, decoder);
        this.gameMode = mode;
		previousTurns = new ArrayList<String>();
	}

    public ChessAI getAI() {
        return ai;
    }

    public void initialiseAI(int difficulty) {
        abortAI();
        this.ai = new MinimaxAI(!playerIsWhite, difficulty);
    }

    public void initialiseAI() {
        abortAI();
        this.ai = new MinimaxAI(!playerIsWhite);
    }

    public void makeAIMove() {
        if ( (ai != null) && (isWhitesTurn != playerIsWhite)
                && (gameMode == GameMode.SINGLE_PLAYER)) {
            executorForAI.submit(new DoAIMove(this));
        }
    }

    private void abortAI() {
        executorForAI.shutdownNow(); //kill AI
        executorForAI = Executors.newSingleThreadExecutor();
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
        //Cannot make moves once the game has ended or waiting on other player to promote.
        if (gameResult != GameResult.IN_PROGRESS || promoting) return false;

		ChessPiece beingMoved = board.getPiece(pieceLocation);
		ChessPiece movedOnto = board.getPiece(targetLocation);

        //Cannot perform a move that violates the variant's rules.
        //Cannot move a black piece when it's white's turn, and vice versa.

		if (!beingMoved.isValidMove(targetLocation) || !turnPlayersPiece(beingMoved)) {
            System.out.println("Invalid move: against rules, or it is not this colours turn");
            return false;
        }

        //Cannot perform a move if the user is not white or black.
        //Does not apply to local multiplayer games, where fullInteractivity=true.
		if (!userCanInteractWithPiece(beingMoved, local)) {
            System.out.println("Invalid move: you cannot control this colour");
			return false;
        }


		if(local && gameMode != GameMode.MULTIPLAYER_ONLINE) {
			previousTurns.add(toCode());
		}
        //Executes move, unless the piece has a final objection (useful for some variants)
		//If checkmate is detected, the game ends, otherwise the active player is switched.
        //If the move was executed locally, it is sent to the remote player via launcher.
        if (beingMoved.executeMove(targetLocation)) {
            if (checkMate()) {
				endGame(false);
			} else if (staleMate()) {
				endGame(true);
			} else {
				checkForCapturedPieces(targetLocation); //No effect if not Hf
                nextPlayersTurn();
            }

            if (local && gameMode == GameMode.MULTIPLAYER_ONLINE) {
                GameLauncher.currentGameLauncher.broadcastMove(pieceLocation, targetLocation, "");
            }

            lastMoveTime = System.currentTimeMillis();

            makeAIMove(); //No effect if not single player.

			return true;
		}

        return false;
	}

	protected void checkForCapturedPieces(Location targetLocation) {
	}

	protected boolean staleMate(){
		Map<ChessPiece, List<Location>> moves = getAllValidMoves(isWhitesTurn);
		LinkedList<ChessPiece> chessPieces = board.allPieces();
		return moves.size() == 0 || isCheckMateImpossible(chessPieces) || fiftyMoves(chessPieces) || threefoldRepetition();
	}

	//true if no capture has been made and no pawn has been moved in the last fifty moves
	//a move consisting of both players completing their turn
	private boolean fiftyMoves(LinkedList<ChessPiece> chessPieces){

		if(currentTurn < 100) return false;
		int size = chessPieces.size();
		boolean noCaptures = true;
		boolean noPawnMoved = true;

		List<String> boardPositions = previousTurns;
		boardPositions.add(toCode()); //adding board state after current turn


		//getting pawns and their state on the board
		String[] nextStateSplit = toCode().split("\\|");
		ArrayList<String> next = new ArrayList<String>();
		for(String v : nextStateSplit){
			if(v.startsWith("N:Pawn")) next.add(v);
		}
		HashSet<String> pawns = new HashSet<String>();
		for(String pawn : next){
			pawns.add(pawn.substring(0,22));
		}


		for(int i = boardPositions.size() - 1; i >= boardPositions.size() - 100 ; --i){
			String boardState = boardPositions.get(i);

			String[] ts = boardState.split("\\|");
			ArrayList<String> pieces = new ArrayList<String>();
			for(String v : ts){
				if(v.startsWith("N")) pieces.add(v);
			}

			noCaptures = noCaptures && size == pieces.size();
			if(!noCaptures) return false;


			for(String piece : pieces){
				if(piece.startsWith("N:Pawn")){
					noPawnMoved = noPawnMoved && pawns.contains(piece.substring(0,22));
					if(!noPawnMoved) return false;
				}
			}



		}

		return noCaptures && noPawnMoved;

	}

	//true if the same position occurs three time, no progress is being made
	private boolean threefoldRepetition(){
		HashMap<String, Integer> occurrences = new HashMap<String, Integer>();

		for(String position : previousTurns){

			if(occurrences.containsKey(position)){
				occurrences.put(position, occurrences.get(position) + 1);
			}else {
				occurrences.put(position, 1);
			}
		}

		for(String position : occurrences.keySet()){
			if(occurrences.get(position) >= 3) return true;
		}

		return false;
	}

	private boolean isCheckMateImpossible(LinkedList<ChessPiece> chessPieces){


		if(chessPieces.size() == 2){
			if(chessPieces.get(0) instanceof King &&
			   chessPieces.get(1) instanceof King ){
				return true;
			}
		}

		if(chessPieces.size() == 3){
			int kings = 0;
			int bishop = 0;
			int knight = 0;
			for(ChessPiece piece : chessPieces){
				if(piece instanceof King) ++kings;
				else if(piece instanceof Knight) ++knight;
				else if(piece instanceof Bishop) ++bishop;
			}

			return kings == 2 && (bishop == 1 || knight == 1);
		}

		if(chessPieces.size() == 4){
			int kings = 0;
			int bishops = 0;
			int whiteBishop = 0;

			LinkedList<Location> bishopLocations = new LinkedList<Location>();

			for(ChessPiece piece : chessPieces){
				if(piece instanceof King) ++kings;
				else if(piece instanceof Bishop){
					++bishops;
					bishopLocations.add(piece.cords);
					if(piece.type == PieceType.WHITE) ++whiteBishop;
				}
			}

			boolean bishopsOnSameColour;
			if(bishopLocations.size() != 2) return false;
			else{
				Location loc1 = bishopLocations.get(0);
				Location loc2 = bishopLocations.get(1);

				bishopsOnSameColour = (loc1.getX() % 2 == loc2.getX() % 2 &&
						       loc1.getY() % 2 == loc2.getY() % 2) ||
						       (loc1.getX() % 2 != loc2.getX() % 2 &&
							    loc1.getY() % 2 != loc2.getY() % 2);
			}


			return kings == 2 && bishops == 2 && whiteBishop == 1 && bishopsOnSameColour ;
		}

		return  false;
	}

	public void undo() {
		int size = previousTurns.size();
		if(size < 1) {
			return;
		}
		String code = previousTurns.remove(size - 1);
		if (code == null) {
			return;
		}
		load(code);
	}

    /**
     * Co-ordinates of move and optional extra field for pawn promotion.
     * Function is used to handle moves from a remote or AI user.
     * Returns whether the move was accepted by the game controller.
     */
    public boolean handleRemoteMove(int oldX, int oldY, int newX, int newY, String extra) {
        Location oldL = new Location(oldX, oldY);
        Location newL = new Location(newX, newY);

        if (oldL.equals(newL) && extra != null) {
            PromotablePiece promotablePiece;
            //Attempt to parse extra as a pawn promotion argument
			promotablePiece = PromotablePiece.values()[Integer.parseInt(extra)];
			PawnPromotion pawnPromotion = new PawnPromotion(board.getPiece(oldL));
			pawnPromotion.promote(promotablePiece);
            return true;
        } else {
            return attemptMove(oldL, newL, false);
        }
    }

	/**
	 * set the game state to over
	 */
	public void endGame(boolean isADraw) {
		if(!isADraw)
        gameResult = isWhitesTurn ? GameResult.WHITE_WIN : GameResult.WHITE_LOSS;

		else gameResult = GameResult.DRAW;

        if (gameMode == GameMode.MULTIPLAYER_ONLINE) {
            GameLauncher.currentGameLauncher.broadcastEndGame();
        }
	}

	/**
	 * @return true if turn player is in checkmate
	 */
	public boolean checkMate() {
		Map<ChessPiece, List<Location>> moves = getAllValidMoves(!isWhitesTurn);
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

    public GameResult getResult() {
        return gameResult;
    }

	/**
	 * advances the turn,changes the turn player
	 */
	public void nextPlayersTurn() {
		currentTurn++;
        isWhitesTurn = !isWhitesTurn;
	}

	public void previousTurn() {
		currentTurn--;
		isWhitesTurn = !isWhitesTurn;
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
		code.append("#$").append("T:").append(currentTurn).append("~").append("V:").append(gameID).append("$");
		for(ChessPiece p : board.allPieces()) {
			code.append(p.toCode());
		}
		code.append("#");
		return code.toString();
	}

    @Override
    public GameController clone() {
        GameController newGame = new GameController(null, gameID, decoder, gameMode, playerIsWhite);
		newGame.gameID = gameID;
		newGame.decoder = decoder;
		newGame.gameMode = gameMode;
		newGame.playerIsWhite = playerIsWhite;
        newGame.isWhitesTurn = isWhitesTurn;
        newGame.currentTurn = currentTurn;
        newGame.gameResult = gameResult;
        newGame.ai = null;
        return newGame;
    }

	public void load(String code) {
        abortAI();
        int startOfValue = 4;
		int endOfValue = code.indexOf('~', startOfValue);
		currentTurn = Integer.decode(code.substring(startOfValue, endOfValue));
		isWhitesTurn = currentTurn % 2 == 1;
		startOfValue = endOfValue+3;
		endOfValue = code.indexOf('$', startOfValue);
		gameID = Integer.decode(code.substring(startOfValue, endOfValue));
		startOfValue = endOfValue+1;
		endOfValue = code.indexOf('#', startOfValue);
		String pieces = code.substring(startOfValue, endOfValue);
		board.populateFromCode(pieces, decoder);
	}

    //Wait until the AI has moved before recreating it with a new difficulty.
    public void setDifficulty(int difficulty) {
        abortAI();
        initialiseAI(difficulty);
        if (!isLocalsTurn()) {
            makeAIMove();
        }
    }

    public boolean isLocalsTurn() {
        return gameMode == GameMode.MULTIPLAYER_LOCAL || playerIsWhite == isWhitesTurn;
    }

    class DoAIMove implements Runnable {

        private GameController control;

        public DoAIMove(GameController control) {
            this.control = control;
        }

        @Override
        public void run() {
			while (control.animating || control.promoting) Thread.yield();
            AIWorking = true;
            Location[] aiMove = new Location[0];

            try {
                aiMove = control.ai.getBestMove(control.board);
            } catch (InterruptedException e) {
                System.out.println("AI Interrupted");
                ai.reset();
                return;
            }

            control.attemptMove(aiMove[0], aiMove[1], false);
			ChessPiece movedPiece = control.board.getPiece(aiMove[1]);
			if (movedPiece instanceof Pawn && (movedPiece.cords.getX() == 0 || movedPiece.cords.getX() == control.board.numCols() - 1)) {
				PawnPromotion pp = new PawnPromotion(movedPiece);
				pp.promote(PromotablePiece.QUEEN);
			}
            //System.out.println(aiMove[0] + " -> " + aiMove[1]);
            AIWorking = false;
        }

    }
}
