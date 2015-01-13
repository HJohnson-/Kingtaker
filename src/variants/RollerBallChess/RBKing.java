package variants.RollerBallChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import variants.BasicChess.King;
import variants.BasicChess.Rook;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by daniel on 14/12/27.
 */
public class RBKing extends King {


    public RBKing(Board board, PieceType type, Location cords) {
        super(board, type, cords);
    }

    private RollBallRulesHelper h = new RollBallRulesHelper();

    /*To castle:
    - The target space must be two squares from the King

    - The target space must be in the direction of an allied rook.

    - Both pieces must be on the first rank

    - Neither piece can have moved

    - There must be a clear line between the pieces

    - The King cannot be in check, move through a threatened square, or end in check

    In all valid castling attempts, the for loop will catch if the actual move of castling ends in check.
    If a new piece moves in an especially unusual way, the variant might need to override the king piece with something
    that can check for it and undo a failed castle attempt correctly for the new situations
     */


    @Override
    public String getName() {
        return "RBKing";
    }

    @Override
    public boolean isValidMove(Location to, boolean careAboutCheck) {
        King k = new King(board, type, cords);
        if (careAboutCheck ? !validInState(to) : !k.validInStateNoCastle(to)) {
            return false;
        } else if (takingOwnPiece(board.getPiece(to))) {
            return false;
        } else if (h.isInMiddle(to.getX(), to.getY())) {
            return false;
        } else {
            if (careAboutCheck) {
                Location from = cords;
                boolean takingKing = board.getController().isKing(to);
                boolean wouldPutMeInCheck = testIfMoveEndsInCheck(to, from);
                if (wouldPutMeInCheck && !takingKing) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public boolean validInState(Location to) {
        King k = new King(board, type, cords.clone());
        return (k.validCastleAttempt(to) || k.adjacent(to)) && !h.isInMiddle(to);
    }

    @Override
    public List<Location> allPieceMoves() {

        List<Location> moves = new LinkedList<Location>();
        for (int i = -1; i <= 1; i++) {
            int newX = cords.getX() + i;
            if (newX < 0 || newX >= board.numCols()) continue;
            for (int j = -1; j <= 1; j++) {
                int newY = cords.getY() + j;
                if (newY < 0 || newY >= board.numRows()) continue;
                if (!h.isInMiddle(newX, newY))
                    moves.add(new Location(newX, newY));
            }
            if (lastTurnMovedOn == 0) {
                if (!h.isInMiddle(cords.getX(), cords.getY() + 2));
                    //moves.add(new Location(cords.getX(), cords.getY() + 2));
                if (!h.isInMiddle(cords.getX(), cords.getY() - 2));
                    //moves.add(new Location(cords.getX(), cords.getY() - 2));
            }
        }

        return moves;
    }

    @Override
    public int returnValue() {
        return 9999999;
    }

    @Override
    public boolean executeMove(Location to) {
        if(validCastleAttempt(to)) {
            int kingDirection = (int) Math.signum(to.getY() - cords.getY());
            int rookY = (kingDirection == 1 ? board.numRows() - 1 : 0 );
            Location rookCurrent = new Location(cords.getX(), rookY);
            Location rookTarget = new Location(cords.getX(), cords.getY() + kingDirection);

            if (board.doDrawing) {
                ChessPiece rook = board.getPiece(rookCurrent);
                rook.graphics.setGoal(rookTarget);
            }

            board.movePiece(rookCurrent, rookTarget);
            arriveOppKingLoc(to);
            return super.executeMove(to);
        } else {
            arriveOppKingLoc(to);
            return super.executeMove(to);
        }
    }

    private void arriveOppKingLoc(Location to){
        if(type==PieceType.BLACK){
            if(to.getX()==3&&to.getY()==5)
                board.getController().endGame(false);
        }else{
            if(to.getX()==3&&to.getY()==1)
                board.getController().endGame(false);
        }
    }

    @Override
    public RBKing clone() {
        return new RBKing(board, type, cords.clone());
    }

}
