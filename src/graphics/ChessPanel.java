package graphics;

import main.*;
import pieces.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An abstract JPanel extension, which contains lots of graphics tools which are identical across variants.
 */
public abstract class ChessPanel extends JPanel implements Runnable {

    protected Board board;
    protected ChessPiece selectedPiece = null;
    protected int UIWidth = 200;
    protected int UIHeight = 100;
    protected Location offset = new Location(20, 20);
    public int cellWidth;
    public int cellHeight;
    public boolean animating = false;
    protected JButton load = new JButton("Load");
    protected JButton save = new JButton("Save");
    protected boolean verticalUI;
    private String code;
    private Font mainFont;

    /**
     * This constructor sets up a listener to handle the user clicking on the screen.
     * @param board The board which information will be obtained from.
     */
    protected ChessPanel(Board board) {
        this.board = board;
        this.addMouseListener(new HitTestAdapter());

        for (ChessPiece p : board.allPieces()) {
            p.graphics.givePanel(ChessPanel.this);
        }

        mainFont = createFont(24);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(this);
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
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final float[] FRACTIONS = {0.0f, 0.5f, 1.0f};
        final Color[] BG_COLOURS = {Color.WHITE.darker(), new Color(85, 55, 29), Color.DARK_GRAY};
        MultipleGradientPaint BG_GRADIENT = new LinearGradientPaint(new Point2D.Double(0, 0),
                new Point2D.Double(getSize().getWidth(), getSize().getHeight()), FRACTIONS, BG_COLOURS);
        g2.setPaint(BG_GRADIENT);
        g2.fillRect(0, 0, (int) getSize().getWidth(), (int) getSize().getHeight());

        g2.setFont(mainFont);
        doDrawing(g2);
    }

    protected Font createFont(int size) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = ge.getAllFonts();
        int index = (int) Math.floor(Math.random() * fonts.length);
        return new Font(fonts[index].getFontName(), Font.BOLD, size);
    }

    /**
     * This function draws all the UI elements which are not part of the board, such as the current turn and
     * turn counter.
     * @param g2 This is the graphics object which is being drawn to.
     */
    protected void drawUI(Graphics2D g2) {
        Color endCol = new Color(255, 255, 255, 0);
        Point2D start, end;
        if (board.getController().isWhitesTurn()) {
            start = new Point2D.Double(offset.getX() + board.numCols() * cellWidth, offset.getY());
            end = new Point2D.Double(start.getX() + offset.getX(), offset.getY());
        } else {
            start = new Point2D.Double(offset.getX(), offset.getY());
            end = new Point2D.Double(0, offset.getY());
        }

        GradientPaint plyPaint = new GradientPaint(start, Color.WHITE, end, endCol);
        g2.setPaint(plyPaint);
        int plyMarkX = (int) (end.getX() == 0 ? end.getX() : start.getX());
        g2.fillRect(plyMarkX, offset.getY(), offset.getX(), board.numRows() * cellHeight);

        if (board.getController().gameOver()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2.setColor(Color.GREEN);
            g2.fillRect(offset.getX(), offset.getY(), cellWidth * board.numCols(), cellHeight * board.numRows());
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            Font oldFont = g2.getFont();
            g2.setFont(createFont(70));
            g2.setColor(Color.GRAY);
            drawCentreString(board.getController().getWinner() + " Wins",
                    offset, cellWidth * board.numCols(), cellHeight * board.numRows(), g2);
            g2.setFont(oldFont);
        }

        int x, y;

        g2.setPaint(Color.WHITE);

        if (verticalUI) {
            x = offset.getX() * 2 + cellWidth * board.numCols();
            y = offset.getY();
            save.setLocation(x + UIWidth / 2, y);
            drawCentreString("Turn: " + board.getController().getCurrentTurn(), new Location(x, y + UIHeight / 2),
                    UIWidth, UIHeight, g2);
        } else {
            x = offset.getX();
            y = offset.getY() * 2 + cellHeight * board.numRows();
            save.setLocation(x, y + UIHeight / 2);
            g2.drawString("Turn: " + board.getController().getCurrentTurn(), x + 10 + UIWidth / 2, y + 10);
        }

        load.setLocation(x, y);
        load.setSize(UIWidth / 2, UIHeight / 2);
        save.setSize(UIWidth / 2, UIHeight / 2);

        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int start = code.indexOf("V:")+2;
                int end = code.indexOf('$',start);
                String variant = code.substring(start, end);
                GameController gameState = GameControllerMaker.get(variant, code);
                ChessVariant game = ChessVariantMaker.get(variant, gameState);
                game.drawBoard();
            }
        });

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                code = board.getController().toCode();
            }
        });

        if (load.getParent() == null) {
            this.add(load);
            this.add(save);
        }
    }

    protected void drawCentreString(String s, Location offset, int width, int height, Graphics2D g2) {
        FontMetrics fm = g2.getFontMetrics();
        int x = offset.getX() + (width - fm.stringWidth(s)) / 2;
        int y = offset.getY() + (fm.getAscent() + (height - (fm.getAscent() + fm.getDescent())) / 2);
        g2.drawString(s, x, y);
    }

    protected void drawGrid(Graphics2D g2) {
        g2.setColor(tools.BOARD_BLACK);
        for (int x = offset.getX(); x < offset.getX() + board.numRows() * cellWidth; x += cellWidth * 2) {
            for (int y = offset.getY(); y < offset.getY() + board.numCols() * cellHeight; y += cellHeight * 2) {
                g2.fillRect(x, y, cellWidth, cellHeight);
                g2.fillRect(x + cellWidth, y + cellHeight, cellWidth, cellHeight);
            }
        }

        g2.setColor(tools.BOARD_WHITE);
        for (int x = offset.getX(); x < offset.getX() + board.numRows() * cellWidth; x += cellWidth * 2) {
            for (int y = offset.getY(); y < offset.getY() + board.numCols() * cellHeight; y += cellWidth * 2) {
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
            g2.drawOval(l.getX() * cellWidth + offset.getX(), l.getY() * cellHeight + offset.getY(), cellWidth, cellHeight);
        }

        if (board.getController().isInCheck(false)) {
            Location l = board.getController().findKing(false);
            g2.drawOval(l.getX() * cellWidth + offset.getX(), l.getY() * cellHeight + offset.getY(), cellWidth, cellHeight);
        }

        if (selectedPiece != null) {
            g2.setStroke(new BasicStroke(2));
            g2.setPaint(tools.CUR_PIECE);
            g2.drawRect(selectedPiece.graphics.getX(), selectedPiece.graphics.getY(),
                    cellWidth, cellHeight);

            List<Location> moves = board.getController().movesForPiece(selectedPiece, true);
            if (selectedPiece.isWhite() == board.getController().isWhitesTurn()) {
                g2.setPaint(tools.CUR_MOVES);
            } else {
                g2.setPaint(Color.RED.darker());
            }
            for (Location l : moves) {
                g2.drawRect(l.getX() * cellWidth + offset.getX(), l.getY() * cellHeight + offset.getY(),
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

        int boardWidth = (int) getSize().getWidth() - UIWidth - offset.getX() * 2;
        int boardHeight = (int) getSize().getHeight() - UIHeight - offset.getY() * 2;

        if (boardWidth > boardHeight) {
            boardHeight = (int) getSize().getHeight() - offset.getY() * 2;
            verticalUI = true;
        } else {
            boardWidth = (int) getSize().getWidth() - offset.getX() * 2;
            verticalUI = false;
        }

        cellWidth = Math.round(Math.min(boardHeight / board.numRows(), boardWidth / board.numCols()) / 2) * 2;
        //noinspection SuspiciousNameCombination
        cellHeight = cellWidth;



        for (ChessPiece p : board.allPieces()) {
            p.graphics.curCords = new Location(p.cords.getX() * cellWidth + offset.getX(),
                                               p.cords.getY() * cellHeight + offset.getY());
            p.graphics.endCords = p.graphics.curCords.clone();
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
                int x = (e.getX() - offset.getX()) / cellWidth;
                int y = (e.getY() - offset.getY()) / cellHeight;
                Location l = new Location(x, y);

                if (!board.onBoard(l)) {
                    selectedPiece = null;
                } else if (selectedPiece == null) {
                    if (!board.isEmptySpace(l)) {
                        selectedPiece = board.getPiece(l);
                    }
                } else {
                    if (selectedPiece.allPieceMoves().contains(l)) {
                        board.getController().attemptMove(selectedPiece.cords, l, true);
                    }
                    selectedPiece = null;
                }
            }
            repaint();
        }
    }

    @Override
    public void run() {
        while (true) {
            recalculateCellSize();
            repaint();
        }
    }

}
