package graphics;

import javax.swing.*;
import java.awt.*;

/**
 * A basic JFrame implementation, which handles displaying the window on the screen.
 */
public abstract class ChessFrame extends JFrame {

    protected String title;
    protected int width, height;
    protected JPanel panel;
    public boolean fullscreen = false;

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
//        this.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        initUI();
    }


	public ChessFrame(String title, int width, int height, JPanel panel) {
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(panel);



        if (fullscreen) {
            setExtendedState(Frame.MAXIMIZED_BOTH);
            setUndecorated(true);
        } else {
            Dimension minimumDimensions = new Dimension(width, height);
            setSize(minimumDimensions);
            setMinimumSize(minimumDimensions);
        }
        setLocationRelativeTo(null);
    }


}
