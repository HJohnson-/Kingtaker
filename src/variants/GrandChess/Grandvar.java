package variants.GrandChess;

import main.ChessVariant;
import main.GameController;
import variants.BasicChess.BasicDecoder;
import variants.BasicChess.BasicChessFrame;
import main.GameMode;
import pieces.PieceDecoder;

/**
 * The 'Grand Chess' variant. A 10x10 board with two new pieces on each side.
 */
public class Grandvar extends ChessVariant {
    @Override
    public int getVariationID() {
        return 2;
    }

    @Override
    public String getName() {
        return "Grand Chess";
    }

    @Override
    public String getDescription() {
        return "[grand chess rules]\n" +
               "[grand chess rules]\n";
    }

    @Override
    public PieceDecoder getDecoder() {
        return new GrandDecoder();
    }

    public Grandvar() {
        this(GameMode.MULTIPLAYER_LOCAL);
    }

    public Grandvar(GameMode mode) {
		game = new GameController(new GrandBoard(), 2, new BasicDecoder(), mode);
		game.getBoard().setController(game);
		game.getBoard().initializeBoard();
	}

	public Grandvar(GameController game) {
		this.game = game;
		this.game.getBoard().setController(game);
	}

	@Override
	public boolean drawBoard() {
		graphics.tools.create(new BasicChessFrame("Grand Chess", 700, 600, game.getBoard()));
		return true;
	}

    @Override
    public ChessVariant recreate(GameMode mode) {
        return new Grandvar(mode);
    }
}
