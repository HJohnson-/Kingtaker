package variants.BasicChess;

import graphics.GraphicsControl;
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
        int distance = Math.abs(endCords.getX() - curCords.getX()) + Math.abs(endCords.getY() - curCords.getY());
        int sleepTime = (int) Math.ceil(animationTime / distance);

        int animationXStep = (int) Math.signum(endCords.getX() - curCords.getX());
        int animationYStep = (int) Math.signum(endCords.getY() - curCords.getY());

        while (!curCords.getX().equals(endCords.getX())) {
            curCords.incrX(animationXStep);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (!curCords.getY().equals(endCords.getY())) {
            curCords.incrY(animationYStep);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
