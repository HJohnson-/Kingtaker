package graphics;

import java.awt.*;

/**
 * Created by rp1012 on 15/10/14.
 */
public class tools {

    public static final int CELL_WIDTH = 50;
    public static final int CELL_HEIGHT = 50;

    public static void drawGrid(Graphics2D g2, int num_rows, int num_cols) {
        g2.setColor(new Color(255, 255, 255));
        for (int x = 0; x < num_rows * CELL_WIDTH; x += CELL_WIDTH * 2) {
            for (int y = 0; y < num_cols * CELL_HEIGHT; y += CELL_HEIGHT * 2) {
                g2.fillRect(x, y, CELL_WIDTH, CELL_HEIGHT);
                g2.fillRect(x + CELL_WIDTH, y + CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
            }
        }

        g2.setColor(new Color(0, 0, 0));
        for (int x = 0; x < num_rows * CELL_WIDTH; x += CELL_WIDTH * 2) {
            for (int y = 0; y < num_cols * CELL_HEIGHT; y += CELL_WIDTH * 2) {
                g2.fillRect(x + CELL_WIDTH, y, CELL_WIDTH, CELL_HEIGHT);
                g2.fillRect(x, y + CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
            }
        }
    }

}
