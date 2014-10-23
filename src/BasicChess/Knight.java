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

		int oldx = cords.getX();
		int oldy = cords.getY();

		Location loc;
		loc = (new Location(oldx+1, oldy-2));
		if(board.onBoard(loc)) moves.add(loc);
		loc = (new Location(oldx+1, oldy+2));
		if(board.onBoard(loc)) moves.add(loc);
		loc = (new Location(oldx-1, oldy-2));
		if(board.onBoard(loc)) moves.add(loc);
		loc = (new Location(oldx-1, oldy+2));
		if(board.onBoard(loc)) moves.add(loc);

		loc = (new Location(oldx+2, oldy-1));
		if(board.onBoard(loc)) moves.add(loc);
		loc = (new Location(oldx+2, oldy+1));
		if(board.onBoard(loc)) moves.add(loc);
		loc = (new Location(oldx-2, oldy-1));
		if(board.onBoard(loc)) moves.add(loc);
		loc = (new Location(oldx-2, oldy+1));
		if(board.onBoard(loc)) moves.add(loc);

		return moves;
	}
}
