package BasicChessTest;

import main.Location;
import main.PieceType;
import org.junit.Test;
import pieces.ChessPiece;
import variants.BasicChess.BasicBoard;
import variants.BasicChess.Queen;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QueenTest {

	@Test
	public void testReturnValue() throws Exception {
		BasicBoard board = new BasicBoard();
		ChessPiece queen = new Queen(board, PieceType.BLACK, new Location(0,4));
		assertEquals(9, queen.returnValue());
	}

	@Test
	public void testAllPieceMoves() throws Exception {
		BasicBoard board = new BasicBoard();
		ChessPiece queen = new Queen(board, PieceType.BLACK, new Location(0,4));
        List<Location> actual = queen.allPieceMoves();

		//vertical,horizontal moves
		for(int i = 0; i <board.numCols(); i++){
			assertTrue(actual.contains(new Location(queen.cords.getX(),i)));
		}
		for(int j = 0; j<board.numRows(); j++){
			assertTrue(actual.contains(new Location(j,queen.cords.getY())));
		}

		//diagonal moves
		for (int x = queen.cords.getX(), y = queen.cords.getY(); board.onBoard(new Location(x, y)); x += 1, y += 1) {
			assertTrue(actual.contains(new Location(x, y)));
		}
		for (int x = queen.cords.getX(), y = queen.cords.getY(); board.onBoard(new Location(x, y)); x += 1, y += -1) {
			assertTrue(actual.contains(new Location(x, y)));
		}
		for (int x = queen.cords.getX(), y = queen.cords.getY(); board.onBoard(new Location(x, y)); x += -1, y += 1) {
			assertTrue(actual.contains(new Location(x, y)));
		}
		for (int x = queen.cords.getX(), y = queen.cords.getY(); board.onBoard(new Location(x, y)); x += -1, y += -1) {
			assertTrue(actual.contains(new Location(x, y)));
		}

	}

	@Test
	public void testGetName() throws Exception {
		BasicBoard board = new BasicBoard();
		ChessPiece queen = new Queen(board, PieceType.BLACK, new Location(0,4));
		assertEquals("Queen", queen.getName());
	}
}