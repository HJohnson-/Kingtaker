package graphics;

import BasicChess.BasicChessPanel;

import javax.swing.*;

/**
 * Created by rp1012 on 16/10/14.
 */
public abstract class ChessFrame extends JFrame {

    protected String title = "Basic Chess";
    protected int width = 600, height = 600;
    protected ChessPanel panel = new BasicChessPanel(null);

    public ChessFrame() {
        initUI();
    }

    public ChessFrame(String title, int width, int height, ChessPanel panel) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.panel = panel;
        initUI();
    }

    protected void initUI() {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(panel);

        setSize(width, height);
        setLocationRelativeTo(null);
    }

}
