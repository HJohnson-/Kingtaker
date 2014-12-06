package GrandChessTest;

import variants.GrandChess.Cardinal;
import variants.GrandChess.GrandBoard;
import main.Location;
import main.PieceType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CardinalTest {
	GrandBoard board = new GrandBoard();
	Cardinal card = new Cardinal(board, PieceType.BLACK, new Location(1,6));
	@Test
	public void testReturnValue() throws Exception {
		assertEquals(9, card.returnValue());
	}

	@Test
	public void testAllPieceMoves() throws Exception {
		List<Location> actual = card.allPieceMoves();

		for (int x = card.cords.getX(), y = card.cords.getY(); board.onBoard(new Location(x, y)); x += 1, y += 1) {
			assertTrue(actual.contains(new Location(x, y)));
		}
		for (int x = card.cords.getX(), y = card.cords.getY(); board.onBoard(new Location(x, y)); x += 1, y += -1) {
			assertTrue(actual.contains(new Location(x, y)));
		}
		for (int x = card.cords.getX(), y = card.cords.getY(); board.onBoard(new Location(x, y)); x += -1, y += 1) {
			assertTrue(actual.contains(new Location(x, y)));
		}
		for (int x = card.cords.getX(), y = card.cords.getY(); board.onBoard(new Location(x, y)); x += -1, y += -1) {
			assertTrue(actual.contains(new Location(x, y)));
		}

		for (int x = -2; x <= 2; x++) {
			if (x == 0) continue;
			if (card.cords.getX() + x < 0) continue;
			if (card.cords.getX() + x >= board.numCols()) break;

			int y = 3 - Math.abs(x);
			Location l1 = new Location(card.cords.getX() + x, card.cords.getY() + y);
			Location l2 = new Location(card.cords.getX() + x, card.cords.getY() - y);

			if (board.onBoard(l1)) assertTrue(actual.contains(l1));
			if (board.onBoard(l2)) assertTrue(actual.contains(l2));
		}

	}

	@Test
	public void testAllPieceMoves2() throws Exception{
		Cardinal cardinal = new Cardinal(board, PieceType.BLACK, new Location(4,4));
		List<Location> actuals = cardinal.allPieceMoves();

		for(int i = 0; i < 10; i++){
			assertTrue(actuals.contains(new Location(i,i)));
		}

		for(int i = 0; i <9; i++){
			for(int j= 0; j<9; j++){
				if(i+j ==8){
					assertTrue(actuals.contains(new Location(i,j)));
				}
			}
		}

		assertTrue(actuals.contains(new Location(2,3)));
		assertTrue(actuals.contains(new Location(2,5)));
		assertTrue(actuals.contains(new Location(3,2)));
		assertTrue(actuals.contains(new Location(3,6)));
		assertTrue(actuals.contains(new Location(5,6)));
		assertTrue(actuals.contains(new Location(5,6)));
		assertTrue(actuals.contains(new Location(6,3)));
		assertTrue(actuals.contains(new Location(6,5)));
	}

	@Test
	public void testGetName() throws Exception {
		assertEquals("Cardinal", card.getName());
	}
}