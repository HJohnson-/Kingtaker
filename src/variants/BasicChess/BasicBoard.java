package variants.BasicChess;
import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

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
			Location location = new Location(2,j);
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

	@Override
	public int getVariationID() {
		return 0;
	}

    @Override
    public Board clone() {
        BasicBoard b = new BasicBoard();

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
