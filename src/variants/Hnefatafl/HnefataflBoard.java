package variants.Hnefatafl;

import main.Board;
import main.Location;
import main.Move;
import main.PieceType;
import pieces.ChessPiece;
import pieces.EmptyPiece;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by crix9 on 21/11/2014.
 */
public class HnefataflBoard extends Board {

	public HnefataflBoard() {
		this(11,11);
	}

	public  HnefataflBoard(int rows, int cols) {
		pieces = new ChessPiece[rows][cols];
		for (ChessPiece[] row : pieces) {
			Arrays.fill(row, new EmptyPiece(this, null));
		}
	}

	@Override
	public boolean initializeBoard() {

		emptyBoard();

		//King in the middle
		Location location = new Location(5,5);
		this.placePiece(location, new HnefataflKing(this, PieceType.WHITE, location));

		//defenders surrounding king
		location = new Location(3,5);
		this.placePiece(location, new Defender(this, PieceType.WHITE, location));

		location = new Location(4,4);
		this.placePiece(location, new Defender(this, PieceType.WHITE, location));

		location = new Location(4,5);
		this.placePiece(location, new Defender(this, PieceType.WHITE, location));

		location = new Location(4,6);
		this.placePiece(location, new Defender(this, PieceType.WHITE, location));

		location = new Location(5,3);
		this.placePiece(location, new Defender(this, PieceType.WHITE, location));

		location = new Location(5,4);
		this.placePiece(location, new Defender(this, PieceType.WHITE, location));

		location = new Location(5,6);
		this.placePiece(location, new Defender(this, PieceType.WHITE, location));

		location = new Location(5,7);
		this.placePiece(location, new Defender(this, PieceType.WHITE, location));

		location = new Location(6,4);
		this.placePiece(location, new Defender(this, PieceType.WHITE, location));

		location = new Location(6,5);
		this.placePiece(location, new Defender(this, PieceType.WHITE, location));

		location = new Location(6,6);
		this.placePiece(location, new Defender(this, PieceType.WHITE, location));

		location = new Location(7,5);
		this.placePiece(location, new Defender(this, PieceType.WHITE, location));


		//Attackers
		for(int a=3; a<=7; a++) {

			location = new Location(0,a);
			this.placePiece(location, new Attacker(this, PieceType.BLACK, location));

			location = new Location(10,a);
			this.placePiece(location, new Attacker(this, PieceType.BLACK, location));

			location = new Location(a,10);
			this.placePiece(location, new Attacker(this, PieceType.BLACK, location));

			location = new Location(a,0);
			this.placePiece(location, new Attacker(this, PieceType.BLACK, location));
		}

		location = new Location(1,5);
		this.placePiece(location, new Attacker(this, PieceType.BLACK, location));

		location = new Location(9,5);
		this.placePiece(location, new Attacker(this, PieceType.BLACK, location));

		location = new Location(5,1);
		this.placePiece(location, new Attacker(this, PieceType.BLACK, location));

		location = new Location(5,9);
		this.placePiece(location, new Attacker(this, PieceType.BLACK, location));

		return true;
	}

	private void emptyBoard() {
		pieces = new ChessPiece[11][11];
		for (ChessPiece[] row : pieces) {
			Arrays.fill(row, new EmptyPiece(this, null));
		}
	}

	@Override
	public Board clone() {
		HnefataflBoard b = new HnefataflBoard();
		b.pieces = this.pieces;
		b.setController(this.getController().clone());
		b.doDrawing = doDrawing;
		for (int i = 0; i < b.pieces.length; i++) {
			for (int j = 0; j < b.pieces.length; j++) {
				b.pieces[i][j] = b.pieces[i][j].clone();
				b.pieces[i][j].board = b;
			}
		}
		return b;
	}


	//execute the given move, move is assumed not null legal
	void makeMove(PieceType type, Move move) {


		ChessPiece pieceToMove = this.getPiece(move.getFrom());
		this.placePiece(move.getTo(),pieceToMove);
		this.placePiece(move.getFrom(), new EmptyPiece(this,null));

		PieceType currentPlayerType = pieceToMove.type;


		//Check whether move results in a capture of opponent's piece

		//check row above
		if(move.getTo().getX() != 0) {//if not top row
			Location loc = new Location(move.getTo().getX()-1,move.getTo().getY());
			ChessPiece toPiece = this.getPiece(loc);
			PieceType toPieceType = toPiece.type;

			if(isEnemyPiece(currentPlayerType,toPieceType) &&
				move.getTo().getX()-1 != 0 && // cant capture this piece as it's not between two
				!(toPiece instanceof HnefataflKing)) { //cannot take king if not surrounded in all 4 parts

				Location loc2 = new Location(move.getTo().getX()-2,move.getTo().getY());
				ChessPiece pieceUp2 = this.getPiece(loc2);

				if((currentPlayerType == pieceUp2.type) ||
				   (loc2.getX()  == 0 && (loc2.getY() == 0 || loc2.getY() == 10)) || //corners are hostile and can help in capturing other pieces
				   (loc2.getX()  == 5 && loc2.getY()  == 5)) {  //throne can also be hostile
					this.placePiece(loc, new EmptyPiece(this,null)); //piece has been captured, remove it
				}
			}
		}
		//check down
		if(move.getTo().getX() != 10) {//if not moving to bottom row
			Location loc = new Location(move.getTo().getX()+1,move.getTo().getY());
			ChessPiece toPiece = this.getPiece(loc);
			PieceType toPieceType = toPiece.type;

			if(isEnemyPiece(currentPlayerType, toPieceType) &&
			   move.getTo().getX()+1 != 10 &&
			   !(toPiece instanceof HnefataflKing)) {

				Location loc2 = new Location(move.getTo().getX()+2,move.getTo().getY());
				ChessPiece pieceUp2 = this.getPiece(loc2);

				if((currentPlayerType == pieceUp2.type) ||
				   (loc2.getX()  == 10 && (loc2.getY() == 0 || loc2.getY() == 10)) || //corners are hostile and can help in capturing other pieces
				   (loc2.getX()  == 5 && loc2.getY()  == 5)) {  //throne can also be hostile
					this.placePiece(loc, new EmptyPiece(this,null)); //piece has been captured, remove it
				}
			}
		}
		//check left
		if(move.getTo().getY() != 0) {//if not left col
			Location loc = new Location(move.getTo().getX(),move.getTo().getY()-1);
			ChessPiece toPiece = this.getPiece(loc);
			PieceType toPieceType = toPiece.type;


			if(isEnemyPiece(currentPlayerType, toPieceType) &&
					move.getTo().getY()-1 != 0 &&
					!(toPiece instanceof HnefataflKing)) {

				Location loc2 = new Location(move.getTo().getX(),move.getTo().getY()-2);
				ChessPiece pieceUp2 = this.getPiece(loc2);

				if(currentPlayerType == pieceUp2.type ||
				   (loc2.getY() == 0 && (loc2.getX() == 0 || loc2.getX() == 10)) ||
				   (loc2.getX()  == 5 && loc2.getY()  == 5)) {

					this.placePiece(loc, new EmptyPiece(this,null));
				}
			}
		}
		//check right
		if(move.getTo().getY() != 10) {//if not right col

			Location loc = new Location(move.getTo().getX(),move.getTo().getY()+1);
			ChessPiece toPiece = this.getPiece(loc);
			PieceType toPieceType = toPiece.type;

			if(isEnemyPiece(currentPlayerType, toPieceType) &&
					loc.getY() != 10 &&
					!(toPiece instanceof HnefataflKing)) {

				Location loc2 = new Location(move.getTo().getX(),move.getTo().getY()+2);
				ChessPiece pieceUp2 = this.getPiece(loc2);

				if(currentPlayerType == pieceUp2.type ||
					(loc2.getY() == 10 && (loc.getX() == 0 || loc2.getX() == 10)) ||
						(loc2.getX()  == 5 && loc2.getY()  == 5)) {
					this.placePiece(loc, new EmptyPiece(this,null));
				}
			}
		}
	}

	//check if pieces are enemies
	boolean isEnemyPiece(PieceType curr, PieceType opp) {
		return (curr == PieceType.WHITE && opp == PieceType.BLACK) ||
				(opp == PieceType.WHITE && curr == PieceType.BLACK);
	}


	boolean isMoveLegal(PieceType type, Move move) {
       	System.out.println(move.getFrom() + " to " + move.getTo());

		if(move.getTo().getX() < 0 || move.getTo().getX() >= 11 ||
		   move.getTo().getY() < 0 || move.getTo().getY() >= 11) //out of board bounds
		   {
			System.out.println("1");
			return false;}
		else if(this.getPiece(move.getTo()).type != PieceType.EMPTY)//is there a piece already there
		{ System.out.println("2"); return false;}
		else if( !(move.getFrom().equals(getKingLocation()))
				&& ((move.getTo().getX() == 0 && move.getTo().getY() == 0) ||
				    (move.getTo().getX() == 0 && move.getTo().getY() == 10) ||
				    (move.getTo().getX() == 10 && move.getTo().getY() == 0) ||
				    (move.getTo().getX() == 10 && move.getTo().getY() == 10)))
		{ System.out.println("3"); return false; } //only king can move to a corner
		else if((move.getFrom() != getKingLocation()) && (move.getTo().getX() == 5 && move.getTo().getY() == 5))
		{ System.out.println("4");return false;}  //only the king can move to the throne, centre location
		else if(move.getFrom().getX() != move.getTo().getX() &&
				move.getFrom().getY() != move.getTo().getY())
		{ System.out.println("5"); return false; } //cannot make diagonal moves
		else
		{System.out.println("6"); return true;}
	}

	//WHITE wins if the king is one of the 4 corners
	boolean checkWhiteWins() {
		Location loc = new Location(0,0);
		ChessPiece topLeftPiece = this.getPiece(loc);

		loc = new Location(0,10);

		ChessPiece topRightPiece = this.getPiece(loc);

		loc = new Location(10,0);
		ChessPiece bottomLeftPiece = this.getPiece(loc);

		loc = new Location(10,10);
		ChessPiece bottomRightPiece = this.getPiece(loc);

		return (topLeftPiece instanceof HnefataflKing) || (topRightPiece instanceof HnefataflKing)
				|| (bottomLeftPiece instanceof HnefataflKing) || (bottomRightPiece instanceof HnefataflKing);
	}


	private Location getKingLocation(){
		for(int row = 0; row < this.numRows(); row++){
			for (int col = 0; col <this.numCols(); col++){
				Location loc = new Location(row, col);
				if(this.getPiece(loc) instanceof HnefataflKing){
					return loc;
				}
			}
		}
		return null; //should not get here
	}

	//BLACK wins if they surround the white king (corners and central spot are hostile and can act as black soldiers)
	boolean checkBlackWins() {

		Location kingLocation = getKingLocation();
		System.out.println("King location: " + kingLocation);
		int counter=0;
		boolean isCorner = false;


		//check up
		if(kingLocation.getX() != 0) {//if not top row

			ChessPiece piece = this.getPiece(new Location(kingLocation.getX() - 1, kingLocation.getY()));

			if(piece.type == PieceType.BLACK ||
				(kingLocation.getX() - 1 == 0 && (kingLocation.getY() == 0 || kingLocation.getY() == 10)) ||
				(kingLocation.getX() - 1==5 && kingLocation.getY()==5)) {
				counter++;
				if(kingLocation.getX() - 1 == 0 && (kingLocation.getY() == 0 || kingLocation.getY() == 10))
					isCorner=true;
			}
		}
		//check down
		if(kingLocation.getX() != 10) {//if not bottom row

			Location loc = new Location(kingLocation.getX() + 1, kingLocation.getY());
			ChessPiece piece = this.getPiece(loc);

			if(piece.type == PieceType.BLACK ||
			   (loc.getX() == 10 && (loc.getY() == 0 || loc.getY() == 10)) ||
			   (loc.getX()==5 && loc.getY()==5)) {
				counter++;
				if(loc.getX() == 10 && (loc.getY() == 0 || loc.getY() == 10))
					isCorner=true;
			}
		}
		//check left
		if(kingLocation.getY() != 0) {//if not left col

			Location loc = new Location(kingLocation.getX(), kingLocation.getY() - 1);
			ChessPiece piece = this.getPiece(loc);

			if(piece.type == PieceType.BLACK ||
			  (loc.getY() == 0 && (loc.getX() == 0 || loc.getX() == 10)) ||
			  (loc.getX()==5 && loc.getY()==5)) {
				counter++;
				if(loc.getY() == 0 && (loc.getX() == 0 || loc.getX() == 10))
					isCorner=true;
			}
		}
		//check right
		if(kingLocation.getY() != 10) {//if not right col

			Location loc = new Location(kingLocation.getX(), kingLocation.getY() + 1);
			ChessPiece piece = this.getPiece(loc);

			if(piece.type == PieceType.BLACK ||
			   (loc.getY() == 10 && (loc.getX() == 0 || loc.getX() == 10)) ||
			   (loc.getY()==5 && loc.getX()==5)) {
				counter++;
				if(loc.getY() == 10 && (loc.getX() == 0 || loc.getX() == 10))
					isCorner=true;
			}
		}

		if(!isCorner && counter==4)
			return true;
		else if(isCorner && counter==3)
			return true;
		else
			return false;
	}

	//obtain an array of legal for the player playing with pieces of type pieceType
	Move[] getPlayerLegalMoves(PieceType pieceType) {
		if(pieceType == PieceType.EMPTY)
			return null;


		ArrayList<Move> moves = new ArrayList<Move>();

		if(moves.isEmpty()) {
			for(int row=0; row < 11; row++) {
				for(int col=0; col < 11; col++) {
					if(this.getPiece(new Location(row,col)).type == pieceType) {
						for(int a = (col+1); a < 11; a++) { //check right
							if(this.getPiece(new Location(row,col)) instanceof  HnefataflKing && pieceType == PieceType.WHITE) {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(row, a), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location( row, a),pieceType.string()));
								else
									break;
							}
							else {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(row, a), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location( row, a),pieceType.string()));
								else if(row==5 && a==5 && this.getPiece(new Location(5,5)).type == PieceType.EMPTY){ //skip over center
									//do nothing
								}
								else
									break;
							}
						}
						for(int a = (col-1); a >= 0; a--) { //check left
							if(this.getPiece(new Location(row,col)) instanceof  HnefataflKing && pieceType == PieceType.WHITE) {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(row, a), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location( row, a),pieceType.string()));

								else
									break;
							}
							else {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(row, a), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location( row, a),pieceType.string()));
								else if(row==5 && a==5 && this.getPiece(new Location(5,5)).type == PieceType.EMPTY){ //skip over center
									//do nothing
								}

								else
									break;
							}
						}
						for(int a = (row+1); a < 11; a++) { //check down
							if(this.getPiece(new Location(row,col)) instanceof  HnefataflKing && pieceType == PieceType.WHITE) {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(a, col), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location(a,col),pieceType.string()));
								else
									break;
							}
							else {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(a, col), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location( a,col),pieceType.string()));
								else if(row==5 && a==5 && this.getPiece(new Location(5,5)).type == PieceType.EMPTY){ //skip over center
									//do nothing
								}
								else
									break;
							}
						}
						for(int a = (row-1); a >= 0; a--) { //check up
							if(this.getPiece(new Location(row,col)) instanceof  HnefataflKing && pieceType == PieceType.WHITE) {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(a, col), pieceType.string())))
									moves.add(new Move(new Location(row,col), new Location(a,col),pieceType.string()));
								else
									break;
							}
							else {
								if(isMoveLegal(pieceType, new Move(new Location(row, col), new Location(a, col), pieceType.string())))
								moves.add(new Move(new Location(row,col), new Location( a, col),pieceType.string()));
								else if(row==5 && a==5 && this.getPiece(new Location(5,5)).type == PieceType.EMPTY){ //skip over center
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
}

