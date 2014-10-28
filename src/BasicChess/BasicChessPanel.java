package BasicChess;

import graphics.ChessPanel;
import main.Board;
import java.awt.*;

/**
 * Created by rp1012 on 16/10/14.
 */
public class BasicChessPanel extends ChessPanel {

    private final int GRID_WIDTH = 8;
    private final int GRID_HEIGHT = 8;

    /**
     * For basic chess, the default constructor will be fine.
     * @param board The basic chess board, used by the ChessPanel class.
     */
    public BasicChessPanel(Board board) {
        super();
        this.board = board;
    }

    /**
     * This function could potentially be abstracted, it draws a grid of the specified size,
     * then the pieces, then the UI.
     * @param g2 This is the graphics object which is being drawn to.
     */
    @Override
    protected void doDrawing(Graphics2D g2) {
        graphics.tools.drawQuadGrid(g2, GRID_WIDTH, GRID_HEIGHT);
        drawPieces(g2);
        drawUI(g2);
    }

}
