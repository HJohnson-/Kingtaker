package BasicChess;

import graphics.ChessFrame;
import graphics.ChessPanel;

/**
 * Created by rp1012 on 16/10/14.
 */
public class BasicChessFrame extends ChessFrame {

    /**
     * As a basic chess board doesn't have any special requirements, we just call the default constructor with the
     * given parameters.
     */
    public BasicChessFrame(String title, int width, int height, BasicBoard board) {
        super(title, width, height, new BasicChessPanel(board));
    }

}
