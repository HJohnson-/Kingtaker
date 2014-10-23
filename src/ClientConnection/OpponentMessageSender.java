package ClientConnection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by jc4512 on 23/10/14.
 */
public class OpponentMessageSender {
    private final int PORT = 4445;
    private Socket socket;

    public OpponentMessageSender(InetAddress ip) {
        try {
            this.socket = new Socket(ip, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {

    }
}
