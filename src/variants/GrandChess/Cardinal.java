package variants.GrandChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import variants.BasicChess.Bishop;
import variants.BasicChess.Knight;

import java.util.List;

/**
 * Created by hj1012 on 03/11/14.
 */
public class Cardinal extends ChessPiece {

	public Cardinal(Board board, PieceType type, Location cords) {
		super(board, type, cords, "cardinal");
	}

	@Override
	protected boolean validInState(Location to) {
		return Math.abs(cords.getX() - to.getX()) != Math.abs(cords.getY() - to.getY()) || board.clearLine(cords, to);
	}

	@Override
	public int returnValue() {
		return 9;
	}

	@Override
	public List<Location> allPieceMoves() {
		ChessPiece knight = new Knight(board, type, cords);
		ChessPiece bishop = new Bishop(board, type, cords);

		List<Location> moves = knight.allPieceMoves();
		moves.addAll(bishop.allPieceMoves());

		return moves;
	}

	@Override
	public String getName() {
		return "Cardinal";
	}
}
