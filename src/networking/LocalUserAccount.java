package networking;

import networking.NetworkingCodes.ClientCommandCode;
import networking.NetworkingCodes.ResponseCode;

/**
 * Created by jc4512 on 04/11/14.
 */
public class LocalUserAccount {
    private String username;
    private int passwordHash;
    private int rating;
    private boolean loggedIn;

    public LocalUserAccount(String username, String password) {
        this.username = username;
        this.passwordHash = (username + password).hashCode();
    }

    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }

    //authenticate builds and sends a command message to the server and
    // awaits either an OK with the user's rating, or an error code. If the
    // response is of an unexpected format, it will return an unexpected
    // error code, otherwise, whichever response code it received.
    public int authenticate(int clientCommandCode) {
        assert clientCommandCode == ClientCommandCode.REGISTER_ACCOUNT ||
                clientCommandCode == ClientCommandCode.AUTHENTICATE_USER;

        String msg = clientCommandCode + ClientCommandCode.DELIMINATOR +
                     username + ClientCommandCode.DELIMINATOR + passwordHash;

        ServerMessageSender sms = new ServerMessageSender();
        String response = sms.sendMessage(msg, true);

        if (response == null || !response.matches("\\d(" + ResponseCode.DEL + "\\d+)?")) {
            return ResponseCode.UNSPECIFIED_ERROR;
        }

        if (response.matches(ResponseCode.OK + ResponseCode.DEL + "\\d+")) {
            rating = Integer.parseInt(response.substring(2));
            return ResponseCode.OK;
        }

        return Integer.parseInt(response);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }


    private static final String USERNAME_REGEX = "(\\w){3,20}";
    private static final String PASSWORD_REGEX = "......+";
    public static boolean checkAcceptableUsernameAndPassword(String u, String pw) {
        return u.matches(USERNAME_REGEX) && pw.matches(PASSWORD_REGEX);
    }
}
