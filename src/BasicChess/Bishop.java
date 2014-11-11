package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import java.util.LinkedList;
import java.util.List;

/**
 * Default Bishop
 */
public class Bishop extends ChessPiece {
	public Bishop(Board board, PieceType type, Location cords) {
		super(board, type, cords, "bishop");
	}

	public int returnValue() {
		return 3;
	}

	@Override
	public boolean validInState(Location to) {
		return board.clearLine(cords, to);
	}

    @Override
    public List<Location> allPieceMoves() {
        List<Location> moves = new LinkedList<Location>();

        for (int x = cords.getX(), y = cords.getY(); board.onBoard(new Location(x, y)); x += 1, y += 1) {
            moves.add(new Location(x, y));
        }
        for (int x = cords.getX(), y = cords.getY(); board.onBoard(new Location(x, y)); x += 1, y += -1) {
            moves.add(new Location(x, y));
        }
        for (int x = cords.getX(), y = cords.getY(); board.onBoard(new Location(x, y)); x += -1, y += 1) {
            moves.add(new Location(x, y));
        }
        for (int x = cords.getX(), y = cords.getY(); board.onBoard(new Location(x, y)); x += -1, y += -1) {
            moves.add(new Location(x, y));
        }

        return moves;
    }

	@Override
	public String getName() {
		return "Bishop";
	}

    @Override
    public ChessPiece clone() {
        return new Bishop(board, type, cords);
    }
}