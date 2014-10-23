package BasicChess;

import pieces.ChessPiece;
import main.Location;
import main.PieceType;
import main.Board;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hj1012 on 15/10/14.
 */
public class Rook extends ChessPiece {

	public Rook(Board board, PieceType type, Location cords) {
		super(board, type, cords, "rook");
	}

	public int returnValue() {
		return 5;
	}

	@Override
	public boolean invalidTarget(Location to) {
		int horizontalMovement = to.getX().compareTo(cords.getX());
		int verticalMovement = to.getY().compareTo(cords.getY());
		if(Math.abs(verticalMovement) == Math.abs(horizontalMovement)) {
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
        for (int x = 0; x < board.numCols(); x++) {
            moves.add(new Location(x, cords.getY()));
        }
        for (int y = 0; y < board.numRows(); y++) {
            moves.add(new Location(cords.getX(), y));
        }
        return moves;
    }
}
