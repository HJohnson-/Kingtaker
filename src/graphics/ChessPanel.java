package graphics;

import BasicChess.BasicBoard;
import BasicChess.BasicChessFrame;
import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by rp1012 on 15/10/14.
 */
public abstract class ChessPanel extends JPanel {

    protected Board board;
    protected ChessPiece selectedPiece = null;

    protected ChessPanel() {
        this.addMouseListener(new HitTestAdapter());
    }

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

            String imgName = p.img;
            if (p.type == PieceType.BLACK) imgName += "Black";

            if (tools.imageMap.get(imgName) == null) System.err.println(imgName + " is null.");

            TexturePaint texture = new TexturePaint(tools.imageMap.get(imgName), new Rectangle(0, 0, 50, 50));
            g2.setPaint(texture);
            g2.fillRect(p.cords.getX() * tools.CELL_WIDTH, p.cords.getY() * tools.CELL_HEIGHT,
                    tools.CELL_WIDTH, tools.CELL_HEIGHT);
        }
    }

    protected abstract void doDrawing(Graphics g);

    class HitTestAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX() / tools.CELL_WIDTH;
            int y = e.getY() / tools.CELL_HEIGHT;
            Location l = new Location(x, y);

            if (selectedPiece == null) {
                if (!board.isEmptySpace(l)) {
                    selectedPiece = board.getPiece(l);
                }
            } else {
                board.attemptMove(selectedPiece.cords, l);
                selectedPiece = null;
                revalidate();
            }
        }
    }

}