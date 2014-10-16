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
        this.board = board;
    }

    protected void doDrawing(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        graphics.tools.drawGrid(g2, GRID_WIDTH, GRID_HEIGHT);

        g2.setPaint(new Color(255, 0, 0));
        drawPieces(g2);
    }

}
