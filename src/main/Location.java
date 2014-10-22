package main;

/**
 * Created by hj1012 on 15/10/14.
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
}
