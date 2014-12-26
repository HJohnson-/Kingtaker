package graphics;

import ai.MinimaxAI;
import main.*;
import pieces.ChessPiece;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
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

    protected Board board;
    protected ChessPiece selectedPiece = null;
    protected int UIHeight = 100;
    protected Location offset = new Location(20, 20);
    public int cellWidth;
    public int cellHeight;
    public boolean animating = false;
    protected JButton load = new JButton("Load");
    protected JButton save = new JButton("Save");
    protected JButton undo = new JButton("Undo");
    public StopWatch stopWatch ;
    protected  JPanel time;
    protected JLabel turnLabel = new JLabel();
    protected JPanel turn = new JPanel();
    protected JLabel sliderLabel = new JLabel("Difficulty", JLabel.CENTER);
    protected JProgressBar pb = new JProgressBar(0,100);
    protected JSlider difficulty = new JSlider();
    protected JPanel diffJpanel = new JPanel();
    private String code;
    private Font mainFont;
    private Font bigMainFont;
    private int fps;
    private boolean drawFPS = true;
    private static double lastLoad = 0;
    private boolean hasImplement = false;
    private Border blackline = BorderFactory.createLineBorder(Color.black);
    private TitledBorder AItitle = BorderFactory.createTitledBorder(blackline, "AI Progress");
    private TitledBorder Dtitle = BorderFactory.createTitledBorder(" difficulty ");
    private int bordergap = 20;

    /* This constructor sets up a listener to handle the user clicking on the screen.
    * @param board The board which information will be obtained from.
    */
    protected ChessPanel(final Board board) {
        this.board = board;
        this.addMouseListener(new HitTestAdapter());
        this.addComponentListener(new ResizeAdapter());

        for (ChessPiece p : board.allPieces()) {
            p.graphics.givePanel(ChessPanel.this);
        }

        mainFont = createFont(24);
        bigMainFont = createFont(70);
        recalculateCellSize();

        difficulty.setMinimum(0);
        difficulty.setMaximum(5);
        difficulty.setValue(MinimaxAI.DEFAULT_AI_LEVEL);
        difficulty.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                ChessPanel.this.board.getController().setDifficulty(difficulty.getValue());
            }
        });


        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (System.currentTimeMillis() - 300 > lastLoad) {
                    lastLoad = System.currentTimeMillis();
                    GameController gc = board.getController();
                    gc.load(code);
                    recalculateCellSize();
                }
            }
        });

        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (System.currentTimeMillis() - 300 > lastLoad) {
                    lastLoad = System.currentTimeMillis();
                    GameController gc = board.getController();
                    gc.undo();
                    recalculateCellSize();
                }
            }
        });

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                code = board.getController().toCode();
            }
        });

        if(stopWatch==null) {
            stopWatch = new StopWatch();
            time = stopWatch.buildStopWatch();
//            synchronized (stopWatch);
        }else{
            stopWatch.resetTime();
        }
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
        long start = System.nanoTime();

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

        long elapsed = System.nanoTime() - start;
        fps = (int) (Math.pow(10, 9) / elapsed);
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
        int gapBetweenCol = 10;
        // size x
        int threeWidth = (this.getWidth() - offset.getX()*2 - gapBetweenCol * 2) / 3;

        // position x for 3 columns
        int p3col1 = offset.getX();
        int p3col2 = p3col1+threeWidth+gapBetweenCol;
        int p3col3 = p3col2+threeWidth+gapBetweenCol;

        // position y for 3 rows
        int gapBetweenRow = 10;
        int gapBetweenBoard = 30;

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




        Color endCol = new Color(255, 255, 255, 0);
        Point2D start, end;


//        if (board.getController().isWhitesTurn()) {
//            start = new Point2D.Double(offset.getX() + board.numCols() * cellWidth, offset.getY());
//            end = new Point2D.Double(start.getX() + offset.getX(), offset.getY());
//        } else {
//            start = new Point2D.Double(offset.getX(), offset.getY());
//            end = new Point2D.Double(0, offset.getY());
//        }
//
//        GradientPaint plyPaint = new GradientPaint(start, Color.WHITE, end, endCol);
//        g2.setPaint(plyPaint);
//        int plyMarkX = (int) (end.getX() == 0 ? end.getX() : start.getX());
//        g2.fillRect(plyMarkX, offset.getY(), offset.getX(), board.numRows() * cellHeight);

        if ( board.getController().isWhitesTurn()) {
            stopWatch.isWhite = Boolean.TRUE;
            stopWatch.time.setBackground(tools.BOARD_WHITE);
            stopWatch.clockPanel2.setBackground(tools.BOARD_WHITE);
        } else {
            stopWatch.isWhite = Boolean.FALSE;
            stopWatch.time.setBackground(tools.BOARD_BLACK);
            stopWatch.clockPanel2.setBackground(tools.BOARD_BLACK);
        }


        String gameResultString = getGameResultString(board.getController().getResult());
        if (gameResultString != null) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2.setColor(Color.GREEN);
            g2.fillRect(offset.getX(), offset.getY(), cellWidth * board.numCols(), cellHeight * board.numRows());
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            Font oldFont = g2.getFont();
            g2.setFont(bigMainFont);
            g2.setColor(Color.GRAY);
            drawCentreString(gameResultString,
                    offset, cellWidth * board.numCols(), cellHeight * board.numRows(), g2);
            g2.setFont(oldFont);
        }


        int x, y;

        g2.setPaint(Color.WHITE);

        x = offset.getX();
        y = offset.getY() * 2 + cellHeight * board.numRows();


        /* new graphic */
        turnLabel.setText(String.format("<html>NUM OF TURNS <br> %04d<html>", board.getController().getCurrentTurn()));
        turnLabel.setFont(new Font(time.getFont().getName(), Font.PLAIN, time.getHeight() / 4));

        turnLabel.setBackground(tools.CUR_PIECE);
        turnLabel.setOpaque(true);

        turnLabel.setHorizontalTextPosition(JLabel.CENTER);
        turnLabel.setVerticalTextPosition(JLabel.CENTER);

        turn.setLayout(new BorderLayout());
        turn.setOpaque(true);
        turn.setBackground(tools.CUR_PIECE);
        turn.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        turn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        turn.add(turnLabel, BorderLayout.CENTER);

        turn.add(turnLabel);

        turn.setBounds(1,1,1,1);
        turn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        turn.setLocation(p3col2, p2row1);
        turn.setSize(threeWidth,twoHeight);


        //Save and load buttons.
        save.setLocation(p3col1,p3row1);
        save.setSize(threeWidth,threeHeight);

        load.setLocation(p3col1, p3row2);
        load.setSize(threeWidth,threeHeight);

        undo.setLocation(p3col1, p3row3);
        undo.setSize(threeWidth,threeHeight);

        /* new graphic */

        time.setBounds(1,1,1,1);
        time.setLocation(p3col2,p2row2);
        time.setSize(threeWidth,twoHeight);
        StopWatch.time.setFont(new Font(time.getFont().getName(), Font.PLAIN, time.getHeight()/5));

        /* new graphic */

        //AI progress bar and difficulty controller.
        if (board.getController().gameMode == GameMode.SINGLE_PLAYER) {
            int newX = offset.getX() + (cellWidth * board.numCols() / 2);

            int barWidth = 2 * cellWidth * board.numCols() / 5;
            int barHeight = 20;

            double completed = board.getController().getAI().pcComplete();
            int total = board.getController().getAI().getTotal();
            int done = board.getController().getAI().getCompleted();

            pb.setMaximum(total);

            pb.setValue(done);

            AItitle.setTitleJustification(TitledBorder.CENTER);

            pb.setBorder(AItitle);
            pb.setStringPainted(true);
            pb.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            pb.setLocation(p3col3,p3row1);
            pb.setSize(threeWidth,threeHeight);


            sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            sliderLabel.setSize(threeWidth,threeHeight);
            difficulty.setBounds(1, 1, 1, 1 );
            difficulty.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            difficulty.setMajorTickSpacing(5);
            difficulty.setMinorTickSpacing(1);
            difficulty.setPaintTicks(true);
            difficulty.setPaintLabels(true);

            difficulty.setSize(threeWidth,threeHeight);

            diffJpanel.add(sliderLabel);
            diffJpanel.add(difficulty);

            diffJpanel.setLocation(p3col3,p3row2);
            diffJpanel.setSize(threeWidth,threeHeight);
            diffJpanel.setBackground(new Color(85, 55, 29));
            diffJpanel.setOpaque(false);


        }

        if (load.getParent() == null) {
            this.add(load);
            this.add(save);
            this.add(undo);
        /* new graphic */

            this.add(time);
            this.add(turn);
            if (board.getController().gameMode == GameMode.SINGLE_PLAYER) {
                this.add(pb);
            }
        /* new graphic */
            if (board.getController().gameMode == GameMode.SINGLE_PLAYER) this.add(diffJpanel);
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
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int targetFPS = gd.getDisplayMode().getRefreshRate();
        if (targetFPS == DisplayMode.REFRESH_RATE_UNKNOWN) {
            targetFPS = 120;
        }
        long lastDraw = System.currentTimeMillis();


            /* new graphic */
            stopWatch.start();
        hasImplement = true;
        while (true) {
            if(!stopWatch.isRunning){
                stopWatch.isRunning = true;
                stopWatch.start();
            }

            try {
                Thread.sleep(1000 / targetFPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            repaint();
            lastDraw = System.currentTimeMillis();
        }
    }

}
