package variants.RollerBallChess;

import main.Location;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by daniel on 14/12/27.
 */
public class RollBallRulesHelper {
    public static boolean isInMiddle(Location to) {
        int x = to.getX();
        int y = to.getY();
        if (x >= 2 && x <= 4 && y >= 2 && y <= 4)
            return true;
        else
            return false;
    }

    public boolean isInMiddle(int x, int y) {
        if (x >= 2 && x <= 4 && y >= 2 && y <= 4)
            return true;
        else
            return false;
    }

    public boolean isOuterConner(Location to) {
        int x = to.getX();
        int y = to.getY();
        return ((x == 0 && y == 0) || (x == 6 && y == 0) || (x == 0 && y == 6) || (x == 6 && y == 6));
    }

    public boolean isInnerConner(Location to) {
        int x = to.getX();
        int y = to.getY();
        return ((x == 1 && y == 1) || (x == 5 && y == 1) || (x == 1 && y == 5) || (x == 5 && y == 5));
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

    public boolean inOuterRing(Location to) {
        int x = to.getX();
        int y = to.getY();
        return (x == 0 || y == 0 || x == 6 || y == 6);
    }


    // R = right, L = Left, U = Up, D = Down
    public String getClockWiseDir(Location currentPos) {
        int x = currentPos.getX();
        int y = currentPos.getY();

        // outer conner
        if ((x == 0 && y == 0))
            return "R";

        if ((x == 6 && y == 0))
            return "D";

        if ((x == 0 && y == 6))
            return "U";

        if ((x == 6 && y == 6))
            return "L";

        // inner conner
        if ((x == 1 && y == 1))
            return "R";

        if ((x == 5 && y == 1))
            return "D";

        if ((x == 1 && y == 5))
            return "U";

        if ((x == 5 && y == 5))
            return "L";

        //outer ring
        if (x == 0) return "U";
        if (x == 6) return "D";
        if (y == 0) return "R";
        if (y == 6) return "L";
        //inner ring
        if (x == 1) return "U";
        if (x == 5) return "D";
        if (y == 1) return "R";
        if (y == 5) return "L";

        return "ERROR";
    }

    public boolean isValidedLoc(Location currentPos) {
        int x = currentPos.getX();
        int y = currentPos.getY();

        return (x >= 0 && x <= 6 && y >= 0 && y <= 6 && !isInMiddle(currentPos));
    }

    public List<Location> containOuterCorner(List<Location> moves) {
        Location newLoc = new Location(0, 0);

        // contains the current outer conner and the next outer conner
        LinkedList<Location> returnPair = new LinkedList<Location>();

        if (moves.contains(newLoc)) {
            returnPair.add(newLoc);
            returnPair.add(new Location(6, 0));
        }

        newLoc = new Location(0, 6);
        if (moves.contains(newLoc)) {
            returnPair.add(newLoc);
            returnPair.add(new Location(0, 0));
        }

        newLoc = new Location(6, 0);
        if (moves.contains(newLoc)) {
            returnPair.add(newLoc);
            returnPair.add(new Location(6, 6));
        }

        newLoc = new Location(6, 6);
        if (moves.contains(newLoc)) {
            returnPair.add(newLoc);
            returnPair.add(new Location(0, 6));
        }

        return returnPair;
    }

    public Location findOuterConnerByLoc(Location currentLoc) {
        int x = currentLoc.getX();
        int y = currentLoc.getY();

        String dir = getClockWiseDir(currentLoc);

        if (dir.equals("U")) {
            return new Location(0, 0);
        }
        if (dir.equals("D")) {
            return new Location(6, 6);
        }
        if (dir.equals("L")) {
            return new Location(0, 6);
        }
//        if(dir.equals("U")){
        return new Location(6, 0);
    }

    public List<Location> findBouncingLoc(String dir, List<Location> moves) {
        List<Location> bouncingLocs = new LinkedList<Location>();
        // int mini or max
        int mm;
        if (dir.equals("U") || dir.equals("L")) {
            mm = 100;
        } else {
            mm = -100;
        }

        // find max or min of x or y
        for (Location move : moves) {
            if (dir.equals("U")) {
                if (move.getY() < mm) {
                    mm = move.getY();
                }
            }
            if (dir.equals("D")) {
                if (move.getY() > mm) {
                    mm = move.getY();
                }
            }
            if (dir.equals("L")) {
                if (move.getX() < mm) {
                    mm = move.getX();
                }
            }
            if (dir.equals("R")) {
                if (move.getX() > mm) {
                    mm = move.getX();
                }
            }
        }

        // build return list

        for (Location move : moves) {
            if (dir.equals("U")) {
                if (move.getY() == mm) {
                    bouncingLocs.add(move);
                }
            }
            if (dir.equals("D")) {
                if (move.getY() == mm) {
                    bouncingLocs.add(move);
                }
            }
            if (dir.equals("L")) {
                if (move.getX() == mm) {
                    bouncingLocs.add(move);
                }
            }
            if (dir.equals("R")) {
                if (move.getX() == mm) {
                    bouncingLocs.add(move);
                }
            }
        }

        return bouncingLocs;


    }
}