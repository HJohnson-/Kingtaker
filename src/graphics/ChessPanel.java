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
import java.util.List;

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
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("Bauhaus", Font.BOLD, 16));
        doDrawing(g2);
    }

    protected void drawUI(Graphics2D g2) {
        int x = tools.CELL_WIDTH * (board.numCols() + 1);
        int y = tools.CELL_HEIGHT;
        Color c = board.getController().isWhitesTurn() ? new Color(0, 0, 0) : new Color(255, 255, 255);
        g2.setPaint(c);
        g2.fillRect(x, y, tools.CELL_WIDTH * 2, tools.CELL_HEIGHT);
        g2.setPaint(new Color(255, 255, 255));
        g2.drawString("Turn: " + board.getController().getCurrentTurn(), x, y + tools.CELL_HEIGHT * 2);

        if (board.getController().gameOver()) {
            g2.fillRect(0, 0, tools.CELL_WIDTH * board.numRows(), tools.CELL_HEIGHT * board.numCols());
            g2.setColor(new Color(255, 0, 0));
            g2.setFont(new Font("Bauhaus", Font.BOLD, 50));
            g2.drawString("Game Over", tools.CELL_WIDTH, tools.CELL_HEIGHT * (board.numRows() / 2));
            g2.drawString(board.getController().getWinner() + " Wins",
                    tools.CELL_WIDTH, tools.CELL_HEIGHT * (board.numRows() / 2 + 1));
        }
    }

    protected void drawPieces(Graphics2D g2) {
        LinkedList<ChessPiece> pieces = board.allPieces();

        Iterator<ChessPiece> i = pieces.iterator();
        while (i.hasNext()) {
            ChessPiece p = i.next();

            String imgName = p.img;
            if (p.type == PieceType.BLACK) imgName += "Black";

            if (tools.imageMap.get(imgName) == null) System.err.println(imgName + " is null.");


            TexturePaint texture;
            if (p.graphics.isAnimating()) {
                texture = new TexturePaint(tools.imageMap.get(imgName),
                        new Rectangle(p.graphics.getX(), p.graphics.getY(), 50, 50));
            } else {
                texture = new TexturePaint(tools.imageMap.get(imgName), new Rectangle(0, 0, 50, 50));
            }
            g2.setPaint(texture);
            g2.fillRect(p.graphics.getX(), p.graphics.getY(), tools.CELL_WIDTH, tools.CELL_HEIGHT);
        }

        if (selectedPiece != null) {
            Stroke oldstroke = g2.getStroke();
            g2.setStroke(new BasicStroke(2));
            g2.setPaint(new Color(143, 198, 222));
            g2.drawRect(selectedPiece.graphics.getX(), selectedPiece.graphics.getY(),
                    tools.CELL_WIDTH, tools.CELL_HEIGHT);

            List<Location> moves = board.getController().movesForPiece(selectedPiece, true);
            g2.setPaint(new Color(253, 8, 0));
            for (Location l : moves) {
                g2.drawRect(l.getX() * 50, l.getY() * 50, tools.CELL_WIDTH, tools.CELL_HEIGHT);
            }

            g2.setStroke(oldstroke);
        }

    }

    protected abstract void doDrawing(Graphics2D g2);

    class HitTestAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX() / tools.CELL_WIDTH;
            int y = e.getY() / tools.CELL_HEIGHT;
            Location l = new Location(x, y);

            if (!board.onBoard(l)) {
                selectedPiece = null;
            } else if (selectedPiece == null) {
                if (!board.isEmptySpace(l)) {
                    selectedPiece = board.getPiece(l);
                }
            } else {
                if (selectedPiece.allPieceMoves().contains(l)) {
                    selectedPiece.graphics.setGoal(l);
                    selectedPiece.graphics.givePanel(ChessPanel.this);
                    if (board.getController().attemptMove(selectedPiece.cords, l)) {
                        Thread t = new Thread(selectedPiece.graphics);
                        selectedPiece = null;
                        t.start();
                    }
                }
                selectedPiece = null;
            }

            repaint();
        }
    }

}