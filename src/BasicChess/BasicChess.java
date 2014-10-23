package BasicChess;

import main.*;
import pieces.ChessPiece;

/**
 * Created by crix9 on 15/10/2014.
 */
public class BasicChess extends ChessVariant {

	public BasicChess(){
		game = new GameController(new BasicBoard());
		game.getBoard().setController(game);
	}

	//returns true if there was no errors
	public boolean drawBoard() {
        graphics.tools.create(new BasicChessFrame("Basic Chess", 600, 600, (BasicBoard) game.getBoard()));
        return true;
	}
}
