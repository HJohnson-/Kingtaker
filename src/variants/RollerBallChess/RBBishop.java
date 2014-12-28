package variants.RollerBallChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import variants.BasicChess.Bishop;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by daniel on 14/12/26.
 */
public class RBBishop extends ChessPiece {

    private RollBallRulesHelper h = new RollBallRulesHelper();
    
    public RBBishop(Board board, PieceType type, Location cords) {
        super(board, type, cords, "Bishop");
    }

    @Override
    protected boolean validInState(Location to) {
//        Location conner = h.findOuterConnerByLoc(cords);
//        return  board.clearLine(cords,conner) && board.clearLine(conner,to) && !h.isInMiddle(to);

//        List<Location> bouncingLocs;
//
//        Bishop b = new Bishop(board, type, cords);
//        List<Location> moves1 = new LinkedList<Location>();
//        List<Location> moves2 = new LinkedList<Location>();
//        String dir = h.getClockWiseDir(cords);

        // basic clockwise move
//        for(Location move:b.allPieceMoves()){
//            if(!h.isInMiddle(move)) {
//                if(dir.equals("U")&&(move.getY()<cords.getY())){
//                    if(move.getX())
//                }
//                        dir.equals("D")&&(move.getY()>cords.getY())||
//                        dir.equals("L")&&(move.getX()<cords.getX())||
//                        dir.equals("R")&&(move.getX()>cords.getX())){
//                    moves.add(move);
//                }
//            }
//        }
return false;
    }

    @Override
    public List<Location> allPieceMoves() {
        // a list contains the bouncing location
        List<Location> bouncingLocs;


        Bishop b = new Bishop(board, type, cords);
        List<Location> moves = new LinkedList<Location>();
        String dir = h.getClockWiseDir(cords);

        // basic clockwise move
        for(Location move:b.allPieceMoves()){
            if(!h.isInMiddle(move)) {
                if(dir.equals("U")&&(move.getY()<cords.getY()) ||
                        dir.equals("D")&&(move.getY()>cords.getY())||
                        dir.equals("L")&&(move.getX()<cords.getX())||
                        dir.equals("R")&&(move.getX()>cords.getX())){
                    moves.add(move);
                }
            }
        }

        bouncingLocs = h.findBouncingLoc(dir,moves);

        // bounce on edge
        for(Location move:bouncingLocs){
            if(!h.isInMiddle(move)) {
                if(dir.equals("U")&&(move.getY()<cords.getY()) ||
                        dir.equals("D")&&(move.getY()>cords.getY())||
                        dir.equals("L")&&(move.getX()<cords.getX())||
                        dir.equals("R")&&(move.getX()>cords.getX())){
                    moves.add(move);
                }
            }
        }

        // add location around rook
        Location newLoc = new Location(cords.getX()+1,cords.getY()+1);
        if(h.isValidedLoc(newLoc)&& !moves.contains(newLoc)){
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX()+1,cords.getY()-1);
        if(h.isValidedLoc(newLoc)&& !moves.contains(newLoc)){
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX()-1,cords.getY()-1);
        if(h.isValidedLoc(newLoc)&& !moves.contains(newLoc)){
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX()-1,cords.getY()+1);
        if(h.isValidedLoc(newLoc)&& !moves.contains(newLoc)){
            moves.add(newLoc);
        }

        return moves;
    }

    @Override
    public int returnValue() {
        return 3;
    }

    @Override
    public String getName() {
        return null;
    }
}
