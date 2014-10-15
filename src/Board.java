/**
 * Created by hj1012 on 15/10/14.
 */
abstract public class Board {
	private Piece[][] pieces;

	public Piece getPiece(Location pieceLocation) {
		return pieces[pieceLocation.getX()][pieceLocation.getY()];
	}

	public boolean placePiece(Location targetLocation, Piece toPlace) {
		try {
			pieces[targetLocation.getX()][targetLocation.getY()] = toPlace;
			return true;
		} catch (Error e) {
			return false;
		}
	}

	//Assumes move is valid
	public boolean executeMove(Location pieceLocation, Location targetLocation) {
		try {
			Piece beingMoved = pieces[pieceLocation.getX()][pieceLocation.getY()];
			clearSpace(pieceLocation);
			placePiece(targetLocation, beingMoved);
			return true;
		} catch (Error e) {
			System.out.println(e);
			return false;
		}

	}

	public boolean isEmptySpace(Location targetLocation) {
		return getPiece(targetLocation).isEmpty;
	}

	private void clearSpace(Location pieceLocation) {
		placePiece(pieceLocation, new EmptySquare(this));
	}
}