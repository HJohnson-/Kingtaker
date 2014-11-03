package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import java.util.LinkedList;
import java.util.List;

/**
 * Default knight
 */
public class Knight extends ChessPiece {
	public Knight(Board board, PieceType type, Location cords) {
		super(board, type, cords, "knight");
        graphics = new KnightGraphicsControl(cords, cords);
	}

	public int returnValue() {
		return 3;
	}

	@Override
	protected boolean validInState(Location to) {
		return true;
	}

    @Override
	public List<Location> allPieceMoves() {
		List<Location> moves = new LinkedList<Location>();

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
		return "Knight";
	}
}
