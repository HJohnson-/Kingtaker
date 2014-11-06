package graphics;

import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An abstract JPanel extension, which contains lots of graphics tools which are identical across variants.
 */
public abstract class ChessPanel extends JPanel {

    protected Board board;
    protected ChessPiece selectedPiece = null;
    public int cellWidth;
    public int cellHeight;
    protected int UIWidth = 200;
    public boolean animating = false;
    JButton load = new JButton("Load");
    JButton save = new JButton("Save");

    /**
     * This constructor sets up a listener to handle the user clicking on the screen.
     * @param board The board which information will be obtained from.
     */
    protected ChessPanel(Board board) {
        this.board = board;
        this.addMouseListener(new HitTestAdapter());
        this.addComponentListener(new resizeListener());

        for (ChessPiece p : board.allPieces()) {
            p.graphics.givePanel(ChessPanel.this);
        }
        recalculateCellSize();
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
        int x = cellWidth * (board.numCols());
        int y = 0;

        load.setLocation(x, y);
        load.setSize(100, cellHeight);
        save.setLocation(x + 100, y);
        save.setSize(100, cellHeight);

        if (load.getParent() == null) {
            this.add(load);
            this.add(save);
        }

        y += cellHeight;

        Color c = board.getController().isWhitesTurn() ? Color.BLACK : Color.WHITE;
        g2.setPaint(c);
        g2.fillRect(x + UIWidth / 4, y, UIWidth / 2, cellHeight);
        g2.setPaint(tools.TEXT);
        x += 10;
        g2.drawString("Turn: " + board.getController().getCurrentTurn(), x, y + cellHeight * 2);

        if (board.getController().gameOver()) {
            g2.setFont(new Font("Bauhaus", Font.BOLD, 50));
            g2.drawString("Game Over", cellWidth, cellHeight * board.numRows());
            g2.drawString(board.getController().getWinner() + " Wins",
                    cellWidth, cellHeight * (board.numRows() / 2 + 1));
        }
    }

    protected void drawGrid(Graphics2D g2) {
        g2.setColor(tools.BOARD_BLACK);
        for (int x = 0; x < board.numRows() * cellWidth; x += cellWidth * 2) {
            for (int y = 0; y < board.numCols() * cellHeight; y += cellHeight * 2) {
                g2.fillRect(x, y, cellWidth, cellHeight);
                g2.fillRect(x + cellWidth, y + cellHeight, cellWidth, cellHeight);
            }
        }

        g2.setColor(tools.BOARD_WHITE);
        for (int x = 0; x < board.numRows() * cellWidth; x += cellWidth * 2) {
            for (int y = 0; y < board.numCols() * cellHeight; y += cellWidth * 2) {
                g2.fillRect(x + cellWidth, y, cellWidth, cellHeight);
                g2.fillRect(x, y + cellHeight, cellWidth, cellHeight);
            }
        }
    }

    /**
     * This function takes all the pieces on the board and draws them onto the board in the correct place.
     * If a piece is currently selected, it highlights that piece and also all the squares that piece can move to.
     * @param g2 This is the graphics object which is being drawn to.
     */
    protected void drawPieces(Graphics2D g2) {
        LinkedList<ChessPiece> pieces = board.allPieces();

        for (ChessPiece p : pieces) {

            String imgName = p.image;
            if (p.type == PieceType.BLACK) imgName += "Black";

            if (tools.imageMap.get(imgName) == null) System.err.println(imgName + " is null.");


            TexturePaint texture = new TexturePaint(tools.imageMap.get(imgName),
                    new Rectangle(p.graphics.getX(), p.graphics.getY(), cellWidth, cellHeight));
            g2.setPaint(texture);
            g2.fillRect(p.graphics.getX(), p.graphics.getY(), cellWidth, cellHeight);
        }

        g2.setPaint(tools.CHECK);
        Stroke oldstroke = g2.getStroke();
        g2.setStroke(new BasicStroke(4));

        if (board.getController().isInCheck(true)) {
            Location l = board.getController().findKing(true);
            g2.drawOval(l.getX() * cellWidth, l.getY() * cellHeight, cellWidth, cellHeight);
        }

        if (board.getController().isInCheck(false)) {
            Location l = board.getController().findKing(false);
            g2.drawOval(l.getX() * cellWidth, l.getY() * cellHeight, cellWidth, cellHeight);
        }

        if (selectedPiece != null) {
            g2.setStroke(new BasicStroke(2));
            g2.setPaint(tools.CUR_PIECE);
            g2.drawRect(selectedPiece.graphics.getX(), selectedPiece.graphics.getY(),
                    cellWidth, cellHeight);

            List<Location> moves = board.getController().movesForPiece(selectedPiece, true);
            g2.setPaint(tools.CUR_MOVES);
            for (Location l : moves) {
                g2.drawRect(l.getX() * cellWidth, l.getY() * cellHeight,
                        cellWidth, cellHeight);
            }
        }

        g2.setStroke(oldstroke);

    }

    /**
     * This handles drawing the pieces and UI, which all chess variants will need (although it can be overridden).
     * @param g2 This is the graphics object which is being drawn to.
     */
    protected void doDrawing(Graphics2D g2) {
        drawGrid(g2);
        drawPieces(g2);
        drawUI(g2);
    }

    /**
     * Recalculated how large the board needs to be, based on the current size of the panel.
     */
    public void recalculateCellSize() {
        while (animating) Thread.yield();
        cellWidth = (int) Math.round((
                Math.min(getSize().getWidth() - UIWidth, getSize().getHeight()) / board.numRows()) / 2) * 2;
        cellHeight = cellWidth;

        for (ChessPiece p : board.allPieces()) {
            p.graphics.curCords = new Location(p.cords.getX() * cellWidth, p.cords.getY() * cellHeight);
            p.graphics.endCords = new Location(p.cords.getX() * cellWidth, p.cords.getY() * cellHeight);
            p.graphics.totalSteps = cellWidth / 2;
            p.graphics.animationTime = 1500 / cellWidth;
        }
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
            if (!animating && !board.getController().gameOver()) {
                int x = e.getX() / cellWidth;
                int y = e.getY() / cellHeight;
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
                        if (board.getController().attemptMove(selectedPiece.cords, l)) {
                            ExecutorService pool = Executors.newFixedThreadPool(1);
                            pool.submit(selectedPiece.graphics);
                        }
                    }
                    selectedPiece = null;
                }
            }
            repaint();
        }
    }

    class resizeListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            ChessPanel.this.recalculateCellSize();
            ChessPanel.this.repaint();
        }
    }

}