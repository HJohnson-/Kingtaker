package graphics;

import main.Board;
import main.PieceType;
import pieces.ChessPiece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by rp1012 on 15/10/14.
 */
public abstract class ChessPanel extends JPanel {

    protected Board board;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    protected void drawPieces(Graphics2D g2) {
        LinkedList<ChessPiece> pieces = board.allPieces();

        Iterator<ChessPiece> i = pieces.iterator();
        while (i.hasNext()) {
            ChessPiece p = i.next();

            String fileName = "media/" + p.img;
            if (p.type == PieceType.BLACK) fileName += "Black";
            fileName += ".tif";

            TexturePaint texture = new TexturePaint(image, new Rectangle(0, 0, 50, 50));
            //g2.setPaint(texture);
            g2.fillRect(p.cords.getX() * tools.CELL_WIDTH, p.cords.getY() * tools.CELL_HEIGHT,
                    tools.CELL_WIDTH, tools.CELL_HEIGHT);
        }
    }

    protected abstract void doDrawing(Graphics g);

}
