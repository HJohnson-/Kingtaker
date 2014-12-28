package variants.RollerBallChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import pieces.EmptyPiece;
import variants.BasicChess.King;

import java.util.Arrays;

public class RBBoard extends Board {

	public RBBoard(){
		this(7,7);
	}
	
	public RBBoard(int rows, int cols){
		pieces = new ChessPiece[rows][cols];
		for(ChessPiece[] row : pieces){
			Arrays.fill(row, new EmptyPiece(this, null));
		}
	}

	@Override
	public int getVariationID() {
		return 4;
	}


	@Override
    public boolean initializeBoard() {


  //   	// Top pieces rook
     	Location location;
		location = new Location(2,0);
		this.placePiece(location, new RBRook(this, PieceType.BLACK, location));


     	 location = new Location(2,1);
		this.placePiece(location, new RBRook(this, PieceType.BLACK, location));

		// //Top pieces bishop

  //   	 location = new Location(3,0);
		// this.placePiece(location, new RBBishop(this, PieceType.BLACK, location));

		// //Top pieces king
		location = new Location(3,1);
		this.placePiece(location, new RBKing(this, PieceType.BLACK, location));

		//Top pieces Pawn
		  location = new Location(4,0);
		this.placePiece(location, new RBPawn(this, PieceType.BLACK, location));

		  location = new Location(4,1);
		this.placePiece(location, new RBPawn(this, PieceType.BLACK, location));




		// Bot pieces rook
     	 location = new Location(4,5);
		this.placePiece(location, new RBRook(this, PieceType.WHITE, location));


     	 location = new Location(4,6);
		this.placePiece(location, new RBRook(this, PieceType.WHITE, location));

		// //Bot pieces bishop

  //   	 location = new Location(3,5);
		// this.placePiece(location, new RBBishop(this, PieceType.WHITE, location));

		//Bot pieces king
		 location = new Location(3,5);
		this.placePiece(location, new RBKing(this, PieceType.WHITE, location));

		//Bot pieces Pawn
		  location = new Location(2,5);
		this.placePiece(location, new RBPawn(this, PieceType.WHITE, location));

		  location = new Location(2,6);
		this.placePiece(location, new RBPawn(this, PieceType.WHITE, location));

        return true;
    }

    @Override
    public Board clone() {
   		RBBoard b = new RBBoard();
		ChessPiece[][] newPieces = new ChessPiece[this.pieces.length][this.pieces[0].length];
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				newPieces[i][j] = pieces[i][j].clone();
				newPieces[i][j].board = b;
			}
		}
		b.pieces = newPieces;

		b.setController(this.getController().clone());
		b.getController().setBoard(b);

		b.doDrawing = this.doDrawing;

		return b;
    }
}
