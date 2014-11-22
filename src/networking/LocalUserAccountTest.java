package networking;

import junit.framework.TestCase;
import networking.NetworkingCodes.ClientCommandCode;
import networking.NetworkingCodes.ResponseCode;

public class LocalUserAccountTest extends TestCase {
    private final String testPassword = "123 abc XYZ !$#";
    private final int defaultRating = 1000; //known

    private String getTestUsername() {
        return "test" + System.currentTimeMillis();
    }

    public void testAuthenticateRegisterPositive() throws Exception {
        LocalUserAccount lua = new LocalUserAccount(getTestUsername(), testPassword);
        int response = lua.authenticate(ClientCommandCode.REGISTER_ACCOUNT);
        assertEquals(ResponseCode.OK, response);
        assertEquals(defaultRating, lua.getRating());
        assertTrue(lua.isLoggedIn());
    }

    public void testAuthenticateRegisterDouble() throws Exception {
        String uniqueUsername = getTestUsername();
        LocalUserAccount lua = new LocalUserAccount(uniqueUsername, testPassword);
        int response = lua.authenticate(ClientCommandCode.REGISTER_ACCOUNT);

        LocalUserAccount lua2 = new LocalUserAccount(uniqueUsername, testPassword);
        int response2 = lua2.authenticate(ClientCommandCode.REGISTER_ACCOUNT);
        assertEquals(ResponseCode.BAD_LOGIN, response2);
        assertFalse(lua2.isLoggedIn());
    }

    public void testAuthenticateLoginPositive() throws Exception {
        String uniqueUsername = getTestUsername();
        LocalUserAccount lua = new LocalUserAccount(uniqueUsername, testPassword);
        int response = lua.authenticate(ClientCommandCode.REGISTER_ACCOUNT);

        LocalUserAccount lua2 = new LocalUserAccount(uniqueUsername, testPassword);
        int response2 = lua2.authenticate(ClientCommandCode.AUTHENTICATE_USER);
        assertEquals(ResponseCode.OK, response2);
        assertEquals(defaultRating, lua2.getRating());
        assertTrue(lua2.isLoggedIn());
    }

    public void testAuthenticateLoginBadPassword() throws Exception {
        String uniqueUsername = getTestUsername();
        LocalUserAccount lua = new LocalUserAccount(uniqueUsername, testPassword);
        int response = lua.authenticate(ClientCommandCode.REGISTER_ACCOUNT);

        LocalUserAccount lua2 = new LocalUserAccount(uniqueUsername, testPassword + ":(");
        int response2 = lua2.authenticate(ClientCommandCode.AUTHENTICATE_USER);
        assertEquals(ResponseCode.BAD_LOGIN, response2);
        assertFalse(lua2.isLoggedIn());
    }

    public void testAuthenticateLoginBadUsername() throws Exception {
        String uniqueUsername = getTestUsername(); //not registered
        LocalUserAccount lua = new LocalUserAccount(uniqueUsername, testPassword);
        int response = lua.authenticate(ClientCommandCode.AUTHENTICATE_USER);
        assertEquals(ResponseCode.BAD_LOGIN, response);
        assertFalse(lua.isLoggedIn());
    }

    public void testCheckAcceptableUsernameAndPasswordUsernameTooLong() throws Exception {
        assertFalse(LocalUserAccount.checkAcceptableUsernameAndPassword("012345678901234567890", testPassword));
    }

    public void testCheckAcceptableUsernameAndPasswordUsernameTooShort() throws Exception {
        assertFalse(LocalUserAccount.checkAcceptableUsernameAndPassword(null, testPassword));
        assertFalse(LocalUserAccount.checkAcceptableUsernameAndPassword("", testPassword));
        assertFalse(LocalUserAccount.checkAcceptableUsernameAndPassword("1", testPassword));
        assertFalse(LocalUserAccount.checkAcceptableUsernameAndPassword("22", testPassword));
    }

    public void testCheckAcceptableUsernameAndPasswordUsernameBadCharacters() throws Exception {
        char[] badChars = new char[]{'%','!','£','@','-','#', ' ', '\'', ',' ,'\n','\t'};

        for (int i = 0; i < badChars.length; i++) {
            assertFalse(LocalUserAccount.checkAcceptableUsernameAndPassword(
                    "aaa" + badChars[i], testPassword));
        }
    }

    public void testCheckAcceptableUsernameAndPasswordPasswordTooShort() throws Exception {
        assertFalse(LocalUserAccount.checkAcceptableUsernameAndPassword(getTestUsername(), null));
        assertFalse(LocalUserAccount.checkAcceptableUsernameAndPassword(getTestUsername(), ""));
        assertFalse(LocalUserAccount.checkAcceptableUsernameAndPassword(getTestUsername(), "1"));
        assertFalse(LocalUserAccount.checkAcceptableUsernameAndPassword(getTestUsername(), "22"));
    }

    public void testCheckAcceptableUsernameAndPasswordPositive() throws Exception {
        assertTrue(LocalUserAccount.checkAcceptableUsernameAndPassword(getTestUsername(), testPassword));
    }
}