package variants.BasicChess;
import main.Board;
import main.Location;
import main.PieceType;

/**
 * Created by crix9 on 15/10/2014.
 */
public class BasicBoard extends Board {

	//returns true if human player goes first in offline mode or the lobby host goes first in online mode.
	public boolean initializeBoard(){

		// black pawns
		for(int j = 0; j < 8; j++){
			Location location = new Location(1,j);
			this.placePiece(location, new Pawn(this, PieceType.BLACK, location));
		}


		//black pieces
		for(int i = 0; i < 8; i++){
			Location location = new Location(0,i);
			switch (i){
				case 0:
				case 7:
					this.placePiece(location, new Rook(this, PieceType.BLACK, location));
					break;
				case 1:
				case 6:
					this.placePiece(location, new Knight(this, PieceType.BLACK, location));
					break;
				case 2:
				case 5:
					this.placePiece(location, new Bishop(this, PieceType.BLACK, location));
					break;
				case 4:
					this.placePiece(location, new Queen(this, PieceType.BLACK, location));
					break;
				case 3:
					this.placePiece(location, new King(this, PieceType.BLACK, location));
					break;
				default:

			}
		}


		// white pawns
		for(int j = 0; j < 8; j++){
			Location location = new Location(6,j);
			this.placePiece(location, new Pawn(this, PieceType.WHITE, location));
		}

		// white pieces
		for(int i = 0; i < 8; i++){
			Location location = new Location(7,i);
			switch (i){
				case 0:
				case 7:
					this.placePiece(location, new Rook(this, PieceType.WHITE, location));
					break;
				case 1:
				case 6:
					this.placePiece(location, new Knight(this, PieceType.WHITE, location));
					break;
				case 2:
				case 5:
					this.placePiece(location, new Bishop(this, PieceType.WHITE, location));
					break;
				case 4:
					this.placePiece(location, new Queen(this, PieceType.WHITE, location));
					break;
				case 3:
					this.placePiece(location, new King(this, PieceType.WHITE, location));
					break;
				default:

			}
		}

		return false;
	}
}
