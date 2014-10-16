package BasicChess;

import graphics.ChessPanel;
import graphics.tools;
import pieces.ChessPiece;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by rp1012 on 16/10/14.
 */
public class BasicChessPanel extends ChessPanel {

    private final int GRID_WIDTH = 8;
    private final int GRID_HEIGHT = 8;
    private BasicBoard board;

    public BasicChessPanel(BasicBoard board) {
        this.board = board;
    }

    protected void doDrawing(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        graphics.tools.drawGrid(g2, GRID_WIDTH, GRID_HEIGHT);

        LinkedList<ChessPiece> pieces = board.allPieces();

        Iterator<ChessPiece> i = pieces.iterator();
        while (i.hasNext()) {
            ChessPiece p = i.next();
            BufferedImage piece = null;
            try {
                piece = ImageIO.read(new File(p.img));
            } catch (IOException e) {
                System.err.println("Invalid file name: " + p.img);
                System.exit(2);
            }

            TexturePaint texture = new TexturePaint(piece, new Rectangle(0, 0, 50, 50));

            g2.setPaint(texture);
            g2.fillRect(p.cords.getX(), p.cords.getY(), tools.CELL_WIDTH, tools.CELL_HEIGHT);
        }
    }

}
