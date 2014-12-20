package variants.GrandChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import variants.BasicChess.BasicDecoder;

/**
 * A custom decoder for the grand chess game variant because it has custom pieces
 */
public class GrandDecoder extends BasicDecoder {
	@Override
	protected ChessPiece generate(Board board, PieceType type, Location cords, String name) {
			GrandPieces piece = stringToPiece(name);
			switch(piece) {
				case Marshal:
					return new Marshal(board, type, cords);
				case Cardinal:
					return new Cardinal(board, type, cords);
				case Basic:
					return super.generate(board, type, cords, name);
				default:
					throw new Error("HOW DID YOU EVEN REACH THIS CASE!?");
			}
		}
	private GrandPieces stringToPiece(String string) {
		if(string.equals("Marshal")) {
			return GrandPieces.Marshal;
		} else if (string.equals("Cardinal")) {
			return GrandPieces.Cardinal;
		} else {
			return GrandPieces.Basic;
		}
	}
}

enum GrandPieces {
	Marshal, Cardinal, Basic
}


