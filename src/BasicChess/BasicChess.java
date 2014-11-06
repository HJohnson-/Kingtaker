package BasicChess;

import graphics.tools;
import main.*;

/**
 * The Chess we all know and love.
 */
public class BasicChess extends ChessVariant {

	public BasicChess(){
		game = new GameController(new BasicBoard(), "Basic", new BasicDecoder());
		game.getBoard().setController(game);
		game.getBoard().initializeBoard();
	}

	public BasicChess(GameController game) {
		this.game = game;
		this.game.getBoard().setController(game);
	}

	//returns true if there was no errors
	public boolean drawBoard() {
        graphics.tools.create(new BasicChessFrame("Basic Chess", 700, 625, game.getBoard()));
        return true;
	}
}
