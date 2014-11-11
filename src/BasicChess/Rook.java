package BasicChess;

import pieces.ChessPiece;
import main.Location;
import main.PieceType;
import main.Board;

import java.util.LinkedList;
import java.util.List;

/**
 * Default Rook
 */
public class Rook extends ChessPiece {

	public Rook(Board board, PieceType type, Location cords) {
		super(board, type, cords, "rook");
	}

	public int returnValue() {
		return 5;
	}

	@Override
	public boolean validInState(Location to) {
		return board.clearLine(cords, to);
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
        return moves;
    }

	@Override
	public String getName() {
		return "Rook";
	}

    @Override
    public ChessPiece clone() {
        return new Rook(board, type, cords);
    }

}
