package variants.Hnefatafl;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by crix9 on 21/11/2014.
 */
public class Defender extends ChessPiece {

	public Defender(Board board, PieceType type, Location cords) {
		super(board, type, cords,"defender");
	}

	@Override
	protected boolean validInState(Location to) {
		return board.clearLine(cords, to) && !isHostile(to.getX(),to.getY());
	}

	@Override
	public boolean isValidMove(Location to, boolean careAboutCheck) {
        return validInState(to) && board.isEmptySpace(to) &&
                !board.formsStraightLine(cords, new Location(5,5), to);
    }

	private boolean isHostile(int row, int col) {
		return (row==0 && col==0) || (row==0 && col==10) ||
				(row==10 && col==0) || (row==10 && col==10) || row == 5 && col == 5;
	}

	private boolean goesThroughThrone(Location from, Location to){
		return true;
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
		return "Defender";
	}
}
