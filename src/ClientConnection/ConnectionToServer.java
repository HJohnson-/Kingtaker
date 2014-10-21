package ClientConnection;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
/**
 * Created by daniel on 14/10/20.
 */

public class ConnectionToServer {
    // ip not set yet
    private String serverIP = "146.169.46.133";
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;
    private int attempt = 0;
    private int serverPort = 4444;
    private int clientPort = 3333;
    private String messageFromServer = "NULL";
    private static final char MESSAGE_DELIMINATOR = ',';


    public ObjectInputStream getInputStream() {
        return input;
    }

    public ObjectOutputStream getOutputStream() {
        return output;
    }

    public Socket getSocket() {
        return socket;
    }


    public ConnectionToServer connect() throws Exception {
        try {
            while (socket == null && attempt < 10) {
                System.out.println("Attempting to connect to server :  \n" + serverIP
                        + " on port " + serverPort);


                System.out.println("Attempting : " + attempt);

                // set up socket which is connecting to the server
                socket = new Socket();
                socket.connect(new InetSocketAddress(serverIP, serverPort), clientPort);

                // set output stream
                this.output = new ObjectOutputStream(socket.getOutputStream());
                output.flush();

                // set input stream
                this.input = new ObjectInputStream(socket.getInputStream());

                // testing if socket is null
                if (socket == null) {
                    System.out.println("Socket Null, Connection fail");
                } else {
                    System.out.println("Server Connected!");
                }
                attempt += 1;
            }
            if (attempt == 10) {
                System.out.println("Connection fail.. disconnected!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

}