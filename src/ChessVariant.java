/**
 * Created by hj1012 on 15/10/14.
 */
abstract public class ChessVariant {
	private Board board;
	private boolean isWhitesTurn;

	//returns true if human player goes first in offline mode or the lobby host goes first in online mode.
	abstract public boolean initializeBoard();

	public boolean move(Location pieceLocation, Location targetLocation) {
		try {
			Piece beingMoved = board.getPiece(pieceLocation);
			if(beingMoved.isWhite != isWhitesTurn) {
				throw new Error("Wrong Colour Piece exception");
			}
			if (beingMoved.canYouMoveTo(pieceLocation, targetLocation)) {
				board.executeMove(pieceLocation, targetLocation);
			}
			return true;
		} catch (Error t) {
			return false;
		}
	}


	//returns true if there was no errors
	abstract public boolean drawBoard(boolean fromWhitePerspective);
}
