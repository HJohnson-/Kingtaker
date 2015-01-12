package variants.Hnefatafl;

import main.*;
import pieces.ChessPiece;
import pieces.EmptyPiece;
import pieces.PieceDecoder;

import java.util.ArrayList;

/**
 * Created by crix9 on 03/12/2014.
 */
public class HnefataflController extends GameController {
	public HnefataflController(Board board, int gameID, PieceDecoder decoder, GameMode mode, boolean playerIsWhite) {
		super(board, gameID, decoder, mode, playerIsWhite);
		isWhitesTurn = false;
	}

	public HnefataflController(Board board, PieceDecoder decoder, String boardLayoutCode, GameMode mode) {
		super(board, decoder, boardLayoutCode, mode);
		isWhitesTurn = false;
	}

	@Override
	public GameController getOnlineController(Board board, PieceDecoder decoder, String boardState, GameMode multiplayerOnline) {
		return new HnefataflController(board, decoder, boardState, multiplayerOnline);
	}

	@Override
	public GameController clone() {
		HnefataflController newGame = new HnefataflController(null, gameID, decoder, gameMode, playerIsWhite);
		newGame.isWhitesTurn = this.isWhitesTurn;
		newGame.currentTurn = this.currentTurn;
		newGame.gameResult = this.gameResult;
		newGame.ai = null;
		return newGame;
	}

	//Check whether any opponent pieces has been captured by the move before swapping turns
	@Override
	protected void checkForCapturedPieces(Location location) {

		PieceType currentPlayerType = isWhitesTurn() ? PieceType.WHITE : PieceType.BLACK;


		//Check whether move results in a capture of opponent's piece

		//check row above
		if(location.getX() != 0) {//if not top row
			Location loc = new Location(location.getX()-1,location.getY());
			ChessPiece toPiece = getBoard().getPiece(loc);
			PieceType toPieceType = toPiece.type;

			if(isEnemyPiece(currentPlayerType,toPieceType) &&
					location.getX()-1 != 0 && // cant capture this piece as it's not between two
					!(toPiece instanceof HnefataflKing)) { //cannot take king if not surrounded in all 4 parts

				Location loc2 = new Location(location.getX()-2,location.getY());
				ChessPiece pieceUp2 = getBoard().getPiece(loc2);

				if((currentPlayerType == pieceUp2.type) ||
						(loc2.getX()  == 0 && (loc2.getY() == 0 || loc2.getY() == 10)) || //corners are hostile and can help in capturing other pieces
						(loc2.getX()  == 5 && loc2.getY()  == 5)) {  //throne can also be hostile
					getBoard().placePiece(loc, new EmptyPiece(getBoard(),null)); //piece has been captured, remove it
				}
			}
		}
		//check down
		if(location.getX() != 10) {//if not moving to bottom row
			Location loc = new Location(location.getX()+1,location.getY());
			ChessPiece toPiece = getBoard().getPiece(loc);
			PieceType toPieceType = toPiece.type;

			if(isEnemyPiece(currentPlayerType, toPieceType) &&
					location.getX()+1 != 10 &&
					!(toPiece instanceof HnefataflKing)) {

				Location loc2 = new Location(location.getX()+2,location.getY());
				ChessPiece pieceUp2 = getBoard().getPiece(loc2);

				if((currentPlayerType == pieceUp2.type) ||
						(loc2.getX()  == 10 && (loc2.getY() == 0 || loc2.getY() == 10)) || //corners are hostile and can help in capturing other pieces
						(loc2.getX()  == 5 && loc2.getY()  == 5)) {  //throne can also be hostile
					getBoard().placePiece(loc, new EmptyPiece(getBoard(),null)); //piece has been captured, remove it
				}
			}
		}
		//check left
		if(location.getY() != 0) {//if not left col
			Location loc = new Location(location.getX(),location.getY()-1);
			ChessPiece toPiece = getBoard().getPiece(loc);
			PieceType toPieceType = toPiece.type;


			if(isEnemyPiece(currentPlayerType, toPieceType) &&
					location.getY()-1 != 0 &&
					!(toPiece instanceof HnefataflKing)) {

				Location loc2 = new Location(location.getX(),location.getY()-2);
				ChessPiece pieceUp2 = getBoard().getPiece(loc2);

				if(currentPlayerType == pieceUp2.type ||
						(loc2.getY() == 0 && (loc2.getX() == 0 || loc2.getX() == 10)) ||
						(loc2.getX()  == 5 && loc2.getY()  == 5)) {

					getBoard().placePiece(loc, new EmptyPiece(getBoard(),null));
				}
			}
		}
		//check right
		if(location.getY() != 10) {//if not right col

			Location loc = new Location(location.getX(),location.getY()+1);
			ChessPiece toPiece = getBoard().getPiece(loc);
			PieceType toPieceType = toPiece.type;

			if(isEnemyPiece(currentPlayerType, toPieceType) &&
					loc.getY() != 10 &&
					!(toPiece instanceof HnefataflKing)) {

				Location loc2 = new Location(location.getX(),location.getY()+2);
				ChessPiece pieceUp2 = getBoard().getPiece(loc2);

				if(currentPlayerType == pieceUp2.type ||
						(loc2.getY() == 10 && (loc.getX() == 0 || loc2.getX() == 10)) ||
						(loc2.getX()  == 5 && loc2.getY()  == 5)) {
					getBoard().placePiece(loc, new EmptyPiece(getBoard(),null));
				}
			}
		}
	}


	//check if pieces are enemies
	boolean isEnemyPiece(PieceType curr, PieceType opp) {
		return (curr == PieceType.WHITE && opp == PieceType.BLACK) ||
				(opp == PieceType.WHITE && curr == PieceType.BLACK);
	}

	//does the current player have no legal moves
	@Override
	protected boolean staleMate(){
		PieceType type = isWhitesTurn() ? PieceType.WHITE : PieceType.BLACK;
		Move[] legalMoves = getPlayerLegalMoves(type);

		return type == null || legalMoves.length == 0;
	}

	//this methods checks if one of the player has won,
	// the king is either in one of the corner or has been captured
	@Override
	public boolean checkMate(){

		return checkWhiteWins() || checkBlackWins() ;
	}


	//WHITE wins if the king is one of the 4 corners
	boolean checkWhiteWins() {
		Location loc = new Location(0,0);
		ChessPiece topLeftPiece = getBoard().getPiece(loc);

		loc = new Location(0,10);
		ChessPiece topRightPiece = getBoard().getPiece(loc);

		loc = new Location(10,0);
		ChessPiece bottomLeftPiece = getBoard().getPiece(loc);

		loc = new Location(10,10);
		ChessPiece bottomRightPiece = getBoard().getPiece(loc);

		return (topLeftPiece instanceof HnefataflKing) || (topRightPiece instanceof HnefataflKing)
				|| (bottomLeftPiece instanceof HnefataflKing) || (bottomRightPiece instanceof HnefataflKing);
	}


	private Location getKingLocation(){
		for(int row = 0; row < getBoard().numRows(); row++){
			for (int col = 0; col < getBoard().numCols(); col++){
				Location loc = new Location(row, col);
				if(getBoard().getPiece(loc) instanceof HnefataflKing){
					return loc;
				}
			}
		}
		return null; //should not get here, there should always be a king on the board
	}

	//BLACK wins if they surround the white king (corners and central spot are hostile and can act as black soldiers)
	boolean checkBlackWins() {

		Location kingLocation = getKingLocation();
		int counter=0;
		boolean isCorner = false;
		boolean edge = false;


		//check up
		if(kingLocation.getX() != 0) {//if not top row

			ChessPiece piece = getBoard().getPiece(new Location(kingLocation.getX() - 1, kingLocation.getY()));

			if(piece.type == PieceType.BLACK ||
					(kingLocation.getX() - 1 == 0 && (kingLocation.getY() == 0 || kingLocation.getY() == 10)) ||
					(kingLocation.getX() - 1 == 5 && kingLocation.getY()  == 5)) {
				counter++;
				if(kingLocation.getX() - 1 == 0 && (kingLocation.getY() == 0 || kingLocation.getY() == 10))
					isCorner=true;
			}
		}else{
			edge = true;
		}
		//check down
		if(kingLocation.getX() != 10) {//if not bottom row

			Location loc = new Location(kingLocation.getX() + 1, kingLocation.getY());
			ChessPiece piece = getBoard().getPiece(loc);

			if(piece.type == PieceType.BLACK ||
					(loc.getX() == 10 && (loc.getY() == 0 || loc.getY() == 10)) ||
					(loc.getX()==5 && loc.getY()==5)) {
				counter++;
				if(loc.getX() == 10 && (loc.getY() == 0 || loc.getY() == 10))
					isCorner=true;
			}
		}else{
			edge = true;
		}
		//check left
		if(kingLocation.getY() != 0) {//if not left col

			Location loc = new Location(kingLocation.getX(), kingLocation.getY() - 1);
			ChessPiece piece = getBoard().getPiece(loc);

			if(piece.type == PieceType.BLACK ||
					(loc.getY() == 0 && (loc.getX() == 0 || loc.getX() == 10)) ||
					(loc.getX()==5 && loc.getY()==5)) {
				counter++;
				if(loc.getY() == 0 && (loc.getX() == 0 || loc.getX() == 10))
					isCorner=true;
			}
		}else{
			edge = true;
		}

		//check right
		if(kingLocation.getY() != 10) {//if not right col

			Location loc = new Location(kingLocation.getX(), kingLocation.getY() + 1);
			ChessPiece piece = getBoard().getPiece(loc);

			if(piece.type == PieceType.BLACK ||
					(loc.getY() == 10 && (loc.getX() == 0 || loc.getX() == 10)) ||
					(loc.getY()==5 && loc.getX()==5)) {
				counter++;
				if(loc.getY() == 10 && (loc.getX() == 0 || loc.getX() == 10))
					isCorner=true;
			}
		}else{
			edge = true;
		}

		return (!isCorner && counter==4) || (isCorner && counter==3) ||
				(edge && counter == 3);
	}

	//obtain an array of legal for the player playing with pieces of type pieceType
	Move[] getPlayerLegalMoves(PieceType pieceType) {
		if(pieceType == PieceType.EMPTY)
			return null;


		ArrayList<Move> moves = new ArrayList<Move>();

		if(moves.isEmpty()) {
			for(int row=0; row < 11; row++) {
				for(int col=0; col < 11; col++) {
					if(getBoard().getPiece(new Location(row,col)).type == pieceType) {
						for(int a = (col+1); a < 11; a++) { //check right
							if(getBoard().getPiece(new Location(row,col)) instanceof  HnefataflKing && pieceType == PieceType.WHITE) {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(row, a), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location( row, a),pieceType.string()));
								else
									break;
							}
							else {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(row, a), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location( row, a),pieceType.string()));
								else if(row==5 && a==5 && getBoard().getPiece(new Location(5,5)).type == PieceType.EMPTY){ //skip over center
									//do nothing
								}
								else
									break;
							}
						}
						for(int a = (col-1); a >= 0; a--) { //check left
							if(getBoard().getPiece(new Location(row,col)) instanceof  HnefataflKing && pieceType == PieceType.WHITE) {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(row, a), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location( row, a),pieceType.string()));

								else
									break;
							}
							else {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(row, a), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location( row, a),pieceType.string()));
								else if(row==5 && a==5 && getBoard().getPiece(new Location(5,5)).type == PieceType.EMPTY){ //skip over center
									//do nothing
								}

								else
									break;
							}
						}
						for(int a = (row+1); a < 11; a++) { //check down
							if(getBoard().getPiece(new Location(row,col)) instanceof  HnefataflKing && pieceType == PieceType.WHITE) {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(a, col), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location(a,col),pieceType.string()));
								else
									break;
							}
							else {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(a, col), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location( a,col),pieceType.string()));
								else if(row==5 && a==5 && getBoard().getPiece(new Location(5,5)).type == PieceType.EMPTY){ //skip over center
									//do nothing
								}
								else
									break;
							}
						}
						for(int a = (row-1); a >= 0; a--) { //check up
							if(getBoard().getPiece(new Location(row,col)) instanceof  HnefataflKing && pieceType == PieceType.WHITE) {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(a, col), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location(a,col),pieceType.string()));
								else
									break;
							}
							else {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(a, col), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location( a, col),pieceType.string()));
								else if(row==5 && a==5 && getBoard().getPiece(new Location(5,5)).type == PieceType.EMPTY){ //skip over center
									//do nothing
								}
								else
									break;
							}
						}
					}
				}
			}
		}

		if(moves.isEmpty())
			return null;
		else {
			Move[] moveArr = new Move[moves.size()];
			for(int a=0; a < moves.size(); a++)
				moveArr[a] = moves.get(a);
			return moveArr;
		}
	}

	private boolean isHostile(int row, int col) {
		return (row==0 && col==0) || (row==0 && col==10) ||
				(row==10 && col==0) || (row==10 && col==10) || row == 5 && col == 5;
	}

	boolean isMoveLegal(PieceType type, Move move) {
		//System.out.println(move.getFrom() + " to " + move.getTo());

		if(move.getTo().getX() < 0 || move.getTo().getX() >= 11 ||
		   move.getTo().getY() < 0 || move.getTo().getY() >= 11) //out of board bounds
		{
			//System.out.println("1");
			return false;}
		else if(getBoard().getPiece(move.getTo()).type != PieceType.EMPTY)//is there a piece already there
		{
			//System.out.println("2");
			return false;}
		else if( !(move.getFrom().equals(getKingLocation()))
				&& (isHostile(move.getTo().getX(), move.getTo().getY())))
		{
			//System.out.println("3");
			return false;
		} //only king can move to a corner and throne
		else return !(move.getFrom().getX() != move.getTo().getX() &&
					move.getFrom().getY() != move.getTo().getY());
	}

    //Black starts instead of white in this game, so when loading we have to change whose turn it is
    @Override
    public void load(String code) {
        super.load(code);
        isWhitesTurn = !isWhitesTurn;
    }
}
