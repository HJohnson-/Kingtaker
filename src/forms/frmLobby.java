package forms;

import BasicChess.BasicChess;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by jc4512 on 28/10/14.
 */
public class frmLobby {

    private JPanel panel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblUsernameRating;
    private JLabel lblIsConnected;

    private JFrame parentFrame;

    public frmLobby(final JFrame parentFrame) {
        this.parentFrame = parentFrame;

        final JFrame frame = new JFrame("frmLobby");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        //Called when the lobby form is closed - reopens last form if hidden
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                //TODO: remove any open games

                parentFrame.setVisible(true);
            }
        });
    }
}
