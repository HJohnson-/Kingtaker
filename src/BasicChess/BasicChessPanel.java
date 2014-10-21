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

    public BasicChessPanel(Board board) {
        super();
        this.board = board;
    }

    @Override
    protected void doDrawing(Graphics2D g2) {
        graphics.tools.drawQuadGrid(g2, GRID_WIDTH, GRID_HEIGHT);
        drawPieces(g2);
        drawUI(g2);
    }

}
