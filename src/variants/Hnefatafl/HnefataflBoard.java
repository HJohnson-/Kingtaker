package variants.Hnefatafl;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import pieces.EmptyPiece;

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
	public int getVariationID() {
		return 3;
	}

	@Override
	public Board clone() {
		HnefataflBoard b = new HnefataflBoard();

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
