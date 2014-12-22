package forms;

import networking.NetworkingCodes.ResponseCode;

import javax.swing.*;

//This class is used to display information message boxes, given a parent form.
//All error messages are stored in this class
public class MessageBoxAlert {
    private String MSGBOX_TITLE = "KingTaker";

    private final String UNSPECIFIED_ERROR_MSG = "An unspecified error occurred";

    private JFrame parent;

    public MessageBoxAlert(JFrame parent) {
        this.parent = parent;
    }

    public MessageBoxAlert() {
        this.parent = null;
    }

    //Opens a small GUI form with a single OK button, title "KingTaker",
    // with specified message. 0-length messages are replaced with an.
    // 'unspecified error' message.
    private void showMessage(String msg) {
        if (msg != null && msg.length() > 0) {
            JOptionPane.showMessageDialog(parent, msg,
                    MSGBOX_TITLE, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, UNSPECIFIED_ERROR_MSG,
                    MSGBOX_TITLE, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void showUserLoginResponse(int responseCode) {
        String msg = "";
        switch (responseCode) {
            case (ResponseCode.BAD_LOGIN) :
                msg = "The username or password you entered is incorrect";
                break;
            case (ResponseCode.REFUSED) :
                msg = "Your account is currently suspended for " +
                        "breaking the terms of service";
                break;
        }
        showMessage(msg);
    }

    public void showUserRegisterResponse(int responseCode) {
        String msg = "";
        switch (responseCode) {
            case (ResponseCode.BAD_LOGIN) :
                msg = "You cannot register this username as it already exists";
                break;
            case (ResponseCode.INVALID) :
                msg = "You cannot register this username as it contains" +
                        " illegal characters or is of unacceptable length";
                break;
        }
        showMessage(msg);
    }

    public void showInvalidLoginDetails() {
        showMessage("Your username must be 3-20 alphanumeric characters\n" +
                    "Your password must be 6 characters or longer");
    }

    public void showGameJoinResponse(int responseCode) {
        String msg = "";
        switch (responseCode) {
            case (ResponseCode.REFUSED) :
                msg = "The other player refused your request to join their game";
                break;
            case (ResponseCode.INVALID) :
                msg = "This game is now full or has been removed";
                break;
            case (ResponseCode.EMPTY) :
                msg = "The other player is no longer online";
                break;
            case (ResponseCode.BAD_LOGIN) :
                msg = "You can't join your own game";
                break;
        }
        showMessage(msg);
    }

    public void showDisconnectedOpponent(String opponent) {
        String msg = opponent + " has disconnected.\n" +
                "Close the game window or continue the game with the AI.\n" +
                "This will not affect your rating.";
        showMessage(msg);
    }
}
