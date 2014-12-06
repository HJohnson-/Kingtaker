package variants.Hnefatafl;

import pieces.ChessPiece;
import variants.BasicChess.Rook;
import main.Board;
import main.Location;
import main.PieceType;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by crix9 on 21/11/2014.
 */
public class Attacker extends ChessPiece{

	public Attacker(Board board, PieceType type, Location cords) {
		super(board, type, cords, "attacker");
	}

	@Override
	protected boolean validInState(Location to) { //todo: make sure they cannot go through the throne
		return board.clearLine(cords, to) && !isHostile(to.getX(),to.getY());
	}

	@Override
	public boolean isValidMove(Location to, boolean careAboutCheck) {
		return validInState(to) && board.isEmptySpace(to);
	}

	private boolean isHostile(int row, int col) {
		return (row==0 && col==0) || (row==0 && col==10) ||
				(row==10 && col==0) || (row==10 && col==10) || row == 5 && col == 5;
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
	public int returnValue() {
		return 9;
	}

	@Override
	public String getName() {
		return "Attacker";
	}
}
