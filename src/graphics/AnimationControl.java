package graphics;

import main.Location;

/**
 * Created by Rob on 26/10/2014.
 */
public class AnimationControl {

    public Location curCords, endCords;
    public boolean animating = false;

    //How many ticks to have.
    public int totalSteps = 25;

    //How long each tick is.
    public int animationTime = 50;

}
