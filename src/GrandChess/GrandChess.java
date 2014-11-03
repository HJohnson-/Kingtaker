package GrandChess;

import BasicChess.BasicChess;
import graphics.tools;
import main.ChessVariant;
import main.GameController;
import BasicChess.BasicDecoder;
import BasicChess.BasicChessFrame;

/**
 * Created by hj1012 on 03/11/14.
 */
public class GrandChess extends ChessVariant {

	public GrandChess(){
		String[] pieces = {"rook", "bishop", "knight", "queen", "king", "pawn", "cardinal", "marshal"};
		tools.loadPieces(pieces);
		game = new GameController(new GrandBoard(), "Grand", new BasicDecoder());
		game.getBoard().setController(game);
		game.getBoard().initializeBoard();
	}

	public GrandChess(GameController game) {
		String[] pieces = {"rook", "bishop", "knight", "queen", "king", "pawn", "cardinal", "marshal"};
		tools.loadPieces(pieces);
		this.game = game;
		this.game.getBoard().setController(game);
	}

	@Override
	public boolean drawBoard() {
		graphics.tools.create(new BasicChessFrame("Grand Chess", 700, 600, game.getBoard()));
		return true;
	}
}
