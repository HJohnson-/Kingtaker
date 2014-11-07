package forms;

import main.ChessVariant;
import main.ChessVariantManager;
import networking.GameLobby;
import networking.LocalUserAccount;
import networking.NetworkingCodes.ResponseCode;
import networking.RemoteOpenGame;

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

    public static void hideInstance() {
        instance.frame.setVisible(false);
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
                    frmVariantChooser.showInstance();
                }
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!checkUsernameAndPasswordFields()) {
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
                if (!checkUsernameAndPasswordFields()) {
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
        tblLobby.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String hostUsername = tblLobbyModel.getValueAt(tblLobby.getSelectedRow(), 1).toString();
                    int response = gameLobby.attemptJoinGameByUsername(hostUsername);
                    if (response != ResponseCode.OK) {
                        messageBoxAlert.showGameJoinResponse(response);

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
    private boolean isAllowablePasswordField(String password) {
        return password.length() >= 3 && !password.equals(TXT_PASSWORD_SUGGESTION_TEXT);
    }

    // Called before making a new LocalUserAccount and authenticating.
    private boolean checkUsernameAndPasswordFields() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        return isAllowableUsernameField(username) &&
               isAllowablePasswordField(password) &&
               LocalUserAccount.checkAcceptableUsernameAndPassword(
                                                    username, password);
    }

    // Clear table rows and refill. Set connection label accordingly.
    // Enable user to create/remove their open game with the JButton.
    public void setOpenGamesAndServerStatus(List<RemoteOpenGame> list, boolean isConnected, LocalUserAccount user) {
        tblLobbyModel.getDataVector().removeAllElements();
        for (RemoteOpenGame game : list) {
            ChessVariant variant = ChessVariantManager.getInstance().getVariantByID(game.variantId);
            if (variant != null) {
                //variant is installed and can be joined.
                tblLobbyModel.addRow(new Object[]{variant.getName(), game.hostUsername, game.hostRating});
            }
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
