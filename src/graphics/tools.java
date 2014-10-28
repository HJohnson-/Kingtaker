package graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rp1012 on 15/10/14.
 */
public class tools {

    //These are all the colours used by the program. The idea is that by declaring them as variables it will be easier
    //to implement some sort of settings menu.
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color BOARD_BLACK = new Color(140, 83, 56);
    public static final Color BOARD_WHITE = new Color(255, 215, 142);
    public static final Color CUR_PIECE = new Color(143, 198, 222);
    public static final Color CUR_MOVES = new Color(253, 8, 0);
    public static final Color TEXT = new Color(93, 195, 101);

    public static final int CELL_WIDTH = 50;
    public static final int CELL_HEIGHT = 50;
    public static Map<String, BufferedImage> imageMap = new HashMap<String, BufferedImage>();

    /**
     * This will draw a quadrilateral grid onto the screen, of the given size.
     * @param g2 This is the graphics object to draw the grid onto.
     * @param num_rows The number of rows to draw.
     * @param num_cols The number of columns to draw.
     */
    public static void drawQuadGrid(Graphics2D g2, int num_rows, int num_cols) {
        g2.setColor(BOARD_BLACK);
        for (int x = 0; x < num_rows * CELL_WIDTH; x += CELL_WIDTH * 2) {
            for (int y = 0; y < num_cols * CELL_HEIGHT; y += CELL_HEIGHT * 2) {
                g2.fillRect(x, y, CELL_WIDTH, CELL_HEIGHT);
                g2.fillRect(x + CELL_WIDTH, y + CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
            }
        }

        g2.setColor(BOARD_WHITE);
        for (int x = 0; x < num_rows * CELL_WIDTH; x += CELL_WIDTH * 2) {
            for (int y = 0; y < num_cols * CELL_HEIGHT; y += CELL_WIDTH * 2) {
                g2.fillRect(x + CELL_WIDTH, y, CELL_WIDTH, CELL_HEIGHT);
                g2.fillRect(x, y + CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
            }
        }
    }

    /**
     * This is a function to abstract the process of first creating the screen.
     * @param frame This is a frame object associated with the specific variant.
     */
    public static void create(final ChessFrame frame) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    /**
     * This will load all of the images specified into the imageMap, used to draw pieces later.
     * @param pieces An array of all the pieces which need to be loaded.
     */
    public static void loadPieces(String[] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            try {
                imageMap.put(pieces[i], ImageIO.read(new File("media/" + pieces[i] + ".png")));
                imageMap.put(pieces[i] + "Black", ImageIO.read(new File("media/" + pieces[i] + "Black.png")));
            } catch (IOException e) {
                System.out.println(e);
                System.exit(2);
            }
        }
    }

}
