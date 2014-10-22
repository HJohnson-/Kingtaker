package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hj1012 on 15/10/14.
 */
public class Knight extends ChessPiece {
	public Knight(Board board, PieceType type, Location cords) {
		super(board, type, cords, "knight");
	}

	public int returnValue() {
		return 3;
	}

	@Override
	protected boolean beingBlocked(Location to) {
		return false;
	}

	@Override
	protected boolean invalidTarget(Location to) {
		int horizontalMovement = cords.getX() - to.getX();
		int verticalMovement = cords.getY() -to.getY();

		if((Math.abs(horizontalMovement) == 1 && Math.abs(verticalMovement) == 2)
				|| (Math.abs(verticalMovement) == 1 && Math.abs(horizontalMovement) == 2)) {
			return false;
		} else {
			return true;
		}
	}

    @Override
    public List<Location> allUnblockedMoves() {
        List<Location> moves = new LinkedList<Location>();

        for (int x = -2; x <= 2; x++) {
            if (x == 0) continue;
            if (cords.getX() + x < 0) continue;
            if (cords.getX() + x >= board.numCols()) break;
            int y = 3 - Math.abs(x);

            if (cords.getY() + y < board.numRows()) {
                moves.add(new Location(x, cords.getY() + y));
            }
            if (cords.getY() - y >= 0) {
                moves.add(new Location(x, cords.getY() - y));
            }
        }

        return moves;
    }
}
