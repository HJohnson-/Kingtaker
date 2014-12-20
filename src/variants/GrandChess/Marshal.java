package variants.GrandChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import variants.BasicChess.Knight;
import variants.BasicChess.Rook;

import java.util.List;

/**
 * Created by hj1012 on 03/11/14.
 */
public class Marshal extends ChessPiece {

	public Marshal(Board board, PieceType type, Location cords) {
		super(board, type, cords, "marshal");
	}

	@Override
	protected boolean validInState(Location to) {
		if(cords.getX() == to.getX() || cords.getY() == to.getY()) {
			return board.clearLine(cords, to);
		} else {
			return true;
		}
	}

	@Override
	public int returnValue() {
		return 9;
	}

	@Override
	public List<Location> allPieceMoves() {
		ChessPiece knight = new Knight(board, type, cords);
		ChessPiece rook = new Rook(board, type, cords);

		List<Location> moves = knight.allPieceMoves();
		moves.addAll(rook.allPieceMoves());

		return moves;
	}

	@Override
	public String getName() {
		return "Marshal";
	}

}
