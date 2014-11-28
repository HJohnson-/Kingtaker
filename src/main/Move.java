package main;

/**
 * Created by rp1012 on 28/10/14.
 */
public class Move {

    private Location from;
    private Location to;
    private String pieceType;
    private boolean take;
    private String pieceTakenType;

    public Move(Location from, Location to, String pieceType) {
        this.from = from;
        this.to = to;
        this.pieceType = pieceType;
    }

	public Location getFrom() {
		return from;
	}

	public Location getTo() {
		return to;
	}

	public Move(Location from, Location to, String pieceType, String pieceTakenType) {
        this(from, to, pieceType);

        this.pieceTakenType = pieceTakenType;
        this.take = true;
    }

    @Override
    public String toString() {
        if (take) {
            return (pieceType + " took " + pieceTakenType + " " + from + " to " + to);
        } else {
            return (pieceType + " moved " + from + " to " + to);
        }
    }

}
