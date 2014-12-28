package variants.RollerBallChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import variants.BasicChess.Rook;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by daniel on 14/12/26.
 */
public class RBRook extends ChessPiece {

    private RollBallRulesHelper h = new RollBallRulesHelper();

    public RBRook(Board board, PieceType type, Location cords) {
        super(board, type, cords, "Rook");
    }

    @Override
    protected boolean validInState(Location to) {
//      in outer ring and not in the same horizontal or virtical direction
        if(h.inOuterRing(cords) && ((cords.getX()-to.getX())!=0 && (cords.getY()-to.getY())!=0)){
            Location conner = h.findOuterConnerByLoc(cords);
            return  board.clearLine(cords,conner) && board.clearLine(conner,to) && !h.isInMiddle(to);
        }
        return board.clearLine(cords, to) && !h.isInMiddle(to);
    }

    @Override
    public List<Location> allPieceMoves() {
        Rook r = new Rook(board, type, cords);
        List<Location> moves = new LinkedList<Location>();
        String dir = h.getClockWiseDir(cords);
        
        for(Location move:r.allPieceMoves()){
            if(!h.isInMiddle(move)) {
                if(dir.equals("U")&&(move.getY()<cords.getY()) ||
                        dir.equals("D")&&(move.getY()>cords.getY())||
                        dir.equals("L")&&(move.getX()<cords.getX())||
                        dir.equals("R")&&(move.getX()>cords.getX())){
                    moves.add(move);
                }
            }
        }


        // bounce in conner
        if(h.inOuterRing(cords)){
            List<Location> pair = h.containOuterCorner(moves);
            Location firstOuterConner = pair.get(0);
            r = new Rook(board, type, firstOuterConner);
             dir = h.getClockWiseDir(firstOuterConner);

            if(moves.contains(new Location(6,0))){
            }

            for(Location move:r.allPieceMoves()){
                if(!h.isInMiddle(move)) {
                    if(dir.equals("U")&&(move.getY()<cords.getY()) ||
                            dir.equals("D")&&(move.getY()>cords.getY())||
                            dir.equals("L")&&(move.getX()<cords.getX())||
                            dir.equals("R")&&(move.getX()>cords.getX())){
                        moves.add(move);
                    }
                }
            }
        }

        // move in special case in inner loop
        if(cords.getX() == 1 && cords.getY()==6 ||cords.getX() == 0 && cords.getY()==1 ||cords.getX() == 5 && cords.getY()==0 ||cords.getX() == 6 && cords.getY()==5 ){
            Location conner = h.findOuterConnerByLoc(cords);
            dir = h.getClockWiseDir(conner);
            r = new Rook(board, type, cords);
            for(Location move:r.allPieceMoves()){
                if(!h.isInMiddle(move)) {
                    if(dir.equals("U")&&(move.getY()<cords.getY()) ||
                            dir.equals("D")&&(move.getY()>cords.getY())||
                            dir.equals("L")&&(move.getX()<cords.getX())||
                            dir.equals("R")&&(move.getX()>cords.getX())){
                        moves.add(move);
                    }
                }
            }
        }

        // add location around rook
        Location newLoc = new Location(cords.getX()+1,cords.getY());
        if(h.isValidedLoc(newLoc)&& !moves.contains(newLoc)){
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX(),cords.getY()+1);
        if(h.isValidedLoc(newLoc)&& !moves.contains(newLoc)){
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX()-1,cords.getY());
        if(h.isValidedLoc(newLoc)&& !moves.contains(newLoc)){
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX(),cords.getY()-1);
        if(h.isValidedLoc(newLoc)&& !moves.contains(newLoc)){
            moves.add(newLoc);
        }

        return moves;
    }

    @Override
    public int returnValue() {
        return 5;
    }

    @Override
    public String getName() {
        return "Rook";
    }

    @Override
    public boolean executeMove(Location targetLocation) {


        if(h.inOuterRing(cords) && (cords.getX()-targetLocation.getX())!=0 && (cords.getY()-targetLocation.getY())!=0){


            Location conner = h.findOuterConnerByLoc(cords);

            board.clearSpace(cords);
            board.placePiece(targetLocation, this);

            if (board.doDrawing) graphics.setGoal(conner);
            if (board.doDrawing) graphics.setGoal(targetLocation);
            return true;
        }
        board.clearSpace(cords);
        board.placePiece(targetLocation, this);
        if (board.doDrawing) graphics.setGoal(targetLocation);
        this.lastTurnMovedOn = board.getController().getCurrentTurn();
        return true;
    }
}
