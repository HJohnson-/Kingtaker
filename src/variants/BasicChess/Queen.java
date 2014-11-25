package variants.BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import java.util.List;

/**
 * Default Queen
 */
public class Queen extends ChessPiece {
	public Queen(Board board, PieceType type, Location location) {
		super(board, type, location, "queen");
	}

	public int returnValue() {
		return 9;
	}

	@Override
	public boolean validInState(Location to) {
		return board.clearLine(cords, to);
	}

    @Override
    public List<Location> allPieceMoves() {
        ChessPiece rook = new Rook(board, type, cords);
        ChessPiece bishop = new Bishop(board, type, cords);

        List<Location> moves = rook.allPieceMoves();
        moves.addAll(bishop.allPieceMoves());

        return moves;
    }

	@Override
	public String getName() {
		return "Queen";
	}
}
