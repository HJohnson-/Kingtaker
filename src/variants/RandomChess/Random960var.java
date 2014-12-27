package variants.RandomChess;

import graphics.GraphicsTools;
import main.GameController;
import main.GameMode;
import variants.BasicChess.BasicChessFrame;
import variants.BasicChess.BasicChessvar;
import variants.BasicChess.BasicDecoder;

import java.util.Random;

/**
 * Implements the Random960 chess variant.
 */
public class Random960var extends BasicChessvar {

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

	public Random960var(){
		this(GameMode.MULTIPLAYER_LOCAL);
	}

    public Random960var(GameMode mode) {
        game = new GameController(new RandomBoard(), 1, new BasicDecoder(), mode, new Random().nextBoolean());
        game.getBoard().setController(game);
        game.getBoard().initializeBoard();
    }

	public Random960var(GameController game) {
		this.game = game;
		this.game.getBoard().setController(game);
	}

    @Override
    public boolean drawBoard() {
        GraphicsTools.create(new BasicChessFrame("Random Chess", 500, 700, game.getBoard()));
        return true;
    }

    @Override
    public Random960var recreate(GameMode mode) {
        return new Random960var(mode);
    }
}
