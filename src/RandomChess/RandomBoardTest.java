package RandomChess;

import main.Location;
import org.junit.Test;
import pieces.ChessPiece;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

public class RandomBoardTest {

	@Test
	public void testInitializeBoard() throws Exception {

		RandomBoard board = new RandomBoard();
		board.initializeBoard();

		HashMap<String, Integer> pieces = new HashMap<String, Integer>();
		HashMap<String, Location> pieceLocations = new LinkedHashMap<String, Location>();


		for(int i = 0; i < board.numRows(); i++){
			for(int j = 0; j< board.numCols(); j++){
				ChessPiece piece = board.getPiece(new Location(i,j));
				String pieceName = piece.getName();
				pieceLocations.put(pieceName,piece.cords);
				if(pieces.containsKey(pieceName)){
					pieces.put(pieceName, pieces.get(pieceName)+1);
				}else{
					pieces.put(pieceName,1);
				}

				if (pieceName == "Pawn") assertTrue(piece.cords.getX() == 1 || piece.cords.getX() == 6);
				else if (pieceName != "NOT A PIECE"){
					assertTrue(piece.cords.getX() == 0 || piece.cords.getX() == 7);
				}

			}
		}

		assertEquals(new Integer(16), pieces.get("Pawn"));
		assertEquals(new Integer(2), pieces.get("King"));
		assertEquals(new Integer(2), pieces.get("Queen"));
		assertEquals(new Integer(4), pieces.get("Rook"));
		assertEquals(new Integer(4), pieces.get("Knight"));
		assertEquals(new Integer(4), pieces.get("Bishop"));
		assertEquals(new Integer(32), pieces.get("NOT A PIECE"));

	}
}