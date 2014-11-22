package BasicChessTest;

import BasicChess.BasicBoard;
import BasicChess.Bishop;
import main.Location;
import main.PieceType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BishopTest {

	BasicBoard board = new BasicBoard();
	Bishop bishop = new Bishop(board, PieceType.BLACK, new Location(0,5));

	@Test
	public void testReturnValue() throws Exception {
		assertEquals(3, bishop.returnValue());
	}

	@Test
	public void testAllPieceMoves() throws Exception {
		List<Location> actual = bishop.allPieceMoves();
		for (int x = bishop.cords.getX(), y = bishop.cords.getY(); board.onBoard(new Location(x, y)); x += 1, y += 1) {
			assertTrue(actual.contains(new Location(x, y)));
		}
		for (int x = bishop.cords.getX(), y = bishop.cords.getY(); board.onBoard(new Location(x, y)); x += 1, y += -1) {
			assertTrue(actual.contains(new Location(x, y)));
		}
		for (int x = bishop.cords.getX(), y = bishop.cords.getY(); board.onBoard(new Location(x, y)); x += -1, y += 1) {
			assertTrue(actual.contains(new Location(x, y)));
		}
		for (int x = bishop.cords.getX(), y = bishop.cords.getY(); board.onBoard(new Location(x, y)); x += -1, y += -1) {
			assertTrue(actual.contains(new Location(x, y)));
		}
	}

	@Test
	public void testGetName() throws Exception {

		Bishop bishop = new Bishop(board, PieceType.BLACK, new Location(0,5));

		assertEquals("Bishop", bishop.getName());
	}
}