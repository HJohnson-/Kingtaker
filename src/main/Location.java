package main;

/**
 * A basic coordinate system for finding a location on a chess board
 */
public class Location {
	private int x;
	private int y;

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Integer getX(){
		return x;
	}
	public Integer getY(){
		return y;
	}

    public void incrX(int increment) {
        this.x += increment;
    }

    public void incrY(int increment) {
        this.y += increment;
    }

	@Override
	public boolean equals(Object obj) {
		Location that;
		if(obj instanceof Location) {
			that = (Location) obj;
		} else {
			return false;
		}
		return (this.x == that.x && this.y == that.y);
	}

    @Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

    @Override
    public Location clone() {
        return new Location(x, y);
    }
}
