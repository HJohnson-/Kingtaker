package main;

/**
 * A type to identify what type of piece is located on a given square on the chess board.
 */
public enum PieceType {
    WHITE("White"), BLACK("Black"), EMPTY("Empty");

	private final String string;

	PieceType(String string) {
		this.string = string;
	}

	public String string() {
		return string;
	}
}
