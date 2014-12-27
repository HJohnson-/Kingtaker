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
    public  boolean isInMiddle(int x, int y){
        if(x>=2&&x<=4&&y>=2&&y<=4)
            return true;
        else
            return false;
    }
    public  boolean isOuterConner(Location to){
        int x = to.getX();
        int y = to.getY();
        return((x==0&&y==0) || (x==6&&y==0) || (x==0&&y==6)||(x==6&&y==6) );
    }

    public  boolean isInnerConner(Location to){
        int x = to.getX();
        int y = to.getY();
        return((x==1&&y==1) || (x==5&&y==1) || (x==1&&y==5)||(x==5&&y==5) );
    }

//    public  String whichBorder(Location to){
//        int x = to.getX();
//        int y = to.getY();
//        if(y==0 || y==1 ){
//            return "N";
//        }else if(x==5 || x==6){
//            return "E";
//        }else if(y==5||y==6){
//            return "S";
//        }else {
//            return "W";
//        }
//    }

    public boolean inOuterRing(Location to){
        int x = to.getX();
        int y = to.getY();
        return (x==0 || y==0 || x==6 || y==6);
    }



// R = right, L = Left, U = Up, D = Down
    public String getClockWiseDir(Location to){
        int x = to.getX();
        int y = to.getY();

        // outer conner
        if((x==0&&y==0))
            return "R";

        if((x==6&&y==0))
            return "D";

        if((x==0&&y==6))
            return "U";

        if((x==6&&y==6))
            return "L";

        // inner conner
        if((x==1&&y==1))
            return "R";

        if((x==5&&y==1))
            return "D";

        if((x==1&&y==5))
            return "U";

        if((x==5&&y==5))
            return "L";

        //outer ring
            if(x==0) return "U";
            if(x==6) return "D";
            if(y==0) return "R";
            if(y==6) return "L";
        //inner ring
            if(x==1) return "U";
            if(x==5) return "D";
            if(y==1) return "R";
            if(y==5) return "L";

        return "error";
    }
}
