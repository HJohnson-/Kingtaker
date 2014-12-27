package variants.RollerBallChess;

import main.Location;

/**
 * Created by daniel on 14/12/27.
 */
public class RollBallRulesHelper {
    public static boolean isInMiddle(Location to){
        int x = to.getX();
        int y = to.getY();
        if(x>=2&&x<=4&&y>=2&&y<=4)
        return true;
        else
            return false;
    }
    public static boolean isInMiddle(int x, int y){
        if(x>=2&&x<=4&&y>=2&&y<=4)
            return true;
        else
            return false;
    }
}
