package BasicChess;

import main.ChessVariant;
import main.Location;
import main.PieceType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Created by crix9 on 15/10/2014.
 */
public class BasicChess extends ChessVariant {

	public BasicChess(){
		board = new BasicBoard();

	}

	//returns true if human player goes first in offline mode or the lobby host goes first in online mode.
	public boolean initializeBoard(){

		// black pawns
		for(int j = 0; j < 8; j++){
			Location location = new Location(1,j);
			board.placePiece(location, new Pawn(board, PieceType.BLACK));
		}


		//black pieces
		for(int i = 0; i < 8; i++){
			Location location = new Location(0,i);
			switch (i){
				case 0:
				case 7:
					board.placePiece(location, new Rook(board, PieceType.BLACK));
					break;
				case 1:
				case 6:
					board.placePiece(location, new Knight(board, PieceType.BLACK));
					break;
				case 2:
				case 5:
					board.placePiece(location, new Bishop(board, PieceType.BLACK));
					break;
				case 3:
					board.placePiece(location, new Queen(board, PieceType.BLACK));
					break;
				case 4:
					board.placePiece(location, new King(board, PieceType.BLACK));
					break;
				default:

			}
		}


		// white pawns
		for(int j = 0; j < 8; j++){
			Location location = new Location(6,j);
			board.placePiece(location, new Pawn(board, PieceType.WHITE));
		}

		//black pieces
		for(int i = 0; i < 8; i++){
			Location location = new Location(7,i);
			switch (i){
				case 0:
				case 7:
					board.placePiece(location, new Rook(board, PieceType.WHITE));
					break;
				case 1:
				case 6:
					board.placePiece(location, new Knight(board, PieceType.WHITE));
					break;
				case 2:
				case 5:
					board.placePiece(location, new Bishop(board, PieceType.WHITE));
					break;
				case 3:
					board.placePiece(location, new Queen(board, PieceType.WHITE));
					break;
				case 4:
					board.placePiece(location, new King(board, PieceType.WHITE));
					break;
				default:

			}
		}


		return false;
	}

	//returns true if there was no errors
	public boolean drawBoard(boolean fromWhitePerspective) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BasicChessBoard bcb = new BasicChessBoard();
                bcb.setVisible(true);
            }
        });

        return true;
	}
}

class ChessBoardFrame extends JFrame {

    public ChessBoardFrame() {
        initUI();
    }

    private void initUI() {
        JFrame frame = new JFrame();

        frame.setTitle("Basic Chess");

        frame.add(new BasicChessBoard());

        frame.setSize(400, 428);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }
}

class BasicChessBoard extends JPanel {

    public BasicChessBoard() {
        initBoard();
    }

    private Rectangle2D rect;

    private void initBoard() {
        this.addMouseListener(new HitTestAdapter());

        rect = new Rectangle2D.Float(0f, 50f, 50f, 50f);
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        graphics.tools.drawGrid(g2, 8, 8);

        g2.setColor(new Color(255, 0, 241));
        g2.fill(rect);

    }

    class RectRunnable implements Runnable {

        private Thread runner;

        public RectRunnable() {
            initThread();
        }

        private void initThread() {
            runner = new Thread(this);
            runner.start();
        }

        @Override
        public void run() {
            double y = rect.getY();
            for (double c = 0; c <= 50; c++) {
                rect.setRect(0, y + c, 50, 50);
                repaint();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.exit(1);
                }

            }
        }

    }

    class HitTestAdapter extends MouseAdapter {

        private RectRunnable rectAnimator;

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            if (rect.contains(x, y)) {
                rectAnimator = new RectRunnable();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

}
