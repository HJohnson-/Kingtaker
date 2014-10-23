package BasicChess;

import main.Location;
import org.junit.Test;

import static org.junit.Assert.*;

public class RookTest {

	@Test
	public void testReturnValue()  {
		BasicChess chess = new BasicChess();
		chess.initializeBoard();
		Rook rook = (Rook)chess.board.getPiece(new Location(7,7));

		assertEquals(5, rook.returnValue());

		rook = (Rook)chess.board.getPiece(new Location(7,0));
		assertEquals(5,rook.returnValue());

		rook = (Rook)chess.board.getPiece(new Location(0,7));
		assertEquals(5,rook.returnValue());

		rook = (Rook)chess.board.getPiece(new Location(0,0));

		assertEquals(5, rook.returnValue());
	}

	@Test
	public void testPositiveInvalidTarget() throws Exception {
		BasicChess chess = new BasicChess();
		chess.initializeBoard();
		Rook rook = (Rook) chess.board.getPiece(new Location(7, 7));

		//rooks can only move horizontally or vertically
		Location location;
		for (int i = 0; i <= 7; i++) {
			for(int j = 0; j <= 7; j++){
				location = new Location(i,j);
				if(rook.cords.getX() != location.getX() &&
						rook.cords.getY() != location.getY()){
							assertEquals(true, rook.invalidTarget(location));
				}

			}

		}
	}

	@Test
	public void testNegativeInvalidTarget() throws Exception {
		BasicChess chess = new BasicChess();
		chess.initializeBoard();
		Rook rook = (Rook) chess.board.getPiece(new Location(7, 7));

		//rooks can horizontally or vertically
		Location location;
		for (int i = 6; i >= 0; i--) {
			location = new Location(7,i);
			assertEquals(false, rook.invalidTarget(location));
		}

		for (int i = 6; i >= 0; i--) {
			location = new Location(i,7);
			assertEquals(false, rook.invalidTarget(location));
		}
	}

	@Test
	public void testPositiveBeingBlocked() throws Exception {
		BasicChess chess = new BasicChess();
		chess.initializeBoard();
		Rook rook = (Rook) chess.board.getPiece(new Location(7, 7));


		//cannot move through its own pieces
		Location to = new Location(7, 5);
		assertEquals(false, rook.invalidTarget(to));
		assertEquals(true, rook.beingBlocked(to));

	}

	@Test
	public void testNegativeBeingBlocked() throws Exception {
		BasicChess chess = new BasicChess();
		chess.initializeBoard();
		Rook rook = (Rook) chess.board.getPiece(new Location(7, 7));

		chess.board.movePiece(new Location(7,7), new Location(4,7));


		Location to = new Location(5, 7);
		assertEquals(false, rook.invalidTarget(to));
		assertEquals(false, rook.beingBlocked(to));

	}
}