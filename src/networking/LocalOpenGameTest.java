package networking;

import BasicChess.BasicChess;
import junit.framework.TestCase;
import networking.NetworkingCodes.ClientCommandCode;

public class LocalOpenGameTest extends TestCase {
    private final String testPassword = "123 abc XYZ !$#";

    private String getTestUsername() {
        return "test" + System.currentTimeMillis();
    }

    public void testHostAndDestroyPositive() throws Exception {
        String uniqueUserName = getTestUsername();
        LocalUserAccount lua = new LocalUserAccount(uniqueUserName, testPassword);
        lua.authenticate(ClientCommandCode.REGISTER_ACCOUNT);

        LocalOpenGame log = new LocalOpenGame(new BasicChess());
        log.host();

        ServerMessageSender sms = new ServerMessageSender();
        String lobby1 = sms.sendMessage("0", true);
        assertTrue(lobby1.contains(uniqueUserName));

        log.destroy();
        String lobby2 = sms.sendMessage("0", true);
        assertTrue(!lobby2.contains(uniqueUserName));
    }
}