package graphics;

import main.Location;

import javax.swing.*;

/**
 * Created by Rob on 26/10/2014.
 */
public class GraphicsControl implements Runnable {

    protected Location curCords, endCords;
    protected boolean animating = false;
    protected JPanel panel;
    protected int totalSteps = 25;
    protected int animationTime = 50;

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

    public void givePanel(JPanel panel) {
        this.panel = panel;
    }

    public void setGoal(Location l) {
        endCords = new Location(l.getX() * tools.CELL_WIDTH, l.getY() * tools.CELL_HEIGHT);
    }

    public boolean isAnimating() {
        return animating;
    }

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
                System.err.println(e);
            }

        }

        animating = false;
    }

}
