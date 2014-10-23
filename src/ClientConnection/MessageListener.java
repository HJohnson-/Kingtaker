package ClientConnection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by jc4512 on 23/10/14.
 */

//TODO: make this a singleton
public class MessageListener implements Runnable {
    private static final int LISTENER_PORT = 4445;
    private ServerSocket sktListener;

    @Override
    public void run() {
        try {
            sktListener = new ServerSocket(LISTENER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //Run listening loop
        while (true) {
            try {
                //Receive message from other client or server
                Socket socket = sktListener.accept();
                BufferedReader clientReader =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String clientMessage = clientReader.readLine();
                System.out.println("[" + socket.getInetAddress() + "] sent: " + clientMessage);

                //Process message and generate an appropriate response, or none if it is junk.
                String serverResponseMessage = "OK";//processMessageAndGetResponse(socket,clientMessage);
                if (serverResponseMessage != null) {
                    DataOutputStream clientWriter = new DataOutputStream(socket.getOutputStream());
                    clientWriter.writeBytes(serverResponseMessage);
                    System.out.println("[" + socket.getInetAddress() + "] response: " + serverResponseMessage);
                } else {
                    System.out.println("[" + socket.getInetAddress() + "] no response required");
                }

                //TODO: is this needed?
                clientReader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
