package variants.GrandChessTest;

import variants.GrandChess.GrandBoard;
import variants.GrandChess.Marshal;
import main.Location;
import main.PieceType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MarshalTest {
	GrandBoard board = new GrandBoard();
	Marshal marshal = new Marshal(board, PieceType.BLACK, new Location(1,5));

	@Test
	public void testReturnValue() throws Exception {
		assertEquals(9, marshal.returnValue());
	}

	@Test
	public void testAllPieceMoves() throws Exception {
		List<Location> actual = marshal.allPieceMoves();

		for(int i = 0; i <board.numCols(); i++){
			assertTrue(actual.contains(new Location(marshal.cords.getX(),i)));
		}
		for(int j = 0; j<board.numRows(); j++){
			assertTrue(actual.contains(new Location(j,marshal.cords.getY())));
		}

		for (int x = -2; x <= 2; x++) {
			if (x == 0) continue;
			if (marshal.cords.getX() + x < 0) continue;
			if (marshal.cords.getX() + x >= board.numCols()) break;

			int y = 3 - Math.abs(x);
			Location l1 = new Location(marshal.cords.getX() + x, marshal.cords.getY() + y);
			Location l2 = new Location(marshal.cords.getX() + x, marshal.cords.getY() - y);

			if (board.onBoard(l1)) assertTrue(actual.contains(l1));
			if (board.onBoard(l2)) assertTrue(actual.contains(l2));
		}
	}

	@Test
	public void testAllPieceMoves2() throws Exception {
		Marshal marshal = new Marshal(board, PieceType.BLACK, new Location(4,4));
		List<Location> actual = marshal.allPieceMoves();

		assertEquals(28, actual.size());

		assertTrue(actual.contains(new Location(1,4)));
		assertTrue(actual.contains(new Location(2,4)));
		assertTrue(actual.contains(new Location(3,4)));
		assertTrue(actual.contains(new Location(4,4)));
		assertTrue(actual.contains(new Location(5,4)));
		assertTrue(actual.contains(new Location(6,4)));
		assertTrue(actual.contains(new Location(7,4)));
		assertTrue(actual.contains(new Location(8,4)));
		assertTrue(actual.contains(new Location(9,4)));


		assertTrue(actual.contains(new Location(4,1)));
		assertTrue(actual.contains(new Location(4,2)));
		assertTrue(actual.contains(new Location(4,3)));
		assertTrue(actual.contains(new Location(4,4)));
		assertTrue(actual.contains(new Location(4,5)));
		assertTrue(actual.contains(new Location(4,6)));
		assertTrue(actual.contains(new Location(4,7)));
		assertTrue(actual.contains(new Location(4,8)));
		assertTrue(actual.contains(new Location(4,9)));

		assertTrue(actual.contains(new Location(2,3)));
		assertTrue(actual.contains(new Location(2,5)));
		assertTrue(actual.contains(new Location(3,2)));
		assertTrue(actual.contains(new Location(3,6)));
		assertTrue(actual.contains(new Location(5,6)));
		assertTrue(actual.contains(new Location(5,6)));
		assertTrue(actual.contains(new Location(6,3)));
		assertTrue(actual.contains(new Location(6,5)));

	}

	@Test
	public void testAllPieceMoves3() throws Exception {
		Marshal marshal = new Marshal(board, PieceType.BLACK, new Location(9,9));
		List<Location> actual = marshal.allPieceMoves();

		assertEquals(22, actual.size());

		for(int i = 0; i <board.numCols(); i++){
			assertTrue(actual.contains(new Location(marshal.cords.getX(),i)));
		}
		for(int j = 0; j<board.numRows(); j++){
			assertTrue(actual.contains(new Location(j,marshal.cords.getY())));
		}

		assertTrue(actual.contains(new Location(7,8)));
		assertTrue(actual.contains(new Location(8,7)));

	}

	@Test
	public void testGetName() throws Exception {
		assertEquals("Marshal", marshal.getName());
	}
}