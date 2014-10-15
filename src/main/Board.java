package main;

/**
 * Created by hj1012 on 15/10/14.
 */
abstract public class Board {
	private pieces.ChessPiece[][] pieces;

	public pieces.ChessPiece getPiece(Location pieceLocation) {
		if(!onBoard(pieceLocation)) {
			return null;
		}
		return pieces[pieceLocation.getX()][pieceLocation.getY()];
	}

	public boolean placePiece(Location targetLocation, pieces.ChessPiece toPlace) {
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
			pieces.ChessPiece beingMoved = pieces[pieceLocation.getX()][pieceLocation.getY()];
			clearSpace(pieceLocation);
			placePiece(targetLocation, beingMoved);
			return true;
		} catch (Error e) {
			System.out.println(e);
			return false;
		}

	}

	public boolean isEmptySpace(Location targetLocation) {
		return getPiece(targetLocation).type == PieceType.EMPTY;
	}

	private void clearSpace(Location pieceLocation) {
		placePiece(pieceLocation, new pieces.EmptyPiece(this));
	}

	private boolean onBoard(Location targetLocation) {
		if(targetLocation.getX() < 0 || targetLocation.getY() < 0){
			return false;
		}
		if(targetLocation.getX() >= pieces.length || targetLocation.getY() >= pieces[0].length) {
			return false;
		}
		return true;
	}

	public int numRows() {
		return pieces.length;
	}
}