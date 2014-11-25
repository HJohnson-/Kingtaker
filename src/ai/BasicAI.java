package ai;

import main.Board;
import main.Location;
import pieces.ChessPiece;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic AI, looks to see which move it makes will give it the highest board score.
 */
public class BasicAI extends ChessAI {

    public BasicAI(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public Location[] getBestMove(Board board) {
        List<Location[]> move = new ArrayList<Location[]>();
        int bestScore = Integer.MIN_VALUE;
        for (ChessPiece piece : board.allPieces()) {
            if (piece.isWhite() == isWhite) {
                for (Location l : piece.allPieceMoves()) {
                    if (piece.isValidMove(l, true)) {
                        int tempScore = evaluateMove(l, board);
                        if (tempScore > bestScore) {
                            bestScore = tempScore;
                            move.clear();
                            move.add(new Location[]{piece.cords, l});
                        } else if (tempScore == bestScore) {
                            move.add(new Location[]{piece.cords, l});
                        }
                    }
                }
            }
        }
        return move.get((int) Math.floor(Math.random() * move.size()));
    }


    protected int evaluateMove(Location to, Board board) {
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
        return score;
    }
}
