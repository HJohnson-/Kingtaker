package networking;

/**
 * Created by jc4512 on 04/11/14.
 */
public class LocalUserAccount {
    private String username;
    private String password;
    private int rating;

    public LocalUserAccount(String username, String password) {
        this.username = username;
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
}
