package networking;

import junit.framework.TestCase;

public class ServerMessageSenderTest extends TestCase {

    //Integration testing - the server and database needs to be online.

    public void testSendMessage1() throws Exception {
        ServerMessageSender sms = new ServerMessageSender();
        String response = sms.sendMessage(ClientCommandCode.AUTHENTICATE_USER + "," +
                "***TESTUSER***" + "," + 0, true);
        assertEquals(response, "0");
    }
    public void testSendMessage2() throws Exception {
        ServerMessageSender sms = new ServerMessageSender();
        String response = sms.sendMessage(ClientCommandCode.AUTHENTICATE_USER + "," +
                "***NOTATESTUSER***" + "," + 0, true);
        assertEquals(response, "4");
    }
    public void testSendMessageNoResponse() throws Exception {
        ServerMessageSender sms = new ServerMessageSender();
        String response = sms.sendMessage(ClientCommandCode.REMOVE_GAME + "", true);
        assertNull(response);
    }
}