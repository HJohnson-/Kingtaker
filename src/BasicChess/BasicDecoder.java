package BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import pieces.PieceDecoder;

/**
 * Turns a string into a ChessPiece
 */
public class BasicDecoder extends PieceDecoder {
	protected ChessPiece generate (Board board, PieceType type, Location cords, String name) {
		BasicPieces piece = stringToPiece(name);
		switch(piece) {
			case Pawn:
				return new Pawn(board, type, cords);
			case Queen:
				return new Queen(board, type, cords);
			case King:
				return new King(board, type, cords);
			case Bishop:
				return new Bishop(board, type, cords);
			case Knight:
				return new Knight(board, type, cords);
			case Rook:
				return new Rook(board, type, cords);
			default:
				throw new Error("HOW DID YOU EVEN REACH THIS CASE!?");
		}
	}
	private BasicPieces stringToPiece(String string) {
		if(string.equals("Queen")) {
			return BasicPieces.Queen;
		} else if (string.equals("King")) {
			return BasicPieces.King;
		} else if (string.equals("Pawn")) {
			return BasicPieces.Pawn;
		} else if (string.equals("Knight")) {
			return BasicPieces.Knight;
		} else if (string.equals("Rook")) {
			return BasicPieces.Rook;
		} else if (string.equals("Bishop")) {
			return BasicPieces.Bishop;
		} else {
			throw new Error("MALFORMED PIECE STRING ERROR ERROR DO NOT GIVE BAD STRINGS ANGRY ERROR");
		}
	}
}

enum BasicPieces {
	Queen, King, Pawn, Knight, Rook, Bishop
}