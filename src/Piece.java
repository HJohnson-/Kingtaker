/**
 * Created by rp1012 on 15/10/14.
 */
abstract public class Piece {

    private Board board;
    protected boolean isWhite;

    public Piece(Board board) {
        this.board = board;
    }

    abstract public boolean canYouMoveTo(Location from, Location to);

}
