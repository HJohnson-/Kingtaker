package networking;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by jc4512 on 23/10/14.
 */
//TODO: make this extend MessageSender
public class OpponentMessageSender {
    private final int PORT = 4445;
    private final int TIMEOUT_MS = 10000;
    private final int RETRY_WAIT_MS = 500;

    private InetAddress inetAddress;


    public OpponentMessageSender(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    //Attempts to send message to other client.
    //Returns true if successful, false if there were only timeouts or IO exceptions.
    public String sendMessage(String msg, boolean waitForResponse) {
        long startTime = System.currentTimeMillis();
        String response = null;
        while (System.currentTimeMillis() - startTime < TIMEOUT_MS) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(inetAddress, PORT), TIMEOUT_MS);

                DataOutputStream clientWriter = new DataOutputStream(socket.getOutputStream());
                clientWriter.writeBytes(msg);
                System.out.println("[localhost] sent to [" + socket.getInetAddress().getHostAddress() + "]: " + msg);

                if (waitForResponse) {
                    BufferedReader clientReader =
                            new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    response = clientReader.readLine();
                    System.out.println("[" + socket.getInetAddress().getHostAddress() + "] response: " + response);
                    clientReader.close();
                }

                clientWriter.close();
                socket.close();
                return response;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                try {
                    Thread.sleep(RETRY_WAIT_MS);
                } catch (InterruptedException e1) {
                    return null;
                }
            }
        }
        return response;
    }
}
