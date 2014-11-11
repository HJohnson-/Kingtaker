package RandomChess;

import BasicChess.BasicChess;
import graphics.tools;
import main.GameController;
import BasicChess.BasicDecoder;
import BasicChess.BasicChessFrame;
import main.GameMode;

/**
 * Implements the Random960 chess variant.
 */
public class RandomChess extends BasicChess {

    @Override
    public int getVariationID() {
        return 1;
    }

    @Override
    public String getName() {
        return "Chess960";
    }

    @Override
    public String getDescription() {
        return "[random chess description]";
    }

	public RandomChess(){
		this(GameMode.MULTIPLAYER_LOCAL);
	}

    public RandomChess(GameMode mode) {
        game = new GameController(new RandomBoard(), "Random960", new BasicDecoder(), mode);
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

    @Override
    public RandomChess recreate(GameMode mode) {
        return new RandomChess(mode);
    }
}
