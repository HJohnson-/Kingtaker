package variants.BasicChess;

import graphics.GraphicsTools;
import main.ChessVariant;
import main.GameController;
import main.GameMode;
import pieces.PieceDecoder;

import java.util.Random;

/**
 * The Chess we all know and love.
 */
public class BasicChessvar extends ChessVariant {

    @Override
    public int getVariationID() {
        return 0;
    }

    @Override
    public String getName() {
        return "Standard Chess";
    }

    @Override
    public String getDescription() {
        return "Standard chess rules, as governed by the World Chess Organisation. Basic moves, as well as en passant, castling and pawn promotion are valid. For an overview, read http://en.wikipedia.org/wiki/Rules_of_chess";
    }

    @Override
    public PieceDecoder getDecoder() {
        return new BasicDecoder();
    }

    public BasicChessvar() {
        this(GameMode.MULTIPLAYER_LOCAL);
    }

    public BasicChessvar(GameMode mode) {
		game = new GameController(new BasicBoard(), 0, new BasicDecoder(), mode, new Random().nextBoolean());
		game.getBoard().setController(game);
		game.getBoard().initializeBoard();
	}

	public BasicChessvar(GameController game) {
		this.game = game;
		this.game.getBoard().setController(game);
	}

	//returns true if there was no errors
	public boolean drawBoard() {
        GraphicsTools.create(new BasicChessFrame("Basic Chess", 500, 700, game.getBoard()));
        return true;
	}

    @Override
    public ChessVariant recreate(GameMode mode) {
        return new BasicChessvar(mode);
    }

}
