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
        super(board, type, cords, "bishop");
    }

    @Override
    protected boolean validInState(Location to) {
        boolean result = true;
        // four special position
        if(cords.getX()==1 && cords.getY()==2){
            if(to.getX()==1&&to.getY()==0){
                result = result&& board.clearLine(new Location(0, 1), new Location(1,0));
            }else{
                if(to.getX()>3){
                    result = result&&board.clearLine(cords,new Location(3,0));
                    result = result&&board.clearLine(new Location(3,0),to);
                }else{
                    result = result&&board.clearLine(cords,to);
                }
            }
            return result;
        }

        if(cords.getX()==4 && cords.getY()==1){
            if(to.getX()==6&&to.getY()==1){
                result = result&& board.clearLine(new Location(5, 0), new Location(6,1));
            }else{
                if(to.getY()>3){
                    result = result&&board.clearLine(cords,new Location(6,3));
                    result = result&&board.clearLine(new Location(6,3),to);
                }else{
                    result = result&&board.clearLine(cords,to);
                }
            }
            return result;
        }

        if(cords.getX()==5 && cords.getY()==4){
            if(to.getX()==5&&to.getY()==6){
                result = result&& board.clearLine(new Location(6,5), new Location(5,6));
            }else{
                if(to.getX()<3){
                    result = result&&board.clearLine(cords,new Location(3,6));
                    result = result&&board.clearLine(new Location(3,6),to);
                }else{
                    result = result&&board.clearLine(cords,to);
                }
            }
            return result;
        }

        if(cords.getX()==2 && cords.getY()==5){
            if(to.getX()==0&&to.getY()==5){
                result = result&& board.clearLine(new Location(1,6), new Location(0,5));
            }else{
                if(to.getY()<3){
                    result = result&&board.clearLine(cords,new Location(0,3));
                    result = result&&board.clearLine(new Location(0,3),to);
                }else{
                    result = result&&board.clearLine(cords,to);
                }
            }
            return result;
        }

        // rest

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

        Location bouncingLoc = h.findBouncingLoc(dir,moves,cords).get(0);
         dir = h.getClockWiseDir(bouncingLoc);

        if (dir.equals("U")) {
            if(to.getY()<bouncingLoc.getY()){
                result = result && board.clearLine(cords,bouncingLoc);
                result = result && board.clearLine(bouncingLoc,to);
            }else{
                result = result && board.clearLine(cords,to);
            }
        }

        if (dir.equals("D")) {
            if(to.getY()>bouncingLoc.getY()){
                result = result && board.clearLine(cords,bouncingLoc);
                result = result && board.clearLine(bouncingLoc,to);
            }else{
                result = result && board.clearLine(cords,to);
            }
        }

        if (dir.equals("L")) {
            if(to.getX()<bouncingLoc.getX()){
                result = result && board.clearLine(cords,bouncingLoc);
                result = result && board.clearLine(bouncingLoc,to);
            }else{
                result = result && board.clearLine(cords,to);
            }
        }

        if (dir.equals("R")) {
            if(to.getX()>bouncingLoc.getX()){
                result = result && board.clearLine(cords,bouncingLoc);
                result = result && board.clearLine(bouncingLoc,to);
            }else{
                result = result && board.clearLine(cords,to);
            }
        }

    return result;
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
                    if(!moves.contains(move))
                    moves.add(move);
                }
            }
        }

        bouncingLocs = h.findBouncingLoc(dir,moves,cords);
//        System.out.println(bouncingLocs.size());

        // bounce on edge
        for(Location boundcingLoc:bouncingLocs){
            dir = h.getClockWiseDir(boundcingLoc);
            b = new Bishop(board, type, boundcingLoc);
            for(Location move: b.allPieceMoves())
                if(!h.isInMiddle(move)) {
                    if(dir.equals("U")&&(move.getY()<boundcingLoc.getY()) ||
                            dir.equals("D")&&(move.getY()>boundcingLoc.getY())||
                            dir.equals("L")&&(move.getX()<boundcingLoc.getX())||
                            dir.equals("R")&&(move.getX()>boundcingLoc.getX())){
                        if(!moves.contains(move))
                        moves.add(move);
                    }
                }
        }

        // add location around rook
        Location newLoc = new Location(cords.getX()+1,cords.getY()+1);
        if(h.isValidedLoc(newLoc)&& !moves.contains(newLoc)){
            if(!moves.contains(newLoc))
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX()+1,cords.getY()-1);
        if(h.isValidedLoc(newLoc)&& !moves.contains(newLoc)){
            if(!moves.contains(newLoc))
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX()-1,cords.getY()-1);
        if(h.isValidedLoc(newLoc)&& !moves.contains(newLoc)){
            if(!moves.contains(newLoc))
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX()-1,cords.getY()+1);
        if(h.isValidedLoc(newLoc)&& !moves.contains(newLoc)){
            if(!moves.contains(newLoc))
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
        return "RBBishop";
    }


    @Override
    public boolean executeMove(Location targetLocation) {

//        if(h.inOuterRing(cords) && (cords.getX()-targetLocation.getX())!=0 && (cords.getY()-targetLocation.getY())!=0){
//
//
//            Location conner = h.findOuterConnerByLoc(cords);
//
//            board.clearSpace(cords);
//            board.placePiece(targetLocation, this);
//
//            if (board.doDrawing) graphics.setGoal(conner);
//            if (board.doDrawing) graphics.setGoal(targetLocation);
//            return true;
//        }
//        board.clearSpace(cords);
//        board.placePiece(targetLocation, this);
//        if (board.doDrawing) graphics.setGoal(targetLocation);
//        this.lastTurnMovedOn = board.getController().getCurrentTurn();
//



        List<Location> bouncingLocs;

        Bishop b = new Bishop(board, type, cords);
        List<Location> moves = new LinkedList<Location>();
        String cordsdir = h.getClockWiseDir(cords);

        // basic clockwise move
        for(Location move:b.allPieceMoves()){
            if(!h.isInMiddle(move)) {
                if(cordsdir.equals("U")&&(move.getY()<cords.getY()) ||
                        cordsdir.equals("D")&&(move.getY()>cords.getY())||
                        cordsdir.equals("L")&&(move.getX()<cords.getX())||
                        cordsdir.equals("R")&&(move.getX()>cords.getX())){
                    if(!moves.contains(move))
                        moves.add(move);
                }
            }
        }

        bouncingLocs = h.findBouncingLoc(cordsdir,moves,cords);


        // for the location that only have two possible bouncing location
        // bouncingLocs.get(1) is the closer bouncing location, bouncingLocs.get(0) is the farther bouncing location

        if(bouncingLocs.size()==2) {
            // four special position
            if (cords.getX() == 1 && cords.getY() == 2) {
                if (targetLocation.getX() == 1 && targetLocation.getY() == 0) {
                    if (board.doDrawing) graphics.setGoal(bouncingLocs.get(1));
                    if (board.doDrawing) graphics.setGoal(targetLocation);
                } else {
                    if (targetLocation.getX() > 3) {
                        if (board.doDrawing) graphics.setGoal(bouncingLocs.get(0));
                        if (board.doDrawing) graphics.setGoal(targetLocation);
                    }else{
                        if (board.doDrawing) graphics.setGoal(targetLocation);
                    }
                }
            }

            if (cords.getX() == 4 && cords.getY() == 1) {
                if (targetLocation.getX() == 6 && targetLocation.getY() == 1) {
                    if (board.doDrawing) graphics.setGoal(bouncingLocs.get(1));
                    if (board.doDrawing) graphics.setGoal(targetLocation);
                } else {
                    if (targetLocation.getY() > 3) {
                        if (board.doDrawing) graphics.setGoal(bouncingLocs.get(0));
                        if (board.doDrawing) graphics.setGoal(targetLocation);
                    }else{
                        if (board.doDrawing) graphics.setGoal(targetLocation);
                    }
                }
            }

            if (cords.getX() == 5 && cords.getY() == 4) {
                if (targetLocation.getX() == 5 && targetLocation.getY() == 6) {
                    if (board.doDrawing) graphics.setGoal(bouncingLocs.get(1));
                    if (board.doDrawing) graphics.setGoal(targetLocation);
                } else {
                    if (targetLocation.getX() < 3) {
                        if (board.doDrawing) graphics.setGoal(bouncingLocs.get(0));
                        if (board.doDrawing) graphics.setGoal(targetLocation);
                    }else{
                        if (board.doDrawing) graphics.setGoal(targetLocation);
                    }
                }
            }

            if (cords.getX() == 2 && cords.getY() == 5) {
                if (targetLocation.getX() == 0 && targetLocation.getY() == 5) {
                    if (board.doDrawing) graphics.setGoal(bouncingLocs.get(1));
                    if (board.doDrawing) graphics.setGoal(targetLocation);
                } else {
                    if (targetLocation.getY() < 3) {
                        if (board.doDrawing) graphics.setGoal(bouncingLocs.get(0));
                        if (board.doDrawing) graphics.setGoal(targetLocation);
                    }else{
                        if (board.doDrawing) graphics.setGoal(targetLocation);
                    }
                }
            }

            board.clearSpace(cords);
            board.placePiece(targetLocation, this);
            this.lastTurnMovedOn = board.getController().getCurrentTurn();
            return true;
        }

        // for the location that only have one possible bouncing location

        if(bouncingLocs.size()==1) {
            Location bouncingLoc = h.findBouncingLoc(cordsdir, moves, cords).get(0);
            String bouncedir = h.getClockWiseDir(bouncingLoc);

            if (bouncedir.equals("U")) {
                if (targetLocation.getY() < bouncingLoc.getY()) {
                    if (board.doDrawing) graphics.setGoal(bouncingLoc);
                    if (board.doDrawing) graphics.setGoal(targetLocation);
                } else {
                    if (board.doDrawing) graphics.setGoal(targetLocation);
                }
            }

            if (bouncedir.equals("D")) {
                if (targetLocation.getY() > bouncingLoc.getY()) {
                    if (board.doDrawing) graphics.setGoal(bouncingLoc);
                    if (board.doDrawing) graphics.setGoal(targetLocation);
                } else {
                    if (board.doDrawing) graphics.setGoal(targetLocation);
                }
            }

            if (bouncedir.equals("L")) {
                if (targetLocation.getX() < bouncingLoc.getX()) {
                    if (board.doDrawing) graphics.setGoal(bouncingLoc);
                    if (board.doDrawing) graphics.setGoal(targetLocation);
                } else {
                    if (board.doDrawing) graphics.setGoal(targetLocation);
                }
            }

            if (bouncedir.equals("R")) {
                if (targetLocation.getX() > bouncingLoc.getX()) {
                    if (board.doDrawing) graphics.setGoal(bouncingLoc);
                    if (board.doDrawing) graphics.setGoal(targetLocation);
                } else {
                    if (board.doDrawing) graphics.setGoal(targetLocation);
                }
            }
            board.clearSpace(cords);
            board.placePiece(targetLocation, this);
            this.lastTurnMovedOn = board.getController().getCurrentTurn();
            return true;
        }
        return false;
    }
}
