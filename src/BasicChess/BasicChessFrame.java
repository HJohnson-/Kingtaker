package BasicChess;

import graphics.ChessFrame;
import main.Board;

/**
 * Implements the abstract class ChessFrame, specifically for basic chess.
 */
public class BasicChessFrame extends ChessFrame {

    /**
     * As a basic chess board doesn't have any special requirements, we just call the default constructor with the
     * given parameters.
     */
    public BasicChessFrame(String title, int width, int height, Board board) {
        super(title, width, height, new BasicChessPanel(board));
    }

}
