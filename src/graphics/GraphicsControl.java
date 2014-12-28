package graphics;

import main.Location;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles the animation and display of chess pieces, rather than having this in the ChessPiece class.
 */
public class GraphicsControl implements Runnable {

    protected Location curCords;
    protected Queue<Location> endCords;
    protected int animationTime = 1000;
    public ChessPanel panel;
    ExecutorService pool = Executors.newFixedThreadPool(1);

    /**
     * Converts the given locations from board co-ordinates to graphics co-ordinates.
     * @param cur The current position of the piece.
     * @param end The position the piece will be in at the end of the animation. NB this will equal current piece
     *            when a piece is first created or when a piece is not moving.
     */
    public GraphicsControl(Location cur, Location end) {
        curCords = cur;
        endCords = new ConcurrentLinkedQueue<Location>();
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
        endCords.add(new Location(l.getX() * panel.cellWidth + panel.offset.getX(),
                                l.getY() * panel.cellHeight + panel.offset.getY()));
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

        panel.board.getController().animating = false;
        
        while (!endCords.isEmpty()) {
            
            Location end = endCords.poll();

            float maxDistance = Math.max(Math.abs(end.getX() - curCords.getX()), Math.abs(end.getY() - curCords.getY()));
            int sleepTime = (int) Math.ceil(animationTime / maxDistance);

            int animationXStep = (int) Math.signum(end.getX() - curCords.getX());
            int animationYStep = (int) Math.signum(end.getY() - curCords.getY());

            if ((animationXStep != 0) && (animationYStep != 0) &&
                    (Math.abs(end.getX() - curCords.getX()) != Math.abs(end.getY() - curCords.getY()))) {
                while (!curCords.getX().equals(end.getX())) {
                    curCords.incrX(animationXStep);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                while (!curCords.getY().equals(end.getY())) {
                    curCords.incrY(animationYStep);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                while (!curCords.equals(end)) {
                    curCords.incrX(animationXStep);
                    curCords.incrY(animationYStep);

                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

        panel.board.getController().animating = false;
    }

    @Override
    public String toString() {
        return curCords + " -> " + endCords;
    }

}
