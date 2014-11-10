package ai;

import main.Board;
import main.Location;
import pieces.ChessPiece;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rob on 10/11/2014.
 */
public abstract class ChessAI {

    protected Board board;
    protected boolean isWhite;

    public ChessAI(Board board, boolean isWhite) {
        this.board = board;
        this.isWhite = isWhite;
    }

    public Location[] getBestMove() {
        List<Location[]> move = new ArrayList<Location[]>();
        int bestScore = Integer.MIN_VALUE;
        for (ChessPiece piece : board.allPieces()) {
            if (piece.isWhite() == isWhite) {
                for (Location l : piece.allPieceMoves()) {
                    int tempScore = evaluateMove(piece.cords, l);
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
        return move.get((int) Math.floor(Math.random() * move.size()));
    }

    protected abstract int evaluateMove(Location from, Location to);

}
