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
    private static final String BACKDROP_PIECE_CHARACTERS = "♚♛♜♝♞♟";

    private JPanel panel1;
    private JButton btnSinglePlayer;
    private JButton btnLocalMP;
    private JButton btnOnlineMP;
    private JButton btnExit;
    private JLabel lblTitle;
    private JPanel panButtons;
    private Timer timer;

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
                timer.start();
                beginLocalSP();
            }
        });
        btnLocalMP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                timer.start();
                beginLocalMP();
            }
        });
        btnOnlineMP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                timer.start();
                showLobby();
            }
        });
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (panel1.getWidth()!=0) {
                    drawBackdrop();
                    timer.stop();
                }

            }
        });
        timer.start();
        lblTitle.setText(" ");
    }

    //Called when panel is first available - not on form initialisation
    private void drawBackdrop() {
        Random r = new Random();
        BufferedImage panelImage = new BufferedImage(panel1.getWidth(),
                panel1.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics gr = panelImage.getGraphics();
        gr.setFont(new Font("", Font.PLAIN, 36));

        //  Draws pieces in backdrop
        for (int i = 0; i < BACKDROP_PIECE_COUNT; i++) {
            String piece = BACKDROP_PIECE_CHARACTERS.charAt(
                    r.nextInt(BACKDROP_PIECE_CHARACTERS.length())) + "";
            gr.setColor(new Color(r.nextInt()));
            gr.drawString(piece, r.nextInt(frame.getWidth()) - 20,
                    r.nextInt(frame.getHeight()));
        }

        panel1.getGraphics().drawImage(panelImage, 0, 0, null);

        //Draw KingTaker title graphic
        try {
            BufferedImage titleImage = ImageIO.read(new File("media/title.png"));
            panel1.getGraphics().drawImage(titleImage, 0, 0,
                    panel1.getWidth(), 106, null);
        } catch (Exception e) {
        }

        //Bring buttons to the front (otherwise they'd be invisible
        //behind the graphics drawn.
        btnExit.updateUI();
        btnLocalMP.updateUI();
        btnOnlineMP.updateUI();
        btnSinglePlayer.updateUI();
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
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

}
