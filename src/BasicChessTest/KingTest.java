package BasicChessTest;

import junit.framework.TestCase;
import main.Location;
import main.PieceType;
import variants.BasicChess.BasicBoard;
import variants.BasicChess.King;

import java.lang.reflect.Field;


public class KingTest extends TestCase {

    public void testReturnValue() throws Exception {
        BasicBoard board = new BasicBoard();
        King king = new King(board, PieceType.BLACK, new Location(7,3));

        Field value = King.class.getDeclaredField("REALLY_HIGH_NUMBER");
        value.setAccessible(true);

        int val = value.getInt(king);

        assertEquals(new Integer(val), new Integer(king.returnValue()));

    }

    public void testExecuteMove() throws Exception {

    }

    public void testIsValidMove() throws Exception {

    }

    public void testValidInStateNoCastle() throws Exception {

    }

    public void testValidInState() throws Exception {

    }

    public void testAllPieceMoves() throws Exception {

    }

    public void testGetName() throws Exception {
		BasicBoard board = new BasicBoard();
		King king = new King(board, PieceType.BLACK, new Location(7,3));
		assertEquals("King",king.getName());
    }
}