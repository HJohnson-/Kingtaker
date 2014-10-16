package graphics;

import javax.swing.*;
import java.awt.*;

/**
 * Created by rp1012 on 15/10/14.
 */
public abstract class ChessPanel extends JPanel {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    protected abstract void doDrawing(Graphics g);

}
