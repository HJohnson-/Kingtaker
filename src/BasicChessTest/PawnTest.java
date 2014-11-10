package BasicChessTest;

import BasicChess.BasicBoard;
import BasicChess.King;
import BasicChess.Pawn;
import main.Location;
import main.PieceType;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class PawnTest {

	BasicBoard board = new BasicBoard();
	Pawn pawn = new Pawn(board, PieceType.BLACK, new Location(1,1));

	@Test
	public void testReturnValue() throws Exception {
		assertEquals(1,pawn.returnValue());
	}

	@Test
	public void testExecuteMove() throws Exception {

	}

	@Test
	public void testTestIfMoveEndsInCheck() throws Exception {

	}

	@Test
	public void testAllPieceMoves() throws Exception {

	}

	@Test
	public void testGetMisc() throws Exception {
		Field value = Pawn.class.getDeclaredField("justDidADoubleMove");
		value.setAccessible(true);

		value.set(pawn,true);

		assertEquals("T", pawn.getMisc());

		value.set(pawn,false);
		assertEquals("F", pawn.getMisc());

	}

	@Test
	public void testGetName() throws Exception {
		assertEquals("Pawn",pawn.getName());
	}
}