package forms;

import networking.GameLobby;
import networking.RemoteGame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.util.List;

/**
 * Created by jc4512 on 28/10/14.
 */
public class frmLobby {

    private JPanel panel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblIsConnected;
    private JLabel lblUsernameRating;
    private JTable tblLobby;
    private JButton btnLogin;
    private JButton btnRegister;
    private JButton btnCreateGame;

    private JFrame parentFrame;

    private final String TXT_USERNAME_SUGGESTION_TEXT = "username";
    private final String TXT_PASSWORD_SUGGESTION_TEXT = "password";

    private DefaultTableModel tblLobbyModel;

    public frmLobby(final JFrame parentFrame, final GameLobby gameLobby) {
        this.parentFrame = parentFrame;

        final JFrame frame = new JFrame("frmLobby");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(parentFrame.getLocation());
        panel.grabFocus();

        //Called when the lobby form is closed - reopens last form if hidden
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                parentFrame.setVisible(true);
                gameLobby.close();
            }
        });
        txtUsername.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtUsername.setText("");
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (!isAllowableUsernameField(txtUsername.getText())) {
                    txtUsername.setText(TXT_USERNAME_SUGGESTION_TEXT);
                }
            }
        });
        txtPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtPassword.setText("");
            }
            public void focusLost(FocusEvent e) {
                if (!isAllowableUsernameField(txtUsername.getText())) {
                    txtPassword.setText(TXT_PASSWORD_SUGGESTION_TEXT);
                }
            }
        });

        btnCreateGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (gameLobby.userLoggedIn()) {
                    //TODO Open frmVariationPicker as dialog
                    //somehow this has to talk back to gameLobby.createGame()
                }
            }
        });
    }

    private boolean isAllowableUsernameField(String username) {
        return username.length() > 3 && !username.equals(TXT_USERNAME_SUGGESTION_TEXT);
    }

    // Clear rows and refill. This method is only called when the list actually changes.
    public void displayOpenGames(List<RemoteGame> list) {
        tblLobbyModel.getDataVector().removeAllElements();
        for (RemoteGame game : list) {
            tblLobbyModel.addRow(new Object[]{game.variantId, game.hostUsername, game.hostRating});
        }
    }

    private void createUIComponents() {
        txtUsername = new JTextField();
        txtUsername.setText(TXT_USERNAME_SUGGESTION_TEXT);
        txtPassword = new JPasswordField();
        txtPassword.setText(TXT_PASSWORD_SUGGESTION_TEXT);


        tblLobbyModel = new DefaultTableModel(null, new String[]{"Variant", "User", "ELO Rating"});
        tblLobby = new JTable(tblLobbyModel);
    }


}
