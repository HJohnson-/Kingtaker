package variants.Hnefatafl;

import graphics.ChessPanel;
import graphics.GraphicsTools;
import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by crix9 on 21/11/2014.
 */
public class HnefataflPanel extends ChessPanel {

	public HnefataflPanel(Board board) {
		super(board);
	}

	@Override
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

        drawMovesForSelectedPiece(g2);
//		if (selectedPiece != null) {
//			g2.setStroke(new BasicStroke(2));
//			g2.setPaint(GraphicsTools.CUR_PIECE);
//			g2.drawRect(selectedPiece.graphics.getX(), selectedPiece.graphics.getY(),
//					cellWidth, cellHeight);
//
//			java.util.List<Location> moves = board.getController().movesForPiece(selectedPiece, false);
//			if (selectedPiece.isWhite() == board.getController().isWhitesTurn()) {
//				g2.setPaint(GraphicsTools.CUR_MOVES);
//			} else {
//				g2.setPaint(Color.RED.darker());
//			}
//			for (Location l : moves) {
//				g2.drawRect(l.getX() * cellWidth + offset.getX(), l.getY() * cellHeight + offset.getY(),
//						cellWidth, cellHeight);
//			}
//		}

		g2.setStroke(oldstroke);
	}

	private boolean isHostile(int row, int col) {
		return (row==0 && col==0) || (row==0 && col==10) ||
				(row==10 && col==0) || (row==10 && col==10) || row == 5 && col == 5;
	}

	@Override
	protected void drawGrid(Graphics2D g2) {
		g2.setColor(GraphicsTools.BOARD_BLACK);
		for (int x = 0; x < board.numCols(); x++) {
			for (int y = 0; y < board.numRows(); y++) {
				if(isHostile(y,x))
					g2.setColor(Color.DARK_GRAY);

				else if ((x % 2) == 0 && (y % 2) == 0) {
					g2.setColor(GraphicsTools.BOARD_BLACK);
				} else if ((x % 2) == 1 && (y % 2) == 1) {
					g2.setColor(GraphicsTools.BOARD_BLACK);
				} else {
					g2.setColor(GraphicsTools.BOARD_WHITE);
				}
				g2.fillRect((cellWidth * x) + offset.getX(), (cellHeight * y) + offset.getY(), cellWidth, cellHeight);
			}
		}

		g2.setColor(GraphicsTools.BOARD_WHITE);

	}
}
