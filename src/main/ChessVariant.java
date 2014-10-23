package main;

/**
 * The only class that is interacted with by the main function.
 * GameController objected should be initialized with a board, then a drawBoard function sets up a way for the board
 * to be interacted with an updated.
 */
abstract public class ChessVariant {
	public GameController game;

	//returns true if there was no errors
	abstract public boolean drawBoard();
}
