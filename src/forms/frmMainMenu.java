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

        timer = new Timer(10, new ActionListener() {
            int iteration = 0;
            Graphics gr;
            String characters = "♚♛♜♝♞♟";
            Font pawnFont = new Font("", Font.PLAIN, 36);
            Random r = new Random();

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (frame.getWidth() > 0) {
                    gr = panel1.getGraphics();
                    String piece = characters.charAt(r.nextInt(characters.length())) + "";
                    gr.setFont(pawnFont);
                    gr.setColor(r.nextBoolean() ? Color.BLACK : Color.WHITE);
                    gr.drawString(piece, r.nextInt(frame.getWidth()), r.nextInt(frame.getHeight()));

                    lblTitle.updateUI();
                }
            }
        });
        timer.start();
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
