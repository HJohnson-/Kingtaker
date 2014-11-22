package GrandChess;

import BasicChess.BasicChessFrame;
import BasicChess.BasicDecoder;
import main.ChessVariant;
import main.GameController;
import main.GameMode;
import pieces.PieceDecoder;

/**
 * Created by hj1012 on 03/11/14.
 */
public class GrandChess extends ChessVariant {
    @Override
    public int getVariationID() {
        return 2;
    }

    @Override
    public String getName() {
        return "Grand Chess";
    }

    //TODO: make pawn promotion optional on 8th/9th rank
    @Override
    public String getDescription() {
        return "Grand chess is played on a larger, 10x10 board and introduces two new characters: the Marshall, which combines the powers of the Rook and Knight and the Cardinal, which combines the powers of the Bishop and Knight. Pawn promotion is optional on moving to the 8th or 9th rank, but compulsary on moving to the 10th rank. There is no castling.";
    }

    @Override
    public PieceDecoder getDecoder() {
        return new GrandDecoder();
    }

    public GrandChess() {
        this(GameMode.MULTIPLAYER_LOCAL);
    }

    public GrandChess(GameMode mode) {
		game = new GameController(new GrandBoard(), "Grand", new BasicDecoder(), mode);
		game.getBoard().setController(game);
		game.getBoard().initializeBoard();
	}

	public GrandChess(GameController game) {
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
        return new GrandChess(mode);
    }
}
