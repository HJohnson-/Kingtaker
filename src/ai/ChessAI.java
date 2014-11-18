package ai;

import main.Board;
import main.Location;

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

    abstract public Location[] getBestMove();

}
