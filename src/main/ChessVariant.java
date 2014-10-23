package main;

/**
 * Created by hj1012 on 15/10/14.
 */
abstract public class ChessVariant {
	public GameController game;

	//return true if move successful
	//logic handling if the move is valid has been moved to attemptMove
	public boolean move(Location pieceLocation, Location targetLocation) {
		try {
			game.attemptMove(pieceLocation, targetLocation);
			return true;
		} catch (Error e) {
			System.out.println(e);
			return false;
		}
	}


	//returns true if there was no errors
	abstract public boolean drawBoard();
}
