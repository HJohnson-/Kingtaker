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
 * A collection of various static graphics tools, which are re-used in multiple scenarios.
 */
public class tools {

    //These are all the colours used by the program. The idea is that by declaring them as variables it will be easier
    //to implement some sort of settings menu.
    public static final Color BOARD_BLACK = new Color(140, 83, 56);
    public static final Color BOARD_WHITE = new Color(255, 215, 142);
    public static final Color CUR_PIECE = new Color(143, 198, 222);
    public static final Color CUR_MOVES = new Color(8, 253, 0);
    public static final Color CHECK = new Color(255, 0, 0);

    public static Map<String, BufferedImage> imageMap = new HashMap<String, BufferedImage>();

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
     * This will load the specified image into the imageMap, used to draw pieces later.
     * @param name The piece to be loaded into the image map.
     */
    public static void loadPiece(String name) {
        try {
            imageMap.put(name, ImageIO.read(new File("media/" + name + ".png")));
            imageMap.put(name + "Black", ImageIO.read(new File("media/" + name + "Black.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

}
