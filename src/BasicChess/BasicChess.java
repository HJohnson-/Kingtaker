package BasicChess;

import graphics.ChessPanel;
import main.Board;
import main.ChessVariant;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Created by crix9 on 15/10/2014.
 */
public class BasicChess extends ChessVariant {

    public ChessPiece selectedPiece = null;
    public final int boardWidth = 8;
    public final int boardHeight = 8;

	public BasicChess(){
		board = new BasicBoard();
	}

	//returns true if human player goes first in offline mode or the lobby host goes first in online mode.
	public boolean initializeBoard(){

		// black pawns
		for(int j = 0; j < 8; j++){
			Location location = new Location(1,j);
			board.placePiece(location, new Pawn(board, PieceType.BLACK, location));
		}


		//black pieces
		for(int i = 0; i < 8; i++){
			Location location = new Location(0,i);
			switch (i){
				case 0:
				case 7:
					board.placePiece(location, new Rook(board, PieceType.BLACK, location));
					break;
				case 1:
				case 6:
					board.placePiece(location, new Knight(board, PieceType.BLACK, location));
					break;
				case 2:
				case 5:
					board.placePiece(location, new Bishop(board, PieceType.BLACK, location));
					break;
				case 3:
					board.placePiece(location, new Queen(board, PieceType.BLACK, location));
					break;
				case 4:
					board.placePiece(location, new King(board, PieceType.BLACK, location));
					break;
				default:

			}
		}


		// white pawns
		for(int j = 0; j < 8; j++){
			Location location = new Location(6,j);
			board.placePiece(location, new Pawn(board, PieceType.WHITE, location));
		}

		//black pieces
		for(int i = 0; i < 8; i++){
			Location location = new Location(7,i);
			switch (i){
				case 0:
				case 7:
					board.placePiece(location, new Rook(board, PieceType.WHITE, location));
					break;
				case 1:
				case 6:
					board.placePiece(location, new Knight(board, PieceType.WHITE, location));
					break;
				case 2:
				case 5:
					board.placePiece(location, new Bishop(board, PieceType.WHITE, location));
					break;
				case 3:
					board.placePiece(location, new Queen(board, PieceType.WHITE, location));
					break;
				case 4:
					board.placePiece(location, new King(board, PieceType.WHITE, location));
					break;
				default:

			}
		}


		return false;
	}

	//returns true if there was no errors
	public boolean drawBoard() {

        graphics.tools.create(new BasicChessFrame("Basic Chess", boardWidth, boardHeight, (BasicBoard) board));
        return true;
	}
}
