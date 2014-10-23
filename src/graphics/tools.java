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

    public static final int CELL_WIDTH = 50;
    public static final int CELL_HEIGHT = 50;
    public static Map<String, BufferedImage> imageMap = new HashMap<String, BufferedImage>();

    public static void drawQuadGrid(Graphics2D g2, int num_rows, int num_cols) {
        g2.setColor(new Color(140, 83, 56));
        for (int x = 0; x < num_rows * CELL_WIDTH; x += CELL_WIDTH * 2) {
            for (int y = 0; y < num_cols * CELL_HEIGHT; y += CELL_HEIGHT * 2) {
                g2.fillRect(x, y, CELL_WIDTH, CELL_HEIGHT);
                g2.fillRect(x + CELL_WIDTH, y + CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
            }
        }

        g2.setColor(new Color(255, 215, 142));
        for (int x = 0; x < num_rows * CELL_WIDTH; x += CELL_WIDTH * 2) {
            for (int y = 0; y < num_cols * CELL_HEIGHT; y += CELL_WIDTH * 2) {
                g2.fillRect(x + CELL_WIDTH, y, CELL_WIDTH, CELL_HEIGHT);
                g2.fillRect(x, y + CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
            }
        }
    }

    public static void create(final ChessFrame frame) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

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
