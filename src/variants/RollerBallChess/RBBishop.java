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

//        if(h.findBouncingLoc(dir,moves).isEmpty()){return result;}
        Location bouncingLoc = h.findBouncingLoc(dir,moves).get(0);
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

        bouncingLocs = h.findBouncingLoc(dir,moves);


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
        return 5;
    }

    @Override
    public String getName() {
        return "Bishop";
    }
}
