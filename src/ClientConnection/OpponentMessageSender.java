package ClientConnection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by jc4512 on 23/10/14.
 */
public class OpponentMessageSender {
    private final int PORT = 4445;
    private final int TIMEOUT_MS = 10000;

    private InetAddress inetAddress;


    public OpponentMessageSender(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    //Attempts to send message to other client.
    //Returns true if successful, false if there were only timeouts or IO exceptions.
    public boolean sendMessage(String msg) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < TIMEOUT_MS) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(inetAddress, PORT), TIMEOUT_MS);

                DataOutputStream clientWriter = new DataOutputStream(socket.getOutputStream());
                clientWriter.writeBytes(msg);
                clientWriter.close();
                socket.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
