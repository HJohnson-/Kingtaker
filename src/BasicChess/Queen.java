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
public class Queen extends ChessPiece {
	public Queen(Board board, PieceType type, Location location) {
		super(board, type, location, "queen");
	}

	public int returnValue() {
		return 9;
	}

	@Override
	protected boolean invalidTarget(Location to) {
		int horizontalMovement = to.getX().compareTo(cords.getX());
		int verticalMovement = to.getY().compareTo(cords.getY());
		if(verticalMovement == 0 && horizontalMovement == 0) {
			return true;
		}
		if((Math.abs(to.getX() - cords.getX()) != Math.abs(to.getY() - cords.getY()))
				&& (Math.abs(verticalMovement) == Math.abs(horizontalMovement))
				) {
			return true;
		}
		return false;
	}

	//The queen should not use this function as it overwrites isValidMove entirely.
	@Override
	public boolean beingBlocked(Location to) {
		return !board.clearLine(cords, to);
	}

    @Override
    public List<Location> allUnblockedMoves() {
        ChessPiece rook = new Rook(board, type, cords);
        ChessPiece bishop = new Bishop(board, type, cords);

        List<Location> moves = rook.allUnblockedMoves();
        moves.addAll(bishop.allUnblockedMoves());

        return moves;
    }
}
