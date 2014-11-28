package variants.Hnefatafl;

import variants.BasicChess.Rook;
import main.Board;
import main.Location;
import main.PieceType;


/**
 * Created by crix9 on 21/11/2014.
 */
public class HnefataflKing extends Rook {

	public HnefataflKing(Board board, PieceType type, Location cords) {

		super(board, type, cords);
	}

	@Override
	public int returnValue() {
		return 9999;
	}

	@Override
	public String getName() {
		return "variants.Hnefatafl King";
	}
}
