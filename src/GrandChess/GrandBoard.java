package GrandChess;

import BasicChess.*;
import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import pieces.EmptyPiece;

import java.util.Arrays;

/**
 * Created by hj1012 on 03/11/14.
 */
public class GrandBoard extends Board {

	public GrandBoard() {
		this(10,10);
	}

	public GrandBoard(int rows, int cols) {
		pieces = new ChessPiece[rows][cols];
		for (ChessPiece[] row : pieces) {
			Arrays.fill(row, new EmptyPiece(this, null));
		}
	}

    @Override
    public Board clone() {
        GrandBoard b = new GrandBoard();
        b.pieces = this.pieces;
        b.setController(this.getController().clone());
        return b;
    }

    @Override
	public boolean initializeBoard() {
		// black pawns
		for(int j = 0; j < 10; j++){
			Location location = new Location(2,j);
			this.placePiece(location, new Pawn(this, PieceType.BLACK, location));
		}


		//black pieces
		for(int i = 0; i < 10; i++){
			Location location = new Location(1,i);
			switch (i){
				case 0:
				case 9:
					location = new Location(0,i);
					this.placePiece(location, new Rook(this, PieceType.BLACK, location));
					break;
				case 1:
				case 8:
					this.placePiece(location, new Knight(this, PieceType.BLACK, location));
					break;
				case 2:
				case 7:
					this.placePiece(location, new Bishop(this, PieceType.BLACK, location));
					break;
				case 3:
					this.placePiece(location, new Queen(this, PieceType.BLACK, location));
					break;
				case 4:
					this.placePiece(location, new King(this, PieceType.BLACK, location));
					break;
				case 5:
					this.placePiece(location, new Marshal(this, PieceType.BLACK, location));
					break;
				case 6:
					this.placePiece(location, new Cardinal(this, PieceType.BLACK, location));
					break;
				default:
			}
		}


		// white pawns
		for(int j = 0; j < 10; j++){
			Location location = new Location(7,j);
			this.placePiece(location, new Pawn(this, PieceType.WHITE, location));
		}

		//white pieces
		for(int i = 0; i < 10; i++){
			Location location = new Location(8,i);
			switch (i){
				case 0:
				case 9:
					location = new Location(9,i);
					this.placePiece(location, new Rook(this, PieceType.WHITE, location));
					break;
				case 1:
				case 8:
					this.placePiece(location, new Knight(this, PieceType.WHITE, location));
					break;
				case 2:
				case 7:
					this.placePiece(location, new Bishop(this, PieceType.WHITE, location));
					break;
				case 3:
					this.placePiece(location, new Queen(this, PieceType.WHITE, location));
					break;
				case 4:
					this.placePiece(location, new King(this, PieceType.WHITE, location));
					break;
				case 5:
					this.placePiece(location, new Marshal(this, PieceType.WHITE, location));
					break;
				case 6:
					this.placePiece(location, new Cardinal(this, PieceType.WHITE, location));
					break;
			}
		}
		return true;
	}
}
