package variants.RollerBallChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import variants.BasicChess.King;
import variants.BasicChess.Pawn;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by daniel on 14/12/26.
 */
public class RBPawn extends ChessPiece {

    private RollBallRulesHelper h = new RollBallRulesHelper();

    public RBPawn(Board board, PieceType type, Location cords) {
        super(board, type, cords, "Pawn");
    }

    @Override
    protected boolean validInState(Location to) {
        King k = new King(board, type, cords);
        return k.validInState(to) && !h.isInMiddle(to);
         }

    @Override
    public List<Location> allPieceMoves() {
        King k = new King(board, type, cords);
        List<Location> moves = new LinkedList<Location>();

        for(Location move:k.allPieceMoves()){
            String dir = h.getClockWiseDir(cords);
            if(!h.isInMiddle(move)) {
                if(dir.equals("U")&&(move.getY()==cords.getY()-1) ||
                        dir.equals("D")&&(move.getY()==cords.getY()+1)||
                        dir.equals("L")&&(move.getX()==cords.getX()-1)||
                        dir.equals("R")&&(move.getX()==cords.getX()+1)){
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    @Override
    public int returnValue() {
        return 1;
    }

    @Override
    public String getName() {
        return "Pawn";
    }
}
