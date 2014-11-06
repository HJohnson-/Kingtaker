package forms;

import networking.GameLobby;
import networking.LocalUserAccount;
import networking.NetworkingCodes.ResponseCode;
import networking.RemoteGame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    private JButton btnCreateRemoveGame;
    private MessageBoxAlert messageBoxAlert;

    private final String TXT_USERNAME_SUGGESTION_TEXT = "username";
    private final String TXT_PASSWORD_SUGGESTION_TEXT = "password";
    private final String LBL_ISCONNECTED_TEXT_NO = "NOT CONNECTED";
    private final String LBL_ISCONNECTED_TEXT_YES = "CONNECTED";
    private final Color LBL_ISCONNECTED_COLOR_NO = Color.red;
    private final Color LBL_ISCONNECTED_COLOR_YES = Color.green;
    private final String LBL_USERNAMERATING_TEXT_NO = "NOT LOGGED IN";
    private final String LBL_USERNAMERATING_TEXT_YES = "Welcome, %s (%d)";
    private final String BTN_CREATEGAME_TEXT = "CREATE GAME";
    private final String BTN_REMOVEGAME_TEXT = "REMOVE GAME";

    private DefaultTableModel tblLobbyModel;

    private static frmLobby instance;

    public static void showInstance(final GameLobby gameLobby) {
        if (instance == null) {
            instance = new frmLobby(gameLobby);
        } else {
            instance.displayForm();
        }
    }

    public static frmLobby getInstance() {
        return instance;
    }

    private frmLobby(final GameLobby gameLobby) {
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        messageBoxAlert = new MessageBoxAlert(frame);
        setControlsOnServerStatus(null, false);
        displayForm();

        lblIsConnected.setBackground(LBL_ISCONNECTED_COLOR_NO);

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

        btnCreateRemoveGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (gameLobby.getUser() != null && gameLobby.getUser().isLoggedIn()) {

                    //TODO Open frmVariationPicker as dialog
                    //somehow this has to talk back to gameLobby.createGame()

                }
                tblLobbyModel.addRow(new Object[]{"Capablanca","jc4512",2003});
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!LocalUserAccount.checkAcceptableUsernameAndPassword(
                        txtUsername.getText(), new String(txtPassword.getPassword()))) {
                    messageBoxAlert.showInvalidLoginDetails();
                } else {
                    int result = gameLobby.attemptLogin(txtUsername.getText(), txtPassword.getPassword());
                    if (result == ResponseCode.OK) {
                        setControlsOnServerStatus(gameLobby.getUser(), true);
                    } else {
                        messageBoxAlert.showUserLoginResponse(result);
                    }
                }
            }
        });
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!LocalUserAccount.checkAcceptableUsernameAndPassword(
                        txtUsername.getText(), new String(txtPassword.getPassword()))) {
                    messageBoxAlert.showInvalidLoginDetails();
                } else {
                    int result = gameLobby.attemptRegister(txtUsername.getText(), txtPassword.getPassword());
                    if (result == ResponseCode.OK) {
                        setControlsOnServerStatus(gameLobby.getUser(), true);
                    } else {
                        messageBoxAlert.showUserRegisterResponse(result);
                    }
                }
            }
        });
    }

    // Makes the form visible and sets focus to the panel, so that no fields or
    // other UI elements are selected when first shown.
    private void displayForm() {
        frame.setVisible(true);
        panel.grabFocus();
    }

    // Shows username and rating of current user logged in.
    // Deactivate log in and register buttons and fields.
    private void setControlsOnServerStatus(LocalUserAccount user, boolean isConnected) {
        boolean isLoggedIn = user != null && user.isLoggedIn();

        lblUsernameRating.setText(isLoggedIn ?
                String.format(LBL_USERNAMERATING_TEXT_YES, user.getUsername(), user.getRating()) :
                LBL_USERNAMERATING_TEXT_NO);
        txtUsername.setEnabled(isConnected && !isLoggedIn);
        txtPassword.setEnabled(isConnected && !isLoggedIn);
        btnLogin.setEnabled(isConnected && !isLoggedIn);
        btnRegister.setEnabled(isConnected && !isLoggedIn);
        btnCreateRemoveGame.setEnabled(isConnected && isLoggedIn);

        lblIsConnected.setText(isConnected ? LBL_ISCONNECTED_TEXT_YES : LBL_ISCONNECTED_TEXT_NO);
        lblIsConnected.setBackground(isConnected ? LBL_ISCONNECTED_COLOR_YES : LBL_ISCONNECTED_COLOR_NO);
    }

    // Used by textbox listeners to determine whether the user is in the process of logging in.
    private boolean isAllowableUsernameField(String username) {
        return username.length() >= 3 && !username.equals(TXT_USERNAME_SUGGESTION_TEXT);
    }

    // Clear table rows and refill. Set connection label accordingly.
    // Enable user to create/remove their open game with the JButton.
    public void setOpenGamesAndServerStatus(List<RemoteGame> list, boolean isConnected, LocalUserAccount user) {
        tblLobbyModel.getDataVector().removeAllElements();
        for (RemoteGame game : list) {
            tblLobbyModel.addRow(new Object[]{game.variantId, game.hostUsername, game.hostRating});
        }
        tblLobbyModel.fireTableDataChanged();

        setControlsOnServerStatus(user, isConnected);
    }


    // Called when form is instantiated to customise particular GUI controls.
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
        tblLobby.getTableHeader().setReorderingAllowed(false);

        lblIsConnected = new JLabel(LBL_ISCONNECTED_TEXT_NO);
        lblIsConnected.setOpaque(true);

        lblUsernameRating = new JLabel(LBL_USERNAMERATING_TEXT_NO);

        btnCreateRemoveGame = new JButton(BTN_CREATEGAME_TEXT);
    }


}
