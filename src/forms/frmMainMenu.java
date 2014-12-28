package forms;

import main.GameMode;
import networking.GameLobby;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

/**
 * Created by jc4512 on 15/10/14.
 */
public class frmMainMenu {
    private static final int BACKDROP_PIECE_COUNT = 3000;
    private static final String BACKDROP_PIECE_CHARACTERS = "♚♛♜♝♞♟"; //"♖♘♗♕♔";
    public static final String TITLE_PICTURE_PATH = "media/title.png";

    private JPanel panMain;
    private JButton btnSinglePlayer;
    private JButton btnLocalMP;
    private JButton btnOnlineMP;
    private JButton btnExit;
    private JLabel lblTitle;
    private JPanel panButtons;

    private BufferedImage backdrop;

    private static JFrame frame = new JFrame("KingTaker");

    //Form initialisation, specifying actions for form events
    public frmMainMenu() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                super.windowActivated(e);
                toggleButtonsEnabled(!frmVariantChooser.isVisible() );
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
        lblTitle.setText(" ");
    }

    //Called when panel is first available - not on form initialisation
    private void drawBackdrop(Graphics g) {
        if (backdrop == null) {
            Random r = new Random();
            backdrop = new BufferedImage(frame.getWidth(),
                    frame.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics gr = backdrop.getGraphics();
            gr.setFont(new Font("", Font.PLAIN, 36));

            //  Draws pieces in backdrop
            for (int i = 0; i < BACKDROP_PIECE_COUNT; i++) {
                String piece = BACKDROP_PIECE_CHARACTERS.charAt(
                        r.nextInt(BACKDROP_PIECE_CHARACTERS.length())) + "";
                gr.setColor(new Color(r.nextInt()));
                gr.drawString(piece, r.nextInt(frame.getWidth()) - 20,
                        r.nextInt(frame.getHeight()));

            }

            //Draw KingTaker title graphic
            try {
                BufferedImage titleImage = ImageIO.read(new File(TITLE_PICTURE_PATH));
                gr.drawImage(titleImage, 0, 0,
                        panMain.getWidth(), 106, null);
            } catch (Exception e) {
            }
        }

        g.drawImage(backdrop, 0, 0, null);

    }

    private void showLobby() {
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
        frame.setContentPane(new frmMainMenu().panMain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void createUIComponents() {
        panMain = new BackdropPanel();
    }

    private class BackdropPanel extends JPanel {
        @Override
        public void paint(Graphics g) {
            drawBackdrop(g);

            btnExit.repaint();
            btnLocalMP.repaint();
            btnOnlineMP.repaint();
            btnSinglePlayer.repaint();
        }
    }
}
