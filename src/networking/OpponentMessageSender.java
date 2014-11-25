package networking;

import networking.NetworkingCodes.ResponseCode;

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
public class OpponentMessageSender implements IMessageSender {
    private final int PORT = 4445;
    private final int TIMEOUT_DEFAULT_MS = 1000000;
    private final int RETRY_WAIT_MS = 500;

    private InetAddress inetAddress = null;

    public OpponentMessageSender(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public String sendMessage(String msg, boolean waitForResponse) {
        return sendMessage(msg, waitForResponse, TIMEOUT_DEFAULT_MS);
    }

    //Attempts to send message to other client.
    //Returns true if successful, false if there were only timeouts or IO exceptions.
    public String sendMessage(String msg, boolean waitForResponse, int timeout) {
        long startTime = System.currentTimeMillis();
        String response = ResponseCode.UNSPECIFIED_ERROR + "";
        while (System.currentTimeMillis() - startTime < timeout) {
            try {
                Socket socket = new Socket();
                socket.setSoTimeout(timeout);
                socket.connect(new InetSocketAddress(inetAddress, PORT), timeout);

                DataOutputStream clientWriter = new DataOutputStream(socket.getOutputStream());
                clientWriter.writeBytes(msg + "\n");
                System.out.println("I sent to [" + socket.getInetAddress().getHostAddress() + "]: " + msg);

                if (waitForResponse) {
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader clientReader = new BufferedReader(inputStreamReader);
                    response = clientReader.readLine();
                    System.out.println("[" + socket.getInetAddress().getHostAddress() + "] response to my message: " + response);
                    inputStreamReader.close();
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
                    return response;
                }
            }
        }
        return response;
    }
}
