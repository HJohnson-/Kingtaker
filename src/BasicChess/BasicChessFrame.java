package BasicChess;

import graphics.ChessFrame;
import graphics.ChessPanel;

/**
 * Created by rp1012 on 16/10/14.
 */
public class BasicChessFrame extends ChessFrame {

    public BasicChessFrame(String title, int width, int height, BasicBoard board) {
        super(title, width, height, new BasicChessPanel(board));
    }

}
