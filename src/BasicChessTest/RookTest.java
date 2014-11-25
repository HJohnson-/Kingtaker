package BasicChessTest;

import BasicChess.BasicBoard;
import BasicChess.Rook;
import main.Location;
import main.PieceType;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class RookTest {

	@Test
	public void testReturnValue() throws Exception {
		BasicBoard board = new BasicBoard();
		Rook rook = new Rook(board, PieceType.BLACK, new Location(7,7));
		assertEquals(5, rook.returnValue());
	}


	@Test
	public void testAllPieceMoves() throws Exception {
		BasicBoard board = new BasicBoard();
		Rook rook = new Rook(board, PieceType.BLACK, new Location(7,7));
		board.initializeBoard();

		List<Location> moves = new LinkedList<Location>();
		for (int x = 0; x < board.numCols(); x++) {
			moves.add(new Location(x, rook.cords.getY()));
		}
		for (int y = 0; y < board.numRows(); y++) {
			moves.add(new Location(rook.cords.getX(), y));
		}

		Location[] locs = moves.toArray(new Location[moves.size()]);
		List<Location> obtained = rook.allPieceMoves();
		assertArrayEquals(locs, obtained.toArray(new Location[obtained.size()]));
	}


	@Test
	public void testAllPieceMoves2() throws Exception {
		BasicBoard board = new BasicBoard();
		Rook rook = new Rook(board, PieceType.BLACK, new Location(0,7));
		board.initializeBoard();


		List<Location> actual = rook.allPieceMoves();
		assertEquals(new Integer(16), new Integer(actual.size()));
		for(int i = 0; i <board.numCols(); i++){
			assertTrue(actual.contains(new Location(0,i)));
		}
		for(int j = 0; j<board.numRows(); j++){
			assertTrue(actual.contains(new Location(j,7)));
		}
	}

	@Test
	public void testGetName() throws Exception {
		BasicBoard board = new BasicBoard();
		Rook rook = new Rook(board, PieceType.BLACK, new Location(7,7));
		assertEquals("Rook",rook.getName());
	}
}