package variants.Hnefatafl;

import graphics.ChessFrame;
import main.Board;

/**
 * Created by crix9 on 21/11/2014.
 */
public class HnefataflFrame extends ChessFrame {
	public HnefataflFrame(String title, int width, int height, Board board) {
		super(title, width, height, new HnefataflPanel());
	}
}
