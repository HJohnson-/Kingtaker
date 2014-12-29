package variants.RollerBallChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import variants.BasicChess.BasicDecoder;

public class RBDecoder extends BasicDecoder {
  		@Override
		protected ChessPiece generate (Board board, PieceType type, Location cords, String name) {
			RBPieces piece = stringToPiece(name);
		switch(piece) {
				case RBRook:
					return new RBRook(board, type, cords);
				case RBBishop:
					return new RBBishop(board, type, cords);
				case RBPawn:
					return new RBPawn(board, type, cords);
				case RBKing:
					return new RBKing(board, type, cords);
				default:
					throw new Error("HOW DID YOU EVEN REACH THIS CASE!?");
			}
		}

	private RBPieces stringToPiece(String string) {
		if(string.equals("RBRook")) {
			return RBPieces.RBRook;
		} else if (string.equals("RBBishop")) {
			return RBPieces.RBBishop;
		} else if(string.equals("RBPawn")) {
			return RBPieces.RBPawn;
		} else if(string.equals("RBKing")){
			return RBPieces.RBKing;
		} else {
			throw new Error("MALFORMED PIECE STRING ERROR ERROR DO NOT GIVE BAD STRINGS ANGRY ERROR");
		}
	}
}

enum RBPieces {
	RBRook, RBBishop, RBPawn, RBKing
}


