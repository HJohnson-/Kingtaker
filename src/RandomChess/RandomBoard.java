package RandomChess;

import BasicChess.*;
import main.Board;
import main.Location;
import main.PieceType;

import java.util.Random;

/**
 * Initializes the random state of the board.
 */
public class RandomBoard extends Board {

	@Override
	public boolean initializeBoard() {
		boolean[] filled = {false, false, false, false, false, false, false, false};

		// black pawns
		for(int j = 0; j < 8; j++){
			Location location = new Location(1,j);
			this.placePiece(location, new Pawn(this, PieceType.BLACK, location));
		}

		// white pawns
		for(int j = 0; j < 8; j++){
			Location location = new Location(6,j);
			this.placePiece(location, new Pawn(this, PieceType.WHITE, location));
		}

		// bishops
		Random rowGen = new Random();
		int WSqBis = rowGen.nextInt(4);
		Location location = new Location(0, 2*WSqBis+1);
		this.placePiece(location, new Bishop(this, PieceType.BLACK, location));
		location = flip(location);
		this.placePiece(location, new Bishop(this, PieceType.WHITE, location));
		filled[2*WSqBis+1] = true;
		int BSqBis = rowGen.nextInt(4);
		location = new Location(0, 2*BSqBis);
		this.placePiece(location, new Bishop(this, PieceType.BLACK, location));
		location = flip(location);
		this.placePiece(location, new Bishop(this, PieceType.WHITE, location));
		filled[2*BSqBis] = true;

		// queens
		int QuPos = rowGen.nextInt(6);
		for(int i = 0; i <= QuPos; i++) {
			if(filled[i] == true) QuPos++;
		}
		location = new Location(0, QuPos);
		this.placePiece(location, new Queen(this, PieceType.BLACK, location));
		location = flip(location);
		this.placePiece(location, new Queen(this, PieceType.WHITE, location));
		filled[QuPos] = true;

		//knights
		int KnPos = rowGen.nextInt(5);
		for(int i = 0; i <= KnPos; i++) {
			if(filled[i] == true) KnPos++;
		}
		location = new Location(0, KnPos);
		this.placePiece(location, new Knight(this, PieceType.BLACK, location));
		location = flip(location);
		this.placePiece(location, new Knight(this, PieceType.WHITE, location));
		filled[KnPos] = true;
		KnPos = rowGen.nextInt(4);
		for(int i = 0; i <= KnPos; i++) {
			if(filled[i] == true) KnPos++;
		}
		location = new Location(0, KnPos);
		this.placePiece(location, new Knight(this, PieceType.BLACK, location));
		location = flip(location);
		this.placePiece(location, new Knight(this, PieceType.WHITE, location));
		filled[KnPos] = true;

		for(int i = 0; i < numRows(); i++) {
			if(filled[i] == false) {
				location = new Location(0, i);
				this.placePiece(location, new Rook(this, PieceType.BLACK, location));
				location = flip(location);
				this.placePiece(location, new Rook(this, PieceType.WHITE, location));
				filled[i] = true;
				break;
			}
		}

		for(int i = 0; i < numRows(); i++) {
			if(filled[i] == false) {
				location = new Location(0, i);
				this.placePiece(location, new King(this, PieceType.BLACK, location));
				location = flip(location);
				this.placePiece(location, new King(this, PieceType.WHITE, location));
				filled[i] = true;
				break;
			}
		}

		for(int i = 0; i < numRows(); i++) {
			if(filled[i] == false) {
				location = new Location(0, i);
				this.placePiece(location, new Rook(this, PieceType.BLACK, location));
				location = flip(location);
				this.placePiece(location, new Rook(this, PieceType.WHITE, location));
				filled[i] = true;
				break;
			}
		}

		return true;
	}

    @Override
    public Board clone() {
        RandomBoard b = new RandomBoard();
        b.pieces = this.pieces.clone();
        b.setController(this.getController().clone());
        return b;
    }

    private Location flip(Location location) {
		int newx = location.getX() == 0 ? numCols()-1 : 0;
		int newy = numRows() - location.getY() - 1;
		return new Location(newx, newy);
	}
}
