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
        super(board, type, cords, "rook");
    }

    @Override
    protected boolean validInState(Location to) {
//      in outer ring and not in the same horizontal or virtical direction
        if (h.inOuterRing(cords) && ((cords.getX() - to.getX()) != 0 && (cords.getY() - to.getY()) != 0)) {
            Location conner = h.findOuterCornerByLoc(cords);
            return board.clearLine(cords, conner) && h.bounceableClearline(conner, to, board) && !h.isInMiddle(to);
        }
        return board.clearLine(cords, to) && !h.isInMiddle(to);
    }

    @Override
    public List<Location> allPieceMoves() {
        Rook r = new Rook(board, type, cords);
        List<Location> moves = new LinkedList<Location>();
        String dir = h.getClockwiseDir(cords);

        for (Location move : r.allPieceMoves()) {
            if (!h.isInMiddle(move)) {
                if (dir.equals("U") && (move.getY() < cords.getY()) ||
                        dir.equals("D") && (move.getY() > cords.getY()) ||
                        dir.equals("L") && (move.getX() < cords.getX()) ||
                        dir.equals("R") && (move.getX() > cords.getX())) {
                    moves.add(move);
                }
            }
        }


        // bounce in conner
        if (h.inOuterRing(cords)) {
            List<Location> pair = h.containOuterCorner(moves);
            Location firstOuterConner = pair.get(0);
            r = new Rook(board, type, firstOuterConner);
            dir = h.getClockwiseDir(firstOuterConner);

            for (Location move : r.allPieceMoves()) {
                if (!h.isInMiddle(move)) {
                    if (dir.equals("U") && (move.getY() < cords.getY()) ||
                            dir.equals("D") && (move.getY() > cords.getY()) ||
                            dir.equals("L") && (move.getX() < cords.getX()) ||
                            dir.equals("R") && (move.getX() > cords.getX())) {
                        moves.add(move);
                    }
                }
            }
        }

        // move in special case in inner loop
        if (cords.getX() == 1 && cords.getY() == 6 || cords.getX() == 0 && cords.getY() == 1 || cords.getX() == 5 && cords.getY() == 0 || cords.getX() == 6 && cords.getY() == 5) {
            Location conner = h.findOuterCornerByLoc(cords);
            dir = h.getClockwiseDir(conner);
            r = new Rook(board, type, cords);
            for (Location move : r.allPieceMoves()) {
                if (!h.isInMiddle(move)) {
                    if (dir.equals("U") && (move.getY() < cords.getY()) ||
                            dir.equals("D") && (move.getY() > cords.getY()) ||
                            dir.equals("L") && (move.getX() < cords.getX()) ||
                            dir.equals("R") && (move.getX() > cords.getX())) {
                        moves.add(move);
                    }
                }
            }
        }

        // add location around rook
        Location newLoc = new Location(cords.getX() + 1, cords.getY());
        if (h.isValidedLoc(newLoc) && !moves.contains(newLoc)) {
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX(), cords.getY() + 1);
        if (h.isValidedLoc(newLoc) && !moves.contains(newLoc)) {
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX() - 1, cords.getY());
        if (h.isValidedLoc(newLoc) && !moves.contains(newLoc)) {
            moves.add(newLoc);
        }
        newLoc = new Location(cords.getX(), cords.getY() - 1);
        if (h.isValidedLoc(newLoc) && !moves.contains(newLoc)) {
            moves.add(newLoc);
        }

        return moves;
    }

    @Override
    public int returnValue() {
        return 6;
    }

    @Override
    public String getName() {
        return "RBRook";
    }

    @Override
    public boolean executeMove(Location targetLocation) {


        // graphic for bouncing rook
        if (h.inOuterRing(cords) && (cords.getX() - targetLocation.getX()) != 0 && (cords.getY() - targetLocation.getY()) != 0) {
            Location conner = h.findOuterCornerByLoc(cords);

            board.clearSpace(cords);
            board.placePiece(targetLocation, this);

            if (board.doDrawing) graphics.setGoal(conner);
            if (board.doDrawing) graphics.setGoal(targetLocation);

        } else {

            // graphic for non bouncing rook
            if (board.doDrawing) graphics.setGoal(targetLocation);
        }

        board.clearSpace(cords);
        board.placePiece(targetLocation, this);
        this.lastTurnMovedOn = board.getController().getCurrentTurn();
        return true;
    }

    @Override
    public RBRook clone() {
        return new RBRook(board, type, cords.clone());
    }
}
