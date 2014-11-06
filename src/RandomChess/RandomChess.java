package RandomChess;

import BasicChess.BasicChess;
import graphics.tools;
import main.GameController;
import BasicChess.BasicDecoder;
import BasicChess.BasicChessFrame;

/**
 * Implements the Random960 chess variant.
 */
public class RandomChess extends BasicChess {

	public RandomChess(){
		game = new GameController(new RandomBoard(), "Random960", new BasicDecoder());
		game.getBoard().setController(game);
		game.getBoard().initializeBoard();
	}

	public RandomChess(GameController game) {
		this.game = game;
		this.game.getBoard().setController(game);
	}

    @Override
    public boolean drawBoard() {
        graphics.tools.create(new BasicChessFrame("Random Chess", 700, 625, game.getBoard()));
        return true;
    }
}
