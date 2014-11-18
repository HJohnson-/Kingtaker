package networking;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerMessageSender implements IMessageSender {

    private final String IP_STRING = "line20.doc.ic.ac.uk";
    private final int PORT = 4444;
    private final int TIMEOUT_DEFAULT_MS = 10000;
    private final int RETRY_WAIT_MS = 500;

    //Send message to server and if flag is set, await a response.
    public String sendMessage(String msg, boolean waitForResponse) {
        return sendMessage(msg, waitForResponse, TIMEOUT_DEFAULT_MS);
    }

    @Override
    public String sendMessage(String msg, boolean waitForResponse, int timeout) {
        long startTime = System.currentTimeMillis();
        String response = null;
        while (System.currentTimeMillis() - startTime < timeout) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(InetAddress.getByName(IP_STRING), PORT), timeout);

                DataOutputStream clientWriter = new DataOutputStream(socket.getOutputStream());
                clientWriter.writeBytes(msg + "\n");
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
                e.printStackTrace();
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
