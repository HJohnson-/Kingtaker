package forms;

import networking.NetworkingCodes.ResponseCode;

import javax.swing.*;

//This class is used to display information message boxes, given a parent form.
//All error messages are stored in this class
public class MessageBoxAlert {
    private String title = "KingTaker";
    private JFrame parent;

    public MessageBoxAlert(JFrame parent) {
        this.parent = parent;
    }

    public void showUserLoginResponse(int responseCode) {
        String msg = "";
        switch (responseCode) {
            case (ResponseCode.BAD_LOGIN) :
                msg = "The username or password you entered is incorrect";
                break;
            case (ResponseCode.REFUSED) :
                msg = "Your account is currently suspended for breaking the terms of service";
                break;
        }
        showMessage(msg);
    }

    public void showUserRegisterResponse(int responseCode) {
        String msg = "";
        switch (responseCode) {
            case (ResponseCode.REFUSED) :
                msg = "You cannot register this username as it already exists";
                break;
        }
        showMessage(msg);
    }

    //Opens a small GUI form with a single OK button, title "KingTaker",
    // with specified message. 0-length messages are disallowed.
    private void showMessage(String msg) {
        if (msg != null && msg.length() > 0) {
            JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
