package variants.RollerBallChess;

import graphics.ChessPanel;
import graphics.GraphicsTools;
import main.Board;

import java.awt.*;

/**
 * A specific panel to draw in a basic chess board.
 */
public class RBPanel extends ChessPanel {

    /**
     * For basic chess, the default constructor will be fine.
     */
    public RBPanel(Board board) {
        super(board);
    }

    @Override
    protected void drawGrid(Graphics2D g2) {

        int currX, currY;
        g2.setColor(GraphicsTools.BOARD_BLACK);
        for (int x = offset.getX(); x < offset.getX() + board.numRows() * cellWidth; x += cellWidth * 2) {
            for (int y = offset.getY(); y < offset.getY() + board.numCols() * cellHeight; y += cellHeight * 2) {
                currX = (x - offset.getX()) / cellHeight;
                currY = (y - offset.getY()) / cellWidth;
                g2.fillRect(x, y, cellWidth, cellHeight);
                if ((currX != board.numRows() - 1) && currY != board.numCols() - 1)
                    g2.fillRect(x + cellWidth, y + cellHeight, cellWidth, cellHeight);
            }
        }

        g2.setColor(GraphicsTools.BOARD_WHITE);
        for (int x = offset.getX(); x < offset.getX() + board.numRows() * cellWidth; x += cellWidth * 2) {
            for (int y = offset.getY(); y < offset.getY() + board.numCols() * cellHeight; y += cellWidth * 2) {
                currX = (x - offset.getX()) / cellHeight;
                currY = (y - offset.getY()) / cellWidth;
                if (currX != board.numRows() - 1)
                    g2.fillRect(x + cellWidth, y, cellWidth, cellHeight);
                if (currY != board.numCols() - 1)
                    g2.fillRect(x, y + cellHeight, cellWidth, cellHeight);
            }
        }

        g2.setColor(new Color(85, 55, 29));
        for (int x = offset.getX() + 2 * cellWidth; x < offset.getX() + 5 * cellWidth; x += cellWidth) {
            for (int y = offset.getY() + 2 * cellWidth; y < offset.getY() + 5 * cellHeight; y += cellWidth) {
                g2.fillRect(x, y, cellWidth, cellHeight);
            }
        }
    }
}
