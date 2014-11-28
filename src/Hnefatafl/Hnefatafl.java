package Hnefatafl;

import BasicChess.BasicChessFrame;
import main.ChessVariant;
import main.GameController;
import main.GameMode;
import pieces.PieceDecoder;

/**
 * Created by crix9 on 21/11/2014.
 */
public class Hnefatafl extends ChessVariant {
	@Override
	public int getVariationID() {
		return 3;
	}

	@Override
	public String getName() {
		return "Hnefatafl";
	}

	@Override
	public String getDescription() { return "[Hnefatafl rules]\n"; }

	@Override
	public PieceDecoder getDecoder() {
		return new HnefataflDecoder();
	}

	public Hnefatafl() {
		this(GameMode.MULTIPLAYER_LOCAL);
	}

	public Hnefatafl(GameMode mode) {
		game = new GameController(new HnefataflBoard(), "Hnefatafl", new HnefataflDecoder(), mode);
		game.getBoard().setController(game);
		game.getBoard().initializeBoard();
	}

	public Hnefatafl(GameController game) {
		this.game = game;
		this.game.getBoard().setController(game);
	}

	@Override
	public boolean drawBoard() {
		//graphics.tools.create(new HnefataflFrame("Hnefatafl", 700, 600, game.getBoard()));
		return true;
	}

	@Override
	public ChessVariant recreate(GameMode mode) {
		return new Hnefatafl(mode);
	}


}