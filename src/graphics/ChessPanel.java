package graphics;

import ai.MinimaxAI;
import main.*;
import pieces.ChessPiece;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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

    public Board board;
    protected ChessPiece selectedPiece = null;
    protected int UIHeight = 100;
    protected Location offset = new Location(20, 20);
    public int cellWidth;
    public int cellHeight;
    public boolean animating = false;
    protected JButton btnLoad = new JButton("Load");
    protected JButton btnSave = new JButton("Save");
    protected JButton btnUndo = new JButton("Undo");
    public StopWatch stopWatch ;
    protected JPanel time;
    protected JLabel turnLabel = new JLabel();
    protected JPanel panelTurn = new JPanel();
    protected JLabel lblDifficultySlider = new JLabel("Difficulty", JLabel.CENTER);
    protected JProgressBar pbAIProgress = new JProgressBar(0,100);
    protected JSlider sliderAIDifficulty = new JSlider();
    protected JPanel panelAIDifficulty = new JPanel();
    private String savedBoardCodeString = null;
    private Font fontGameOver = new Font("", Font.BOLD, 24);
    private static double lastLoad = 0;
    private final int gapBetweenCol = 10;
    // position y for 3 rows
    private final int gapBetweenRow = 10;
    private final int gapBetweenBoard = 30;

    /* This constructor sets up a listener to handle the user clicking on the screen.
    * @param board The board which information will be obtained from.
    */
    protected ChessPanel(final Board board) {
        setLayout(null);

        this.board = board;
        this.addMouseListener(new HitTestAdapter());
        this.addComponentListener(new ResizeAdapter());

        for (ChessPiece p : board.allPieces()) {
            p.graphics.givePanel(ChessPanel.this);
        }

        recalculateCellSize();

        sliderAIDifficulty.setMinimum(0);
        sliderAIDifficulty.setMaximum(5);
        sliderAIDifficulty.setValue(MinimaxAI.DEFAULT_AI_LEVEL);
        sliderAIDifficulty.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                ChessPanel.this.board.getController().setDifficulty(sliderAIDifficulty.getValue());
            }
        });

        stopWatch = new StopWatch();
        time = stopWatch.buildStopWatch(board);

        btnLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (System.currentTimeMillis() - 300 > lastLoad) {
                    lastLoad = System.currentTimeMillis();
                    board.getController().load(savedBoardCodeString);
                    recalculateCellSize();
                }
            }
        });

        btnUndo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (System.currentTimeMillis() - 300 > lastLoad) {
                    lastLoad = System.currentTimeMillis();
                    board.getController().undo();
                    recalculateCellSize();
                }
            }
        });

        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                savedBoardCodeString = board.getController().toCode();
            }
        });

        turnLabel.setBackground(GraphicsTools.CUR_PIECE);
        turnLabel.setOpaque(true);
        turnLabel.setHorizontalTextPosition(JLabel.CENTER);
        turnLabel.setVerticalTextPosition(JLabel.CENTER);
        turnLabel.setFont(new Font(time.getFont().getName(), Font.PLAIN, 12));

        panelTurn.setLayout(new BorderLayout());
        panelTurn.setOpaque(true);
        panelTurn.setBackground(GraphicsTools.CUR_PIECE);
        panelTurn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelTurn.setBounds(1, 1, 1, 1);
        panelTurn.add(turnLabel, BorderLayout.CENTER);

        time.setBounds(1,1,1,1);

        pbAIProgress.setStringPainted(true);
        pbAIProgress.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        lblDifficultySlider.setAlignmentX(Component.CENTER_ALIGNMENT);

        sliderAIDifficulty.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sliderAIDifficulty.setMajorTickSpacing(5);
        sliderAIDifficulty.setMinorTickSpacing(1);
        sliderAIDifficulty.setPaintTicks(true);
        sliderAIDifficulty.setPaintLabels(true);

        panelAIDifficulty.add(lblDifficultySlider);
        panelAIDifficulty.add(sliderAIDifficulty);
        panelAIDifficulty.setBackground(new Color(85, 55, 29));
        panelAIDifficulty.setOpaque(false);


        drawSwingComponents();

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

        //g2.setFont(mainFont);

        doDrawing(g2);
    }

    /**
     * This function draws all the UI elements which are not part of the board, such as the current turn and
     * turn counter.
     * @param g2 This is the graphics object which is being drawn to.
     */
    protected void drawUI(Graphics2D g2) {
        String gameResultString = getGameResultString(board.getController().getResult());
        if (gameResultString != null) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2.setColor(Color.GREEN);
            g2.fillRect(offset.getX(), offset.getY(), cellWidth * board.numCols(), cellHeight * board.numRows());
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            Font oldFont = g2.getFont();
            g2.setFont(fontGameOver);
            g2.setColor(Color.GRAY);
            drawCentreString(gameResultString,
                    offset, cellWidth * board.numCols(), cellHeight * board.numRows(), g2);
            g2.setFont(oldFont);
        }
    }

    protected void drawCentreString(String s, Location offset, int width, int height, Graphics2D g2) {
        FontMetrics fm = g2.getFontMetrics();
        int x = offset.getX() + (width - fm.stringWidth(s)) / 2;
        int y = offset.getY() + (fm.getAscent() + (height - (fm.getAscent() + fm.getDescent())) / 2);
        g2.drawString(s, x, y);
    }

    protected void drawGrid(Graphics2D g2) {
        g2.setColor(GraphicsTools.BOARD_BLACK);
        for (int x = offset.getX(); x < offset.getX() + board.numRows() * cellWidth; x += cellWidth * 2) {
            for (int y = offset.getY(); y < offset.getY() + board.numCols() * cellHeight; y += cellHeight * 2) {
                g2.fillRect(x, y, cellWidth, cellHeight);
                g2.fillRect(x + cellWidth, y + cellHeight, cellWidth, cellHeight);
            }
        }

        g2.setColor(GraphicsTools.BOARD_WHITE);
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

            if (GraphicsTools.imageMap.get(imgName) == null) System.err.println(imgName + " is null.");


            TexturePaint texture = new TexturePaint(GraphicsTools.imageMap.get(imgName),
                    new Rectangle(p.graphics.getX(), p.graphics.getY(), cellWidth, cellHeight));
            g2.setPaint(texture);
            g2.fillRect(p.graphics.getX(), p.graphics.getY(), cellWidth, cellHeight);
        }

        g2.setPaint(GraphicsTools.CHECK);
        Stroke oldstroke = g2.getStroke();
        g2.setStroke(new BasicStroke(4));

        GameController gc = board.getController();
        if (gc.isInCheck(true)) {
            Location l = gc.findKing(true);
            g2.drawOval(l.getX() * cellWidth + offset.getX(), l.getY() * cellHeight + offset.getY(), cellWidth, cellHeight);
        }

        if (gc.isInCheck(false)) {
            Location l = gc.findKing(false);
            g2.drawOval(l.getX() * cellWidth + offset.getX(), l.getY() * cellHeight + offset.getY(), cellWidth, cellHeight);
        }

        if (selectedPiece != null) {
            g2.setStroke(new BasicStroke(2));
            g2.setPaint(GraphicsTools.CUR_PIECE);
            g2.drawRect(selectedPiece.graphics.getX(), selectedPiece.graphics.getY(),
                    cellWidth, cellHeight);

            List<Location> moves = gc.movesForPiece(selectedPiece, true);
            if (selectedPiece.isWhite() == gc.isWhitesTurn() && gc.isLocalsTurn()) {
                g2.setPaint(GraphicsTools.CUR_MOVES);
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

        int boardWidth = (int) getSize().getWidth() - offset.getX() * 2;
        int boardHeight = (int) getSize().getHeight() - UIHeight - offset.getY() * 2;

        cellWidth = Math.round(Math.min(boardHeight / board.numRows(), boardWidth / board.numCols()) / 2) * 2;
        //noinspection SuspiciousNameCombination
        cellHeight = cellWidth;

        for (ChessPiece p : board.allPieces()) {
            p.graphics.givePanel(this);
            p.graphics.curCords = new Location(p.cords.getX() * cellWidth + offset.getX(),
                    p.cords.getY() * cellHeight + offset.getY());
            p.graphics.endCords = p.graphics.curCords.clone();
        }
    }

    //Returns the appropriate message to display based on the game's result
    //or null if the game is still in progress.
    private String getGameResultString(GameResult gameResult) {
        switch (board.getController().getResult()) {
            case DRAW:
                return "Stalemate! It is a draw";
            case WHITE_WIN:
                return "Checkmate! White wins";
            case WHITE_LOSS:
                return "Checkmate! Black wins";
        }
        return null;
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
            if (!animating && board.getController().getResult() == GameResult.IN_PROGRESS) {
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
        }
    }



    class ResizeAdapter extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            ChessPanel.this.recalculateCellSize();
        }
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int targetFPS = gd.getDisplayMode().getRefreshRate();
        if (targetFPS == DisplayMode.REFRESH_RATE_UNKNOWN) {
            targetFPS = 120;
        }
        long lastDraw = System.currentTimeMillis();


            /* new graphic */
            stopWatch.start(); //todo REMOVE
        while (true) {
            if (!stopWatch.isRunning){
                stopWatch.isRunning = true;
                stopWatch.start();
            }

            while (true) {
                try {Thread.sleep((1000 / targetFPS));} catch (InterruptedException e) {}
                //System.out.println((1000 / targetFPS) - (System.currentTimeMillis() - lastDraw));
                lastDraw = System.currentTimeMillis();
                if (!Toolkit.getDefaultToolkit().getLockingKeyState(
                        KeyEvent.VK_CAPS_LOCK)) {
                    drawSwingComponents();
                    repaint();
                } else {
                    board.getController().endGame(false);
                }

            }

        }
    }

    private void drawSwingComponents() {
        // size x
        int threeWidth = (this.getWidth() - offset.getX()*2 - gapBetweenCol * 2) / 3;

        // position x for 3 columns
        int p3col1 = offset.getX();
        int p3col2 = p3col1+threeWidth+gapBetweenCol;
        int p3col3 = p3col2+threeWidth+gapBetweenCol;



        // size y for 3 rows
        int threeHeight = (this.getHeight()-board.numRows()* cellHeight-gapBetweenBoard-gapBetweenRow*2-offset.getY()*2)/3;
        int p3row1 = board.numRows()* cellHeight + offset.getY()+gapBetweenBoard;
        int p3row2 = p3row1+threeHeight+gapBetweenRow;
        int p3row3 = p3row2+threeHeight+gapBetweenRow;

        // size y for 2 rows
        int twoHeight = (this.getHeight()-board.numRows()* cellHeight-gapBetweenBoard-gapBetweenRow-offset.getY()*2)/2;
        // position y for 2 rows
        int p2row1 = board.numRows()* cellHeight + offset.getY()+gapBetweenBoard;
        int p2row2 = p2row1+twoHeight+gapBetweenRow;

        stopWatch.isWhite = board.getController().isWhitesTurn();

        int x, y;

        x = offset.getX();
        y = offset.getY() * 2 + cellHeight * board.numRows();

        turnLabel.setText(String.format("<html>Turn Number: %04d<html>", board.getController().getCurrentTurn()));


        panelTurn.setLocation(p3col2, p2row1);
        panelTurn.setSize(threeWidth, threeHeight);


        //Save and load buttons.
        btnSave.setLocation(p3col1, p3row1);
        btnSave.setSize(threeWidth, threeHeight);

        btnLoad.setLocation(p3col1, p3row2);
        btnLoad.setSize(threeWidth, threeHeight);

        btnUndo.setLocation(p3col1, p3row3);
        btnUndo.setSize(threeWidth, threeHeight);

        /* new graphic */


        time.setLocation(p3col2, p3row2);
        time.setSize(threeWidth, gapBetweenRow + threeHeight * 2);

        time.setFont(new Font(time.getFont().getName(), Font.PLAIN, time.getHeight()/5));



        /* new graphic */

        //AI progress bar and difficulty controller.
        if (board.getController().gameMode == GameMode.SINGLE_PLAYER) {
            int newX = offset.getX() + (cellWidth * board.numCols() / 2);

            int barWidth = 2 * cellWidth * board.numCols() / 5;
            int barHeight = 20;

            double completed = board.getController().getAI().pcComplete();
            int total = board.getController().getAI().getTotal();
            int done = board.getController().getAI().getCompleted();

            pbAIProgress.setMaximum(total);
            pbAIProgress.setValue(done);

            pbAIProgress.setLocation(p3col3, p3row1);
            pbAIProgress.setSize(threeWidth, threeHeight);

            lblDifficultySlider.setSize(threeWidth, threeHeight);

            sliderAIDifficulty.setBounds(1, 1, 1, 1);
            sliderAIDifficulty.setSize(threeWidth, threeHeight);

            panelAIDifficulty.setLocation(p3col3, p3row2);
            panelAIDifficulty.setSize(threeWidth, threeHeight);

        }

        if (btnLoad.getParent() == null) {
            if (board.getController().gameMode != GameMode.MULTIPLAYER_ONLINE) {
                this.add(btnLoad);
                this.add(btnSave);
                this.add(btnUndo);
            }
            this.add(time);
            this.add(panelTurn);
            if (board.getController().gameMode == GameMode.SINGLE_PLAYER) {
                this.add(pbAIProgress);
                this.add(panelAIDifficulty);
            }
        }
    }

}
