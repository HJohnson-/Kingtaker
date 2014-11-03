package BasicChess;

import graphics.ChessPanel;
import graphics.GraphicsControl;
import main.Board;
import main.Location;

/**
 * Custom animation for knights, so they look better while moving.
 */
public class KnightGraphicsControl extends GraphicsControl {

    public KnightGraphicsControl(Location cur, Location end) {
        super(cur, end);
    }

    /**
     * Custom animation for knights, this makes them complete their x-direction move before moving in the y-direction.
     * This gives a more natural look for knight animations, rather than some strange diagonal move.
     */
    @Override
    public void run() {
        int animationXStep = (endCords.getX() - curCords.getX()) / totalSteps;
        int animationYStep = (endCords.getY() - curCords.getY()) / totalSteps;
        animating = true;

        while (!curCords.getX().equals(endCords.getX())) {
            curCords.incrX(animationXStep);
            panel.repaint();
            try {
                Thread.sleep(animationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (!curCords.getY().equals(endCords.getY())) {
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
