package variants.Hnefatafl;

import javax.swing.*;
import java.awt.*;

/**
 * Created by crix9 on 21/11/2014.
 */
public class HnefataflPanel extends JPanel{

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
}
