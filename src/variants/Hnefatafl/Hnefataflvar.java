package variants.Hnefatafl;

import main.ChessVariant;
import main.GameController;
import main.GameMode;
import pieces.PieceDecoder;
import variants.BasicChess.BasicChessFrame;

/**
 * Created by crix9 on 21/11/2014.
 */
public class Hnefataflvar extends ChessVariant {
	@Override
	public int getVariationID() {
		return 3;
	}

	@Override
	public String getName() {
		return "Hnefatafl";
	}

	@Override
	public String getDescription() { return "[variants.Hnefataflvar rules]\n"; }

	@Override
	public PieceDecoder getDecoder() {
		return new HnefataflDecoder();
	}

	public Hnefataflvar() {
		this(GameMode.MULTIPLAYER_LOCAL);
	}

	public Hnefataflvar(GameMode mode) {
		game = new GameController(new HnefataflBoard(), 3, new HnefataflDecoder(), mode, GameController.defaultPIW);
		game.getBoard().setController(game);
		game.getBoard().initializeBoard();
	}

	public Hnefataflvar(GameController game) {
		this.game = game;
		this.game.getBoard().setController(game);
	}

	@Override
	public boolean drawBoard() {
		graphics.tools.create(new HnefataflFrame("Hnefatafl", 700, 600, game.getBoard()));
		return true;
	}

	@Override
	public ChessVariant recreate(GameMode mode) {
		return new Hnefataflvar(mode);
	}


}