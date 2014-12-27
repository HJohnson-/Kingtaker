package variants.RollerBallChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import java.util.List;

/**
 * Created by daniel on 14/12/26.
 */
public class RBPawn extends ChessPiece {
    public RBPawn(Board board, PieceType type, Location cords) {
        super(board, type, cords, "PBPawn");
    }

    @Override
    protected boolean validInState(Location to) {
        return false;
    }

    @Override
    public List<Location> allPieceMoves() {
        return null;
    }

    @Override
    public int returnValue() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }
}
