package forms;

import main.GameMode;
import networking.GameLobby;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

/**
 * Created by jc4512 on 15/10/14.
 */
public class frmMainMenu {
    private JPanel panel1;
    private JButton btnSinglePlayer;
    private JButton btnLocalMP;
    private JButton btnOnlineMP;
    private JButton btnExit;
    private JLabel lblTitle;
    private JPanel panButtons;
    private Timer timer;

    private final String LBLTITLE_TEXT = "KingTaker";

    private static JFrame frame = new JFrame("KingTaker");

    //Form initialisation, specifying actions for form events
    public frmMainMenu() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                super.windowActivated(e);
                toggleButtonsEnabled(!(frmVariantChooser.isVisible() || GameLobby.isOpen()));
            }
        });
        
        btnSinglePlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                beginLocalSP();
            }
        });
        btnLocalMP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                beginLocalMP();
            }
        });
        btnOnlineMP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showLobby();
            }
        });
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        timer = new Timer(1, new ActionListener() {
            int iteration = 0;
            Graphics gr;
            String characters = "♚♛♜♝♞♟";
            Color color1 = new Color(181, 158, 122);
            Color color2 = new Color(0, 0, 0);
            Font pawnFont = new Font("", Font.PLAIN, 36);
            Font titleFont1 = new Font("", Font.BOLD, 78);
            Font titleFont2 = new Font("", Font.BOLD, 75);

            Random r = new Random();

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (frame.getWidth() > 0) {
                    gr = panel1.getGraphics();

                    String piece = characters.charAt(r.nextInt(characters.length())) + "";
                    gr.setFont(pawnFont);
                    gr.setColor(r.nextBoolean() ? color1 : color2);
                    gr.drawString(piece, r.nextInt(frame.getWidth()), r.nextInt(frame.getHeight()));

                    gr.setFont(titleFont1);
                    gr.setColor(Color.DARK_GRAY);
                    drawCenteredString(LBLTITLE_TEXT, frame.getWidth(), 80, gr);
                    gr.setFont(titleFont2);
                    gr.setColor(Color.WHITE);
                    drawCenteredString(LBLTITLE_TEXT, frame.getWidth(), 80, gr);

                    btnSinglePlayer.updateUI();
                    btnLocalMP.updateUI();
                    btnOnlineMP.updateUI();
                    btnExit.updateUI();
                }
            }

            public void drawCenteredString(String s, int w, int h, Graphics g) {
                FontMetrics fm = g.getFontMetrics();
                int x = (w - fm.stringWidth(s)) / 2;
                int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
                g.drawString(s, x, y);
            }
        });
        timer.start();

        lblTitle.setText(" ");
    }

    private void showLobby() {
        toggleButtonsEnabled(false);
        GameMode.currentGameMode = GameMode.MULTIPLAYER_ONLINE;
        GameLobby.getInstance().open();
    }

    private void beginLocalMP() {
        toggleButtonsEnabled(false);
        GameMode.currentGameMode = GameMode.MULTIPLAYER_LOCAL;
        frmVariantChooser.showInstance();
    }

    private void beginLocalSP() {
        toggleButtonsEnabled(false);
        GameMode.currentGameMode = GameMode.SINGLE_PLAYER;
        frmVariantChooser.showInstance();
    }

    private void toggleButtonsEnabled(boolean enabled) {
        btnExit.setEnabled(enabled);
        btnLocalMP.setEnabled(enabled);
        btnOnlineMP.setEnabled(enabled);
        btnSinglePlayer.setEnabled(enabled);
    }

    public static void main(String[] args) {
        frame.setContentPane(new frmMainMenu().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

}
