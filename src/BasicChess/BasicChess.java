package BasicChess;

import main.ChessVariant;
import main.Location;
import main.PieceType;

/**
 * Created by crix9 on 15/10/2014.
 */
public class BasicChess extends ChessVariant {

	public BasicChess(){
		board = new BasicBoard();

	}

	//returns true if human player goes first in offline mode or the lobby host goes first in online mode.
	public boolean initializeBoard(){

		// black pawns
		for(int j = 0; j < 8; j++){
			Location location = new Location(1,j);
			board.placePiece(location, new Pawn(board, PieceType.BLACK));
		}


		//black pieces
		for(int i = 0; i < 8; i++){
			Location location = new Location(0,i);
			switch (i){
				case 0:
				case 7:
					board.placePiece(location, new Rook(board, PieceType.BLACK));
					break;
				case 1:
				case 6:
					board.placePiece(location, new Knight(board, PieceType.BLACK));
					break;
				case 2:
				case 5:
					board.placePiece(location, new Bishop(board, PieceType.BLACK));
					break;
				case 3:
					board.placePiece(location, new Queen(board, PieceType.BLACK));
					break;
				case 4:
					board.placePiece(location, new King(board, PieceType.BLACK));
					break;
				default:

			}
		}


		// white pawns
		for(int j = 0; j < 8; j++){
			Location location = new Location(6,j);
			board.placePiece(location, new Pawn(board, PieceType.WHITE));
		}

		//black pieces
		for(int i = 0; i < 8; i++){
			Location location = new Location(7,i);
			switch (i){
				case 0:
				case 7:
					board.placePiece(location, new Rook(board, PieceType.WHITE));
					break;
				case 1:
				case 6:
					board.placePiece(location, new Knight(board, PieceType.WHITE));
					break;
				case 2:
				case 5:
					board.placePiece(location, new Bishop(board, PieceType.WHITE));
					break;
				case 3:
					board.placePiece(location, new Queen(board, PieceType.WHITE));
					break;
				case 4:
					board.placePiece(location, new King(board, PieceType.WHITE));
					break;
				default:

			}
		}


		return false;
	}

	//returns true if there was no errors
	public boolean drawBoard(boolean fromWhitePerspective){
		return false;
	}
}
