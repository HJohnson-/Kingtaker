package forms;

import networking.GameLobby;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        btnSinglePlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showVariantChooser();
            }
        });
        btnLocalMP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showVariantChooser();
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

    private void showVariantChooser() {
        frmVariantChooser frm = new frmVariantChooser(frame);
        frame.setVisible(false);
    }

    private void showLobby() {
        GameLobby lobby = new GameLobby(frame);
        frame.setVisible(false);
    }

    public static void main(String[] args) {
        frame.setContentPane(new frmMainMenu().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
