package variants.RollerBallChess;

import graphics.ChessPanel;
import graphics.GraphicsTools;
import main.Board;
import main.Location;

import java.awt.*;


public class RBPanel extends ChessPanel {
    private final int CORNER_SCALE = 4;

    public RBPanel(Board board) {
        super(board);
    }

    @Override
    protected void drawGrid(Graphics2D g2) {
        //Draw normal grid
        super.drawGrid(g2);

        int ofX = offset.getX();
        int ofY = offset.getY();
        int lastX = ofX + cellWidth * board.numCols();
        int lastY = ofX + cellWidth * board.numCols();

        //Draw centre circle
        g2.setPaint(GraphicsTools.BOARD_BLACK.brighter());
        g2.fillRect(ofX + 2 * cellWidth, ofY + 2 * cellWidth, cellWidth * 3, cellHeight * 3);
        g2.setPaint(BG_PAINT);
        g2.fillOval(ofX + 2 * cellWidth, ofY + 2 * cellWidth, cellWidth * 3, cellHeight * 3);

        //Draw board corners
        int[] xPoints; int[] yPoints;
        xPoints = new int[]{ofX, ofX, ofX + cellWidth / CORNER_SCALE};
        yPoints = new int[]{ofY, ofY + cellHeight / CORNER_SCALE, ofY};
        g2.fillPolygon(xPoints, yPoints, 3);

        xPoints = new int[]{lastX, lastX, lastX - cellWidth / CORNER_SCALE};
        yPoints = new int[]{ofY, ofY + cellHeight / CORNER_SCALE, ofY};
        g2.fillPolygon(xPoints, yPoints, 3);

        xPoints = new int[]{ofX, ofX, ofX + cellWidth / CORNER_SCALE};
        yPoints = new int[]{lastY, lastY - cellHeight / CORNER_SCALE, lastY};
        g2.fillPolygon(xPoints, yPoints, 3);

        xPoints = new int[]{lastX, lastX, lastX - cellWidth / CORNER_SCALE};
        yPoints = new int[]{lastY, lastY - cellHeight / CORNER_SCALE, lastY};
        g2.fillPolygon(xPoints, yPoints, 3);

        //Draw arrow symbol in the middle to indicate direction.
        g2.setPaint(Color.BLACK);
        g2.setFont(new Font("", Font.PLAIN, cellWidth * 2));
        drawCentreString("↻", new Location(ofX + 2 * cellWidth, ofY + 2 * cellWidth),
                cellWidth * 3, cellHeight * 3, g2);
    }

    @Override
    protected void drawBorder(Graphics2D g2) {
        int halfWidth = BOARD_BORDER_WIDTH / 2;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(BOARD_BORDER_WIDTH));
        g2.drawRoundRect(offset.getX() - halfWidth,
                offset.getY() - halfWidth,
                board.numCols() * cellWidth + halfWidth * 2,
                board.numRows() * cellHeight + halfWidth * 2,
                cellWidth, cellHeight);
    }
}
