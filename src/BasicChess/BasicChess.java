package BasicChess;

import main.*;

/**
 * The Chess we all know and love.
 */
public class BasicChess extends ChessVariant {

	public BasicChess(){
		game = new GameController(new BasicBoard(), "Basic");
		game.getBoard().setController(game);
	}

	//returns true if there was no errors
	public boolean drawBoard() {
        graphics.tools.create(new BasicChessFrame("Basic Chess", 600, 600, (BasicBoard) game.getBoard()));
        return true;
	}
}
