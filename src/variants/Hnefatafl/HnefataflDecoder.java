package variants.Hnefatafl;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import pieces.PieceDecoder;


/**
 * Created by crix9 on 21/11/2014.
 */
public class HnefataflDecoder extends PieceDecoder {

	protected ChessPiece generate (Board board, PieceType type, Location cords, String name) {
		HnefataflPieces piece = stringToPiece(name);
		switch(piece) {
			case HnefataflKing:
				return new HnefataflKing(board, type, cords);
			case Defender:
				return new Defender(board, type, cords);
			case Attacker:
				return new Attacker(board, type, cords);
			default:
				throw new Error("HOW DID YOU EVEN REACH THIS CASE!?");
		}
	}
	private HnefataflPieces stringToPiece(String string) {
		if(string.equals("HnefataflKing")) {
			return HnefataflPieces.HnefataflKing;
		} else if (string.equals("Defender")) {
			return HnefataflPieces.Defender;
		} else if (string.equals("Attacker")) {
			return HnefataflPieces.Attacker;
		} else {
			throw new Error("MALFORMED PIECE STRING ERROR ERROR DO NOT GIVE BAD STRINGS ANGRY ERROR");
		}
	}
}

enum HnefataflPieces {
	HnefataflKing, Defender, Attacker
}