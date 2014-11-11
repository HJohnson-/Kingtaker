package GrandChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hj1012 on 03/11/14.
 */
public class Marshal extends ChessPiece {

	public Marshal(Board board, PieceType type, Location cords) {
		super(board, type, cords, "marshal");
	}

	@Override
	protected boolean validInState(Location to) {
		if(cords.getX() == to.getX() || cords.getY() == to.getY()) {
			return board.clearLine(cords, to);
		} else {
			return true;
		}
	}

	@Override
	public int returnValue() {
		return 9;
	}

	@Override
	public List<Location> allPieceMoves() {
		List<Location> moves = new LinkedList<Location>();
		for (int x = 0; x < board.numCols(); x++) {
			moves.add(new Location(x, cords.getY()));
		}
		for (int y = 0; y < board.numRows(); y++) {
			moves.add(new Location(cords.getX(), y));
		}
		for (int x = -2; x <= 2; x++) {
			if (x == 0) continue;
			if (cords.getX() + x < 0) continue;
			if (cords.getX() + x >= board.numCols()) break;

			int y = 3 - Math.abs(x);
			Location l1 = new Location(cords.getX() + x, cords.getY() + y);
			Location l2 = new Location(cords.getX() + x, cords.getY() - y);

			if (board.onBoard(l1)) moves.add(l1);
			if (board.onBoard(l2)) moves.add(l2);
		}
		return moves;
	}

	@Override
	public String getName() {
		return "Marshal";
	}

    @Override
    public ChessPiece clone() {
        return new Marshal(board, type, cords.clone());
    }
}
