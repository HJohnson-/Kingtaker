package variants.RollerBallChess;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;
import variants.BasicChess.BasicDecoder;

public class RBDecoder extends BasicDecoder {
    @Override
    protected ChessPiece generate(Board board, PieceType type, Location cords, String name) {
        RBPieces piece = stringToPiece(name);
			switch(piece) {
				case Rook:
					return new RBRook(board, type, cords);
				case Bishop:
					return new RBBishop(board, type, cords);
				case Pawn:
					return new RBPawn(board, type, cords);
				case King:
					return new RBKing(board, type, cords);
				case Basic:
					return super.generate(board, type, cords, name);
				default:
					throw new Error("HOW DID YOU EVEN REACH THIS CASE!?");
			}
		}

	private RBPieces stringToPiece(String string) {
		if(string.equals("Rook")) {
			return RBPieces.Rook;
		} else if (string.equals("Bishop")) {
			return RBPieces.Bishop;
		} else if(string.equals("Pawn")) {
			return RBPieces.Pawn;
		} else if(string.equals("King")){
			return RBPieces.King;
		} else {
			return RBPieces.Basic;
		}
	}
}

enum RBPieces {
	Rook, Bishop, Pawn,King, Basic
}


