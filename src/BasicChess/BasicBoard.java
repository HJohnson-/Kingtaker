package BasicChess;
import graphics.tools;
import main.Board;
import main.GameController;
import main.Location;
import main.PieceType;
import pieces.EmptyPiece;

/**
 * Created by crix9 on 15/10/2014.
 */
public class BasicBoard extends Board {

	//returns true if human player goes first in offline mode or the lobby host goes first in online mode.
	public boolean initializeBoard(){

		String[] pieces = {"rook", "bishop", "knight", "queen", "king", "pawn"};
		tools.loadPieces(pieces);

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
				case 3:
					this.placePiece(location, new Queen(this, PieceType.BLACK, location));
					break;
				case 4:
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

		//black pieces
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
				case 3:
					this.placePiece(location, new Queen(this, PieceType.WHITE, location));
					break;
				case 4:
					this.placePiece(location, new King(this, PieceType.WHITE, location));
					break;
				default:

			}
		}

		Location emptyLocation;
		for(int i = 2; i < 6; i++){
			for(int j = 0; j < 8; j++){
				emptyLocation = new Location(i,j);
				this.placePiece(emptyLocation, new EmptyPiece(this, emptyLocation));
			}
		}

		return false;
	}
}
