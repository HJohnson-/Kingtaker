package graphics;

import main.Location;

/**
 * Handles the animation and display of chess pieces, rather than having this in the ChessPiece class.
 */
public class GraphicsControl implements Runnable {

    protected Location curCords, endCords;
    protected boolean animating = false;
    protected int totalSteps = 25;
    protected int animationTime = 50;
    public ChessPanel panel;

    /**
     * Converts the given locations from board co-ordinates to graphics co-ordinates.
     * @param cur The current position of the piece.
     * @param end The position the piece will be in at the end of the animation. NB this will equal current piece
     *            when a piece is first created or when a piece is not moving.
     */
    public GraphicsControl(Location cur, Location end) {
        curCords = new Location(cur.getX() * ChessPanel.cellWidth, cur.getY() * ChessPanel.cellHeight);
        endCords = new Location(end.getX() * ChessPanel.cellWidth, end.getY() * ChessPanel.cellHeight);
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
        endCords = new Location(l.getX() * ChessPanel.cellWidth, l.getY() * ChessPanel.cellHeight);
    }

    public void givePanel(ChessPanel panel) {
        this.panel = panel;
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
        panel.animating = true;

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

        panel.animating = false;
        animating = false;
    }

    @Override
    public String toString() {
        return curCords + " -> " + endCords;
    }

}
