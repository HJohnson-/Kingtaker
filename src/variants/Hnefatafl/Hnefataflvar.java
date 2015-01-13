package variants.Hnefatafl;

import graphics.GraphicsTools;
import main.ChessVariant;
import main.GameController;
import main.GameMode;
import pieces.PieceDecoder;

import java.util.Random;

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
	public String getDescription() {
		return "Hnefatafl (or King’s Table, pronounced nef-ah-tah-fel) is a Viking strategic board game.\n\n" +
			   "The defending side (white pieces) must get the king to one of the corner squares before he is surrounded by attackers. \n\n" +
				"Attackers (black pieces) must kill the king for victory by surrounding him on four sides, either with attackers or a combination of boundaries and attackers. \n\n" +
				"All pieces except the king are captured if they are sandwiched between two enemy pieces\n\n" +
			   "For more, visit http://aagenielsen.dk/fetlar_rules_en.html"; }

	@Override
	public PieceDecoder getDecoder() {
		return new HnefataflDecoder();
	}

	public Hnefataflvar() {
		this(GameMode.MULTIPLAYER_LOCAL);
	}

	public Hnefataflvar(GameMode mode) {
		HnefataflBoard board = new HnefataflBoard();
		board.initializeBoard();
		game = new HnefataflController(board, getVariationID(), new HnefataflDecoder(), mode, new Random().nextBoolean());
		game.getBoard().setController(game);
	}

	public Hnefataflvar(GameController game) {
		this.game = game;
		this.game.getBoard().setController(game);
	}

	@Override
	public boolean drawBoard() {
		GraphicsTools.create(new HnefataflFrame(getName(), 500, 700, game.getBoard()));
		return true;
	}

	@Override
	public ChessVariant recreate(GameMode mode) {
		return new Hnefataflvar(mode);
	}


}