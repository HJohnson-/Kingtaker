package Hnefatafl;

import BasicChess.Rook;
import main.Board;
import main.Location;
import main.PieceType;


/**
 * Created by crix9 on 21/11/2014.
 */
public class Defender extends Rook {

	public Defender(Board board, PieceType type, Location cords) {
		super(board, type, cords);
	}

	@Override
	public String getName() {
		return "Defender";
	}
}
