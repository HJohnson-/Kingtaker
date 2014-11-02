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

    private final JFrame frame = new JFrame("frmLobby");
    private JPanel panel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblIsConnected;
    private JLabel lblUsernameRating;
    private JTable tblLobby;
    private JButton btnLogin;
    private JButton btnRegister;
    private JButton btnCreateGame;

    private final String TXT_USERNAME_SUGGESTION_TEXT = "username";
    private final String TXT_PASSWORD_SUGGESTION_TEXT = "password";

    private DefaultTableModel tblLobbyModel;

    private static frmLobby instance;

    public static void showInstance(final GameLobby gameLobby) {
        if (instance == null) {
            instance = new frmLobby(gameLobby);
        } else {
            instance.displayForm();
        }
    }

    private frmLobby(final GameLobby gameLobby) {
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        displayForm();

        //Called when the lobby form is closed - reopens last form if hidden
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
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
                tblLobbyModel.addRow(new Object[]{"Capablanca","jc4512",2003});
            }
        });
    }

    // Makes the form visible and sets focus to the panel, so that no fields or
    // other UI elements are selected when first shown.
    private void displayForm() {
        frame.setVisible(true);
        panel.grabFocus();
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
        tblLobby = new JTable(tblLobbyModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
        tblLobby.setRowHeight(30);
    }


}
