package variants.RollerBallChess;

/**
 * Created by daniel on 14/12/27.
 */
public class MiddleSquareRules {
    public static boolean isInMiddle(int x,int y){
        if(x>=2&&x<=4&&y>=2&&y<=4)
        return true;
        else
            return false;
    }
}
