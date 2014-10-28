package graphics;

import main.Location;

import javax.swing.*;

/**
 * Handles the animation and display of chess pieces, rather than having this in the ChessPiece class.
 */
public class GraphicsControl implements Runnable {

    protected Location curCords, endCords;
    protected boolean animating = false;
    protected JPanel panel;
    protected int totalSteps = 25;
    protected int animationTime = 50;

    /**
     * Converts the given locations from board co-ordinates to graphics co-ordinates.
     * @param cur The current position of the piece.
     * @param end The position the piece will be in at the end of the animation. NB this will equal current piece
     *            when a piece is first created or when a piece is not moving.
     */
    public GraphicsControl(Location cur, Location end) {
        this.curCords = new Location(cur.getX() * tools.CELL_WIDTH, cur.getY() * tools.CELL_HEIGHT);
        this.endCords = new Location(end.getX() * tools.CELL_WIDTH, end.getY() * tools.CELL_HEIGHT);
    }

    public int getX() {
        return curCords.getX();
    }

    public int getY() {
        return curCords.getY();
    }

    /**
     * Sets the panel field, allowing the run function to call repaint to show the changes it makes.
     * @param panel The panel to be redrawn after each animation step.
     */
    public void givePanel(JPanel panel) {
        this.panel = panel;
    }

    /**
     * Converts the given co-ordinates from chess square (i.e. 0-7 for basic chess) to a graphics position.
     * @param l The location which the piece will end up on, when the animation concludes.
     */
    public void setGoal(Location l) {
        endCords = new Location(l.getX() * tools.CELL_WIDTH, l.getY() * tools.CELL_HEIGHT);
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
