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

        Bishop b = new Bishop(board, type, cords);

        String dir = h.getClockwiseDir(cords);

        List<Location> movesfirstBranchA = new LinkedList<Location>();
        List<Location> movesfirstBranchB = new LinkedList<Location>();

        // basic clockwise move
        for (Location move : b.allPieceMoves()) {
            addLocIntoBranch(cords, move, dir, movesfirstBranchA, movesfirstBranchB);
        }

        List<Location> bouncingLocsFirstBranch = new LinkedList<Location>();

        // find bouncing location
        if (!movesfirstBranchA.isEmpty()) {
            bouncingLocsFirstBranch.add(movesfirstBranchA.get(movesfirstBranchA.size() - 1));
        } else {
            // if the branch is empty
            bouncingLocsFirstBranch.add(new Location(-1, -1));
        }
        if (!movesfirstBranchB.isEmpty()) {
            bouncingLocsFirstBranch.add(movesfirstBranchB.get(movesfirstBranchB.size() - 1));
        } else {
            // if the branch is empty
            bouncingLocsFirstBranch.add(new Location(-1, -1));
        }

        List<Location> movesA1 = h.listClone(movesfirstBranchA);
        List<Location> movesA2 = h.listClone(movesfirstBranchA);
        List<Location> movesB1 = h.listClone(movesfirstBranchB);
        List<Location> movesB2 = h.listClone(movesfirstBranchB);

        // bounce on edge
        for (Location boundcingLoc : bouncingLocsFirstBranch) {
            if (boundcingLoc.getX() >= 0) {
                dir = h.getClockwiseDir(boundcingLoc);
                b = new Bishop(board, type, boundcingLoc);
                for (Location move : b.allPieceMoves()) {
                    if (bouncingLocsFirstBranch.indexOf(boundcingLoc) == 0) {
                        addLocIntoBranch(boundcingLoc, move, dir, movesA1, movesA2);
                    } else {
                        addLocIntoBranch(boundcingLoc, move, dir, movesB1, movesB2);
                    }
                }
            }
        }


        List<Location> moves;
        if (movesA1.contains(to)) {
            moves = movesA1;
        } else if (movesA2.contains(to)) {
            moves = movesA2;
        } else if (movesB1.contains(to)) {
            moves = movesB1;
        } else if (movesB2.contains(to)) {
            moves = movesB2;
        } else {
            // target location is moving backwork
            return true;
        }

        Location bouncingLoc = h.findBouncingLoc(moves, cords);

        String bouncedir = h.getClockwiseDir(bouncingLoc);

        if (bouncedir.equals("U")) {
            if (to.getY() < bouncingLoc.getY()) {
                result = result && h.rbClearLine(cords, bouncingLoc, board);
                result = result && h.bounceableClearline(bouncingLoc, to, board);
            } else {
                result = result && h.rbClearLine(cords, to, board);
            }
        }

        if (bouncedir.equals("D")) {
            if (to.getY() > bouncingLoc.getY()) {
                result = result && h.rbClearLine(cords, bouncingLoc, board);
                result = result && h.bounceableClearline(bouncingLoc, to, board);
            } else {
                result = result && h.rbClearLine(cords, to, board);
            }
        }

        if (bouncedir.equals("L")) {
            if (to.getX() < bouncingLoc.getX()) {
                result = result && h.rbClearLine(cords, bouncingLoc, board);
                result = result && h.bounceableClearline(bouncingLoc, to, board);
            } else {
                result = result && h.rbClearLine(cords, to, board);
            }
        }

        if (bouncedir.equals("R")) {
            if (to.getX() > bouncingLoc.getX()) {
                result = result && h.rbClearLine(cords, bouncingLoc, board);
                result = result && h.bounceableClearline(bouncingLoc, to, board);
            } else {
                result = result && h.rbClearLine(cords, to, board);
            }
        }

        return result;
    }

    @Override
    public List<Location> allPieceMoves() {
        // a list contains the bouncing location

        Bishop b = new Bishop(board, type, cords);

        String dir = h.getClockwiseDir(cords);

        List<Location> movesfirstBranchA = new LinkedList<Location>();
        List<Location> movesfirstBranchB = new LinkedList<Location>();

        // basic clockwise move
        for (Location move : b.allPieceMoves()) {
            addLocIntoBranch(cords, move, dir, movesfirstBranchA, movesfirstBranchB);
        }

        List<Location> bouncingLocsFirstBranch = new LinkedList<Location>();

        // find bouncing location
        if (!movesfirstBranchA.isEmpty()) {
            bouncingLocsFirstBranch.add(movesfirstBranchA.get(movesfirstBranchA.size() - 1));
        } else {
            // if the branch is empty
            bouncingLocsFirstBranch.add(new Location(-1, -1));
        }
        if (!movesfirstBranchB.isEmpty()) {
            bouncingLocsFirstBranch.add(movesfirstBranchB.get(movesfirstBranchB.size() - 1));
        } else {
            // if the branch is empty
            bouncingLocsFirstBranch.add(new Location(-1, -1));
        }

        List<Location> movesA1 = h.listClone(movesfirstBranchA);
        List<Location> movesA2 = h.listClone(movesfirstBranchA);
        List<Location> movesB1 = h.listClone(movesfirstBranchB);
        List<Location> movesB2 = h.listClone(movesfirstBranchB);

        // bounce on edge
        for (Location boundcingLoc : bouncingLocsFirstBranch) {
            if (boundcingLoc.getX() >= 0) {
                dir = h.getClockwiseDir(boundcingLoc);
                b = new Bishop(board, type, boundcingLoc);
                for (Location move : b.allPieceMoves()) {
                    if (bouncingLocsFirstBranch.indexOf(boundcingLoc) == 0) {
                        addLocIntoBranch(boundcingLoc, move, dir, movesA1, movesA2);
                    } else {
                        addLocIntoBranch(boundcingLoc, move, dir, movesB1, movesB2);
                    }
                }
            }
        }

        // all moves
        List<Location> moves = new LinkedList<Location>();

        // add location around rook
        Location newLoc = new Location(cords.getX() + 1, cords.getY() + 1);
        if (h.isValidedLoc(newLoc)) {
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX() + 1, cords.getY() - 1);
        if (h.isValidedLoc(newLoc)) {
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX() - 1, cords.getY() - 1);
        if (h.isValidedLoc(newLoc)) {
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX() - 1, cords.getY() + 1);
        if (h.isValidedLoc(newLoc)) {
            moves.add(newLoc);
        }

        moves.addAll(movesA1);
        moves.addAll(movesA2);
        moves.addAll(movesB1);
        moves.addAll(movesB2);

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

        Bishop b = new Bishop(board, type, cords);

        String dir = h.getClockwiseDir(cords);

        List<Location> movesfirstBranchA = new LinkedList<Location>();
        List<Location> movesfirstBranchB = new LinkedList<Location>();

        // basic clockwise move
        for (Location move : b.allPieceMoves()) {
            addLocIntoBranch(cords, move, dir, movesfirstBranchA, movesfirstBranchB);
        }

        List<Location> bouncingLocsFirstBranch = new LinkedList<Location>();

        // find bouncing location
        if (!movesfirstBranchA.isEmpty()) {
            bouncingLocsFirstBranch.add(movesfirstBranchA.get(movesfirstBranchA.size() - 1));
        } else {
            // if the branch is empty
            bouncingLocsFirstBranch.add(new Location(-1, -1));
        }
        if (!movesfirstBranchB.isEmpty()) {
            bouncingLocsFirstBranch.add(movesfirstBranchB.get(movesfirstBranchB.size() - 1));
        } else {
            // if the branch is empty
            bouncingLocsFirstBranch.add(new Location(-1, -1));
        }

        List<Location> movesA1 = h.listClone(movesfirstBranchA);
        List<Location> movesA2 = h.listClone(movesfirstBranchA);
        List<Location> movesB1 = h.listClone(movesfirstBranchB);
        List<Location> movesB2 = h.listClone(movesfirstBranchB);

        // bounce on edge
        for (Location boundcingLoc : bouncingLocsFirstBranch) {
            if (boundcingLoc.getX() >= 0) {
                dir = h.getClockwiseDir(boundcingLoc);
                b = new Bishop(board, type, boundcingLoc);
                for (Location move : b.allPieceMoves()) {
                    if (bouncingLocsFirstBranch.indexOf(boundcingLoc) == 0) {
                        addLocIntoBranch(boundcingLoc, move, dir, movesA1, movesA2);
                    } else {
                        addLocIntoBranch(boundcingLoc, move, dir, movesB1, movesB2);
                    }
                }
            }
        }

        List<Location> moves;
        if (movesA1.contains(targetLocation)) {
            moves = movesA1;
        } else if (movesA2.contains(targetLocation)) {
            moves = movesA2;
        } else if (movesB1.contains(targetLocation)) {
            moves = movesB1;
        } else if (movesB2.contains(targetLocation)) {
            moves = movesB2;
        } else {
            if (board.doDrawing) graphics.setGoal(targetLocation);
            board.clearSpace(cords);
            board.placePiece(targetLocation, this);
            this.lastTurnMovedOn = board.getController().getCurrentTurn();
            return true;
        }


        Location bouncingLoc = h.findBouncingLoc(moves, cords);
        String bouncedir = h.getClockwiseDir(bouncingLoc);

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


    private void addLocIntoBranch(Location from, Location to, String dir, List<Location> movesfirstBranchA, List<Location> movesfirstBranchB) {

        if (!h.isInMiddle(to)) {
            if (dir.equals("U") && (to.getY() < from.getY())) {
                if (to.getX() < from.getX()) {
                    if (!movesfirstBranchA.contains(to))
                        movesfirstBranchA.add(to);
                } else {
                    if (!movesfirstBranchB.contains(to))
                        movesfirstBranchB.add(to);
                }
            }

            if (dir.equals("D") && (to.getY() > from.getY())) {
                if (to.getX() > from.getX()) {
                    if (!movesfirstBranchA.contains(to))
                        movesfirstBranchA.add(to);
                } else {
                    if (!movesfirstBranchB.contains(to))
                        movesfirstBranchB.add(to);
                }
            }

            if (dir.equals("L") && (to.getX() < from.getX())) {
                if (to.getY() > from.getY()) {
                    if (!movesfirstBranchA.contains(to))
                        movesfirstBranchA.add(to);
                } else {
                    if (!movesfirstBranchB.contains(to))
                        movesfirstBranchB.add(to);
                }
            }

            if (dir.equals("R") && (to.getX() > from.getX())) {
                if (to.getY() < from.getY()) {
                    if (!movesfirstBranchA.contains(to))
                        movesfirstBranchA.add(to);
                } else {
                    if (!movesfirstBranchB.contains(to))
                        movesfirstBranchB.add(to);
                }
            }
        }
    }

    @Override
    public RBBishop clone() {
        return new RBBishop(board, type, cords.clone());
    }

}
