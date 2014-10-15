package graphics;

import javax.swing.*;
import java.awt.event.MouseAdapter;

/**
 * Created by rp1012 on 15/10/14.
 */
public abstract class ChessPanel extends JPanel {

    public ChessPanel() {
        initPanel();
    }

    private void initPanel() {
        this.addMouseListener(new HitTestListener());
    }

}

class HitTestListener extends MouseAdapter {

}
