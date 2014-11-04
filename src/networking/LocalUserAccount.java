package networking;

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

    public boolean attemptRegister() {

        return false;
    }

    public boolean attemptLogin() {

        return false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
