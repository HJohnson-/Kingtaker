package BasicChess;

import graphics.ChessPanel;
import graphics.tools;
import main.*;
import pieces.ChessPiece;
import pieces.EmptyPiece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by crix9 on 15/10/2014.
 */
public class BasicChess extends ChessVariant {

    public ChessPiece selectedPiece = null;
    public final int boardWidth = 8;
    public final int boardHeight = 8;

	public BasicChess(){
		game = new GameController(new BasicBoard());
		game.getBoard().setController(game);
	}

	//returns true if there was no errors
	public boolean drawBoard() {

        graphics.tools.create(new BasicChessFrame("Basic Chess", 600, 600, (BasicBoard) game.getBoard()));
        return true;
	}
}
