package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hj1012 on 15/10/14.
 */
public class Bishop extends ChessPiece {
	public Bishop(Board board, PieceType type, Location cords) {
		super(board, type, cords, "bishop");
	}

	public int returnValue() {
		return 3;
	}

	@Override
	protected boolean invalidTarget(Location to) {
		int horizontalMovement = cords.getX().compareTo(to.getX());
		int verticalMovement = cords.getY().compareTo(to.getY());
		if(verticalMovement == 0 || 0 == horizontalMovement) {
			return true;
		}
		if(Math.abs(to.getX() - cords.getX()) != Math.abs(to.getY() - cords.getY())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean beingBlocked(Location to) {
		return !board.clearLine(cords, to);
	}

    @Override
    public List<Location> allUnblockedMoves() {
        List<Location> moves = new LinkedList<Location>();

        for (int x = cords.getX(), y = cords.getY(); board.onBoard(x, y); x += 1, y += 1) {
            moves.add(new Location(x, y));
        }
        for (int x = cords.getX(), y = cords.getY(); board.onBoard(x, y); x += 1, y += -1) {
            moves.add(new Location(x, y));
        }
        for (int x = cords.getX(), y = cords.getY(); board.onBoard(x, y); x += -1, y += 1) {
            moves.add(new Location(x, y));
        }
        for (int x = cords.getX(), y = cords.getY(); board.onBoard(x, y); x += -1, y += -1) {
            moves.add(new Location(x, y));
        }

        return moves;
    }
}