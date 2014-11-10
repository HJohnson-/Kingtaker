package ai;

import main.Board;
import main.Location;
import pieces.ChessPiece;

import java.util.List;

/**
 * A basic AI, looks to see which move it makes will give it the highest board score.
 */
public class BasicAI extends ChessAI {

    public BasicAI(Board board, boolean isWhite) {
        super(board, isWhite);
    }

    @Override
    protected int evaluateMove(Location from, Location to) {
        int score = 0;
        List<ChessPiece> pieces = board.allPieces();
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).cords.equals(to)) {
                pieces.remove(i);
            }
        }
        for (ChessPiece p : pieces) {
            if (p.isWhite() == isWhite) {
                score += p.returnValue();
            } else {
                score -= p.returnValue();
            }
        }
        return 0;
    }
}
