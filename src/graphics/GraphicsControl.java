package graphics;

import main.Location;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles the animation and display of chess pieces, rather than having this in the ChessPiece class.
 */
public class GraphicsControl implements Runnable {

    protected Location curCords, endCords;
    protected int animationTime = 1000;
    public ChessPanel panel;

    /**
     * Converts the given locations from board co-ordinates to graphics co-ordinates.
     * @param cur The current position of the piece.
     * @param end The position the piece will be in at the end of the animation. NB this will equal current piece
     *            when a piece is first created or when a piece is not moving.
     */
    public GraphicsControl(Location cur, Location end) {
        curCords = cur;
        endCords = end;
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
        endCords = new Location(l.getX() * panel.cellWidth + panel.offset.getX(),
                                l.getY() * panel.cellHeight + panel.offset.getY());
        ExecutorService pool = Executors.newFixedThreadPool(1);
        pool.submit(this);
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

        float maxDistance = Math.max(Math.abs(endCords.getX() - curCords.getX()), Math.abs(endCords.getY() - curCords.getY()));
        int sleepTime = (int) Math.ceil(animationTime / maxDistance);

        int animationXStep = (int) Math.signum(endCords.getX() - curCords.getX());
        int animationYStep = (int) Math.signum(endCords.getY() - curCords.getY());

        panel.board.getController().animating = true;

        while (!curCords.equals(endCords)) {
            curCords.incrX(animationXStep);
            curCords.incrY(animationYStep);

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        panel.board.getController().animating = false;
    }

    @Override
    public String toString() {
        return curCords + " -> " + endCords;
    }

}
