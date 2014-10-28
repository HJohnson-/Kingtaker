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

    /**
     * Sets up the chess frame with default parameters.
     */
    public ChessFrame() {
        initUI();
    }

    /**
     * Sets up all the parameters of the ChessFrame.
     * @param title What to display in the title bar.
     * @param width The width of the window.
     * @param height The height of the window.
     * @param panel The panel of the chess variant, which is drawn into the window.
     */
    public ChessFrame(String title, int width, int height, ChessPanel panel) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.panel = panel;
        initUI();
    }

    /**
     * Part of initialising a JPanel, this function sets up the window.
     */
    protected void initUI() {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(panel);

        setSize(width, height);
        setLocationRelativeTo(null);
    }

}
