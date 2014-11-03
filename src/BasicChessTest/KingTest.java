package BasicChessTest;

import BasicChess.BasicBoard;
import BasicChess.King;
import main.Location;
import main.PieceType;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;

public class KingTest {

    @Test
    public void testReturnValue() throws Exception {
        BasicBoard board = new BasicBoard();
        King king = new King(board, PieceType.BLACK, new Location(7,3));

        Field value = King.class.getDeclaredField("REALLY_HIGH_NUMBER");
        value.setAccessible(true);

        int val = value.getInt(king);

        assertEquals(new Integer(val), new Integer(king.returnValue()));
    }

    @Test
    public void testExecuteMove() throws Exception {

    }

    @Test
    public void testIsValidMove() throws Exception {

    }

    @Test
    public void testValidInStateNoCastle() throws Exception {

    }

    @Test
    public void testValidInState() throws Exception {

    }

    @Test
    public void testAllPieceMoves() throws Exception {

    }

    @Test
    public void testGetName() throws Exception {

    }
}