package networking;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerMessageSender implements IMessageSender {

    private String ABSOLUTE_PATH_TO_SERVER_CONFIG_FILE = "server.txt";
    private InetSocketAddress serverAddress;
    private final int PORT = 4444;
    private final int TIMEOUT_DEFAULT_MS = 10000;
    private final int RETRY_WAIT_MS = 500;

    //Server:port is loaded on demand from text file next to jar.
    public InetSocketAddress getServerSocketAddress() {
        if (serverAddress == null) {
            try {
                File file = new File(ABSOLUTE_PATH_TO_SERVER_CONFIG_FILE);
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();
                String text = new String(data, "UTF-8").trim();
                serverAddress = new InetSocketAddress(text.split(":")[0],
                                    Integer.parseInt(text.split(":")[1]));
            } catch (Exception e) {
                //If for any reason the server file is not found or is wrong,
                //lobby will not load, but single player is not impacted.
                e.printStackTrace();
            }
        }
        return serverAddress;
    }

    public void sendMessageAsync(String msg) {
        //TODO: implement this if needed.
    }

    //Send message to server and if flag is set, await a response.
    //Note that waitForResponse = false still guarantees the packet arrived.
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
                socket.connect(getServerSocketAddress(), timeout);

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
