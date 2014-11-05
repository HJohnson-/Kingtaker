package networking;

import networking.NetworkingCodes.ClientToClientCode;
import networking.NetworkingCodes.ResponseCode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageListener implements Runnable {
    private static final int LISTENER_PORT = 4445;
    private ServerSocket sktListener;
    private static MessageListener instance;

    private boolean acceptJoins = false;
    public boolean acceptMoves = false;
    private int pieceCode = -1;

    //This needs to be a singleton as two identical sockets cannot be opened
    // on the same computer, yet the port number must be known.
    public static MessageListener getInstance() {
        if (instance == null) {
            instance = new MessageListener();
        }
        return instance;
    }

    private MessageListener() {}

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
                System.out.println("[" + socket.getInetAddress().getHostAddress() + "] sent: " + clientMessage);

                //Process message and generate an appropriate response, or none if it is junk.
                String serverResponseMessage = processMessageAndGetResponse(socket, clientMessage);
                if (serverResponseMessage != null) {
                    DataOutputStream clientWriter = new DataOutputStream(socket.getOutputStream());
                    clientWriter.writeBytes(serverResponseMessage);
                    System.out.println("[" + socket.getInetAddress().getHostAddress() + "] response: " + serverResponseMessage);
                } else {
                    System.out.println("[" + socket.getInetAddress().getHostAddress() + "] no response required");
                }

                clientReader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String processMessageAndGetResponse(Socket socket, String message) {
        String response = null;
        try {
            String fields[] = message.split(ClientToClientCode.DEL);
            int clientToClientCode = Integer.valueOf(fields[0]);

            switch (clientToClientCode) {
                case ClientToClientCode.JOIN_OPEN_GAME :
                    if (acceptJoins) {
                        response = ResponseCode.OK + ResponseCode.DEL + pieceCode;
                        acceptJoins = false;
                        //TODO: initiate game. We probably want a GameLauncher class or something

                    } else {
                        response = ResponseCode.REFUSED + "";
                    }
                    break;

                case ClientToClientCode.SEND_MOVE :
                    if (acceptMoves /*&& The socket is expected? What is the proper behaviour here?*/) {
                        //TODO: swapping moves
                    } else {
                        response = ResponseCode.REFUSED + "";
                    }
                    break;
            }

        } catch (Exception e) {
            //Is only supposed to occur if the message is malformed and fields[n] is out of bounds,
            //throwing an ArrayIndexOutOfBoundsException.
            response = ResponseCode.INVALID + "";
        }
        return response;
    }

    public void hostOpenGame(int pieceCode) {
        acceptJoins = true;
        this.pieceCode = pieceCode;
    }

    public void removeOpenGame() {
        acceptJoins = false;
    }
}
