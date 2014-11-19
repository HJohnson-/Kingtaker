package ai;

import main.Board;
import main.Location;

/**
 * Created by Rob on 10/11/2014.
 */
public abstract class ChessAI {

    protected boolean isWhite;

    public ChessAI(boolean isWhite) {
        this.isWhite = isWhite;
    }

    abstract public Location[] getBestMove(Board board);

}
