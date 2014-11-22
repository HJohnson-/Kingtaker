package BasicChess;

import graphics.tools;
import main.*;
import pieces.ChessPiece;
import pieces.PieceDecoder;

/**
 * The Chess we all know and love.
 */
public class BasicChess extends ChessVariant {

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

    public BasicChess() {
        this(GameMode.MULTIPLAYER_LOCAL);
    }

    public BasicChess(GameMode mode) {
		game = new GameController(new BasicBoard(), "Basic", new BasicDecoder(), mode);
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

    @Override
    public ChessVariant recreate(GameMode mode) {
        return new BasicChess(mode);
    }

}
