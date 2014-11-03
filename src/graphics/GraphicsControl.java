package graphics;

import main.Board;
import main.Location;

import javax.swing.*;

/**
 * Handles the animation and display of chess pieces, rather than having this in the ChessPiece class.
 */
public class GraphicsControl implements Runnable {

    protected Location curCords, endCords;
    protected boolean animating = false;
    protected ChessPanel panel;
    protected int totalSteps = 25;
    protected int animationTime = 50;

    /**
     * Converts the given locations from board co-ordinates to graphics co-ordinates.
     * @param cur The current position of the piece.
     * @param end The position the piece will be in at the end of the animation. NB this will equal current piece
     *            when a piece is first created or when a piece is not moving.
     */
    public GraphicsControl(Location cur, Location end, ChessPanel panel) {
        this.panel = panel;
        panel.recalculateCellSize();
        this.curCords = new Location(cur.getX() * panel.cellWidth, cur.getY() * panel.cellWidth);
        this.endCords = new Location(end.getX() * panel.cellHeight, end.getY() * panel.cellHeight);
    }

    public int getX() {
        return curCords.getX();
    }

    public int getY() {
        return curCords.getY();
    }

    /**
     * Converts the given co-ordinates from chess square (i.e. 0-7 for basic chess) to a graphics position.
     * @param l The location which the piece will end up on, when the animation concludes.
     */
    public void setGoal(Location l) {
        endCords = new Location(l.getX() * panel.cellWidth, l.getY() * panel.cellHeight);
    }

    /**
     * A default animator for pieces, which makes them move in a straight line (possibly diagonal) from their start
     * position to their end position. It will take totalSteps * animationTime milliseconds to complete the animation.
     * This results in longer moves having faster pieces than short moves.
     */
    @Override
    public void run() {
        int animationXStep = (endCords.getX() - curCords.getX()) / totalSteps;
        int animationYStep = (endCords.getY() - curCords.getY()) / totalSteps;
        animating = true;

        while (!curCords.equals(endCords)) {

            curCords.incrX(animationXStep);
            curCords.incrY(animationYStep);
            panel.repaint();

            try {
                Thread.sleep(animationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        animating = false;
    }

}
