package BasicChessTest;

import main.Location;
import main.PieceType;
import org.junit.Test;
import variants.BasicChess.BasicBoard;
import variants.BasicChess.Knight;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KnightTest {

	BasicBoard board = new BasicBoard();
	Knight knight = new Knight(board, PieceType.BLACK,new Location(0,6));

	@Test
	public void testReturnValue() throws Exception {
		assertEquals(3, knight.returnValue());
	}

	@Test
	public void testAllPieceMoves() throws Exception {
		List<Location> actual = knight.allPieceMoves();

		assertTrue(actual.contains(new Location(1,4)));
		assertTrue(actual.contains(new Location(2,5)));
		assertTrue(actual.contains(new Location(2,7)));
		assertEquals(3,actual.size());
	}

	@Test
	public void testAllPieceMoves2() throws Exception {
		Knight knight = new Knight(board, PieceType.WHITE,new Location(7,7));
		List<Location> actual = knight.allPieceMoves();


		assertTrue(actual.contains(new Location(6,5)));
		assertTrue(actual.contains(new Location(5,6)));
		assertEquals(2,actual.size());
	}

	@Test
	public void testGetName() throws Exception {
		assertEquals("Knight", knight.getName());
	}
}