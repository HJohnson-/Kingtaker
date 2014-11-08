package networking;

import junit.framework.TestCase;
import networking.NetworkingCodes.ClientCommandCode;
import networking.NetworkingCodes.ResponseCode;

public class ServerMessageSenderTest extends TestCase {

    //Integration testing - the server and database needs to be online.

    public void testSendMessage1() throws Exception {
        ServerMessageSender sms = new ServerMessageSender();
        String response = sms.sendMessage(ClientCommandCode.AUTHENTICATE_USER + "," +
                "***TESTUSER***" + "," + 0, true);
        assertEquals(ResponseCode.OK + ",1000",response);
    }
    public void testSendMessage2() throws Exception {
        ServerMessageSender sms = new ServerMessageSender();
        String response = sms.sendMessage(ClientCommandCode.AUTHENTICATE_USER + "," +
                "***NOTATESTUSER***" + "," + 0, true);
        assertEquals(ResponseCode.BAD_LOGIN + "", response);
    }
    public void testSendMessageNoResponse() throws Exception {
        ServerMessageSender sms = new ServerMessageSender();
        String response = sms.sendMessage(ClientCommandCode.REMOVE_GAME + "", true);
        assertNull(response);
    }
}