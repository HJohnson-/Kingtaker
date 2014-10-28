package graphics;

import BasicChess.BasicBoard;
import BasicChess.BasicChessFrame;
import BasicChess.King;
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

    /**
     * This constructor sets up a listener to handle the user clicking on the screen.
     * @param board The board which information will be obtained from.
     */
    protected ChessPanel(Board board) {
        this.board = board;
        this.addMouseListener(new HitTestAdapter());
    }

    /**
     * This is the main JPanel function for drawing the panel, which is extended so that we can tell it to also draw
     * the chess board.
     * @param g A graphics object, responsible for handling everything drawn to the screen.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("Bauhaus", Font.BOLD, 16));
        doDrawing(g2);
    }

    /**
     * This function draws all the UI elements which are not part of the board, such as the current turn and
     * turn counter.
     * @param g2 This is the graphics object which is being drawn to.
     */
    protected void drawUI(Graphics2D g2) {
        int x = tools.CELL_WIDTH * (board.numCols() + 1);
        int y = tools.CELL_HEIGHT;
        Color c = board.getController().isWhitesTurn() ? tools.BLACK : tools.WHITE;
        g2.setPaint(c);
        g2.fillRect(x, y, tools.CELL_WIDTH * 2, tools.CELL_HEIGHT);
        g2.setPaint(tools.TEXT);
        g2.drawString("Turn: " + board.getController().getCurrentTurn(), x, y + tools.CELL_HEIGHT * 2);

        if (board.getController().gameOver()) {
            g2.setFont(new Font("Bauhaus", Font.BOLD, 50));
            g2.drawString("Game Over", tools.CELL_WIDTH, tools.CELL_HEIGHT * board.numRows());
            g2.drawString(board.getController().getWinner() + " Wins",
                    tools.CELL_WIDTH, tools.CELL_HEIGHT * (board.numRows() / 2 + 1));
        }
    }

    /**
     * This function takes all the pieces on the board and draws them onto the board in the correct place.
     * If a piece is currently selected, it highlights that piece and also all the squares that piece can move to.
     * @param g2 This is the graphics object which is being drawn to.
     */
    protected void drawPieces(Graphics2D g2) {
        LinkedList<ChessPiece> pieces = board.allPieces();

        Iterator<ChessPiece> i = pieces.iterator();
        while (i.hasNext()) {
            ChessPiece p = i.next();

            String imgName = p.img;
            if (p.type == PieceType.BLACK) imgName += "Black";

            if (tools.imageMap.get(imgName) == null) System.err.println(imgName + " is null.");


            TexturePaint texture = new TexturePaint(tools.imageMap.get(imgName),
                    new Rectangle(p.graphics.getX(), p.graphics.getY(), tools.CELL_WIDTH, tools.CELL_HEIGHT));
            g2.setPaint(texture);
            g2.fillRect(p.graphics.getX(), p.graphics.getY(), tools.CELL_WIDTH, tools.CELL_HEIGHT);
        }

        g2.setPaint(tools.CHECK);
        Stroke oldstroke = g2.getStroke();
        g2.setStroke(new BasicStroke(4));

        if (board.getController().isInCheck(true)) {
            Location l = board.getController().findKing(true);
            g2.drawOval(l.getX() * tools.CELL_WIDTH, l.getY() * tools.CELL_HEIGHT, tools.CELL_WIDTH, tools.CELL_HEIGHT);
        }

        if (board.getController().isInCheck(false)) {
            Location l = board.getController().findKing(false);
            g2.drawOval(l.getX() * tools.CELL_WIDTH, l.getY() * tools.CELL_HEIGHT, tools.CELL_WIDTH, tools.CELL_HEIGHT);
        }

        if (selectedPiece != null) {
            g2.setStroke(new BasicStroke(2));
            g2.setPaint(tools.CUR_PIECE);
            g2.drawRect(selectedPiece.graphics.getX(), selectedPiece.graphics.getY(),
                    tools.CELL_WIDTH, tools.CELL_HEIGHT);

            List<Location> moves = board.getController().movesForPiece(selectedPiece, true);
            g2.setPaint(tools.CUR_MOVES);
            for (Location l : moves) {
                g2.drawRect(l.getX() * tools.CELL_WIDTH, l.getY() * tools.CELL_HEIGHT,
                        tools.CELL_WIDTH, tools.CELL_HEIGHT);
            }
        }

        g2.setStroke(oldstroke);

    }

    /**
     * This handles drawing the pieces and UI, which all chess variants will need (although it can be overridden).
     * @param g2 This is the graphics object which is being drawn to.
     */
    protected void doDrawing(Graphics2D g2) {
        drawPieces(g2);
        drawUI(g2);
    }

    class HitTestAdapter extends MouseAdapter {

        /**
         * This function handles what actions are taken when the user clicks on the board.
         * It has several cases to handle a click off the board, on an invalid move, a valid move, and when there
         * is not a currently selected piece.
         * @param e This holds the co-ordinates of where the mouse was clicked.
         */
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