package forms;

import BasicChess.BasicChess;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

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
    private JTable tblLobby;

    private JFrame parentFrame;

    private final String BTN_USERNAME_SUGGESTION_TEXT = "username";
    private final String BTN_PASSWORD_SUGGESTION_TEXT = "password";

    public frmLobby(final JFrame parentFrame) {
        this.parentFrame = parentFrame;

        final JFrame frame = new JFrame("frmLobby");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        panel.grabFocus();

        //Called when the lobby form is closed - reopens last form if hidden
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                //TODO: remove any open games

                parentFrame.setVisible(true);
            }
        });
        txtUsername.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtUsername.setText("");
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (!isAllowableUsername(txtUsername.getText())) {
                    txtUsername.setText(BTN_USERNAME_SUGGESTION_TEXT);
                }
            }
        });
        txtPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtPassword.setText("");
            }
            public void focusLost(FocusEvent e) {
                if (!isAllowableUsername(txtUsername.getText())) {
                    txtPassword.setText(BTN_PASSWORD_SUGGESTION_TEXT);
                }
            }
        });

    }

    //First tier
    private boolean isAllowableUsername(String username) {
        return username.length() > 3 && !username.equals(BTN_USERNAME_SUGGESTION_TEXT);
    }
}
