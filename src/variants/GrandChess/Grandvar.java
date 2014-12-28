package variants.GrandChess;

import graphics.GraphicsTools;
import main.ChessVariant;
import main.GameController;
import main.GameMode;
import pieces.PieceDecoder;
import variants.BasicChess.BasicChessFrame;
import variants.BasicChess.BasicDecoder;

import java.util.Random;

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
        return "Grand chess is played on a larger, 10x10 board and introduces two new characters: the Marshall, which combines the powers of the Rook and Knight and the Cardinal, which combines the powers of the Bishop and Knight. There is no castling.";
    }

    @Override
    public PieceDecoder getDecoder() {
        return new GrandDecoder();
    }

    public Grandvar() {
        this(GameMode.MULTIPLAYER_LOCAL);
    }

    public Grandvar(GameMode mode) {
        GrandBoard board = new GrandBoard();
        board.initializeBoard();
		game = new GameController(board, getVariationID(), new GrandDecoder(), mode, new Random().nextBoolean());
		game.getBoard().setController(game);
	}

	public Grandvar(GameController game) {
		this.game = game;
		this.game.getBoard().setController(game);
	}

	@Override
	public boolean drawBoard() {
		GraphicsTools.create(new BasicChessFrame(getName(), 500, 700, game.getBoard()));
		return true;
	}

    @Override
    public ChessVariant recreate(GameMode mode) {
        return new Grandvar(mode);
    }
}
