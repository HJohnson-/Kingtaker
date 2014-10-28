package BasicChess;

import graphics.GraphicsControl;
import main.Location;

/**
 * Created by rp1012 on 28/10/14.
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
                System.err.println(e);
            }
        }

        while (!curCords.getY().equals(endCords.getY())) {
            curCords.incrY(animationYStep);
            panel.repaint();
            try {
                Thread.sleep(animationTime);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

        animating = false;
    }

}
