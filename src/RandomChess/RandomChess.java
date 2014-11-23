package RandomChess;

import BasicChess.BasicChess;
import BasicChess.BasicChessFrame;
import BasicChess.BasicDecoder;
import main.GameController;
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
        return "Otherwise known as Fischer Random Chess, this variant employs the same board and pieces as standard chess; however, the starting position of the pieces on the players' home ranks is randomized. The name \"Chess960\" is derived from the number of possible starting positions. The random setup renders the prospect of obtaining an advantage through the memorization of opening lines impracticable, compelling players to rely on their talent and creativity.";
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
        graphics.tools.create(new BasicChessFrame("Random Chess", 600, 700, game.getBoard()));
        return true;
    }

    @Override
    public RandomChess recreate(GameMode mode) {
        return new RandomChess(mode);
    }
}
