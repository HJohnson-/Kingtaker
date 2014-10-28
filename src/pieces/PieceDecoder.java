package pieces;

import main.Board;
import main.Location;
import main.PieceType;

/**
 * Turns a string
 */
abstract public class PieceDecoder {

	private static final int startOfFirst = 3;
	private static final int sizeOfBuffer = 3;
	private static final char delimiter = '~';
	private static final char endOfPieceChar = '|';

	public ChessPiece decode(String code, Board board) {
		int startOfValue = startOfFirst;
		int endOfValue = code.indexOf(delimiter);
		String name = code.substring(startOfValue, endOfValue);

		startOfValue = nextVal(endOfValue);
		endOfValue = code.indexOf(delimiter, startOfValue);
		int lastMovedOn = Integer.decode(code.substring(startOfValue, endOfValue));
		startOfValue = nextVal(endOfValue);
		endOfValue = code.indexOf(delimiter, startOfValue);
		int x = Integer.decode(code.substring(startOfValue, endOfValue));
		startOfValue = nextVal(endOfValue);
		endOfValue = code.indexOf(delimiter, startOfValue);
		int y = Integer.decode(code.substring(startOfValue, endOfValue));
		startOfValue = nextVal(endOfValue);
		endOfValue = code.indexOf(delimiter, startOfValue);
		PieceType type = code.substring(startOfValue, endOfValue).equals("White") ? PieceType.WHITE : PieceType.BLACK;
		ChessPiece decoded = generate(board, type, new Location(x, y), name);
		startOfValue = nextVal(endOfValue);
		endOfValue = code.indexOf(endOfPieceChar, startOfValue);
		decoded.finishGen(lastMovedOn, code.substring(startOfValue, endOfValue));
		return decoded;
	}

	abstract protected ChessPiece generate (Board board, PieceType type, Location cords, String name);

	private int nextVal(int endOfValue) {
		return endOfValue + sizeOfBuffer;
	}
}
