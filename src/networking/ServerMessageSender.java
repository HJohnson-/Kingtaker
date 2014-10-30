package networking;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by daniel on 14/10/20.
 */


public class ServerMessageSender {

    private final String IP = "localhost";
    private final int PORT = 4444;
    private final int TIMEOUT_MS = 10000;
    private Socket socket = new Socket();

    //Send message to server and if flag is set, await a response.
    public String sendMessage(String msg, boolean waitForResponse) {
        long startTime = System.currentTimeMillis();
        String response = null;
        while (System.currentTimeMillis() - startTime < TIMEOUT_MS) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(InetAddress.getByName(IP), PORT), TIMEOUT_MS);

                DataOutputStream clientWriter = new DataOutputStream(socket.getOutputStream());
                clientWriter.writeBytes(msg + "\n");
                System.out.println("[localhost] sent to [" + socket.getInetAddress().getHostAddress() + "]: " + msg);

                if (waitForResponse) {
                    BufferedReader clientReader =
                            new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    response = clientReader.readLine();
                    clientReader.close();
                }

                clientWriter.close();
                socket.close();
                return response;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return response;
    }


}
