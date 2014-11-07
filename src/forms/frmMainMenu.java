package forms;

import main.GameMode;
import networking.GameLobby;

import javax.swing.*;
import java.awt.event.*;

/**
 * Created by jc4512 on 15/10/14.
 */
public class frmMainMenu {
    private JPanel panel1;
    private JButton btnSinglePlayer;
    private JButton btnLocalMP;
    private JButton btnOnlineMP;
    private JButton btnExit;

    private static JFrame frame = new JFrame("frmMainMenu");

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

    }

    private void showLobby() {
        toggleButtonsEnabled(false);
        frmVariantChooser.currentGameMode = GameMode.MULTIPLAYER_ONLINE;
        GameLobby.getInstance().open();
    }

    private void beginLocalMP() {
        toggleButtonsEnabled(false);
        frmVariantChooser.currentGameMode = GameMode.MULTIPLAYER_LOCAL;
        frmVariantChooser.showInstance();
    }

    private void beginLocalSP() {
        toggleButtonsEnabled(false);
        frmVariantChooser.currentGameMode = GameMode.SINGLE_PLAYER;
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
