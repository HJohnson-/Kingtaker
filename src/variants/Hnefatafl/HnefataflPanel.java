package variants.Hnefatafl;

import graphics.ChessPanel;
import graphics.tools;
import main.Board;
import main.Location;
import main.PieceType;
import pieces.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.util.*;

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

			if (tools.imageMap.get(imgName) == null) System.err.println(imgName + " is null.");


			TexturePaint texture = new TexturePaint(tools.imageMap.get(imgName),
					new Rectangle(p.graphics.getX(), p.graphics.getY(), cellWidth, cellHeight));
			g2.setPaint(texture);
			g2.fillRect(p.graphics.getX(), p.graphics.getY(), cellWidth, cellHeight);
		}

		g2.setPaint(tools.CHECK);
		Stroke oldstroke = g2.getStroke();
		g2.setStroke(new BasicStroke(4));

		if (selectedPiece != null) {
			g2.setStroke(new BasicStroke(2));
			g2.setPaint(tools.CUR_PIECE);
			g2.drawRect(selectedPiece.graphics.getX(), selectedPiece.graphics.getY(),
					cellWidth, cellHeight);

			java.util.List<Location> moves = board.getController().movesForPiece(selectedPiece, true);
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

/*
	public HnefataflPanel() {

		setLayout(null);

		setBackground(new Color(85, 55, 29));

		BoardPanel board = new BoardPanel();

		add(board);
		add(board.newGameButton);
		add(board.abandonButton);
		add(board.message);


		//set positions and sizes
		board.setBounds(20, 20, 448, 448);
		board.newGameButton.setBounds(500, 60, 120, 30);
		board.abandonButton.setBounds(500, 120, 120, 30);
		board.message.setBounds(0, 520, 700, 30);
	}
	*/
}
