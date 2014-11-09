package networking;

import forms.frmVariantChooser;
import main.GameController;
import main.OnlineGameLauncher;
import main.PieceType;
import networking.NetworkingCodes.ClientToClientCode;
import networking.NetworkingCodes.ResponseCode;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageListener implements Runnable {
    public static final int LISTENER_PORT = 4445;
    private ServerSocket sktListener;
    private static MessageListener instance;
    private InetAddress remoteAddress;

    private Thread thread;

    private boolean acceptJoins = false;
    public boolean acceptMoves = false;

    //For game set up
    private int pieceCode = -1;
    private String board = "";

    //For ingame move-swapping.
    private GameController gameController;

    //This needs to be a singleton as two identical sockets cannot be opened
    // on the same computer, yet the port number must be known.
    public static MessageListener getInstance() {
        if (instance == null) {
            instance = new MessageListener();
        }
        return instance;
    }

    private MessageListener() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            sktListener = new ServerSocket(LISTENER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("MessageListener running");
        //Run listening loop
        while (true) {
            
            try {
                //The below 3 lines are dangerous ones, as they will block
                //until a message has been sent or the socket is closed by
                //the other client - either by .close() or program exit.
                //If the other client does this twice: s = new Socket(me)
                //the second message will not be flushed until the first is!
                Socket socket = sktListener.accept();
                BufferedReader clientReader =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String clientMessage = clientReader.readLine();

                System.out.println("MessageListener awakened");
                System.out.println("[" + socket.getInetAddress().getHostAddress() + "] sent me: " + clientMessage);

                //Process message and generate an appropriate response, or none if it is junk.
                String serverResponseMessage = processMessageAndGetResponse(socket, clientMessage);
                if (serverResponseMessage != null) {
                    DataOutputStream clientWriter = new DataOutputStream(socket.getOutputStream());
                    clientWriter.writeBytes(serverResponseMessage + "\n");
                    System.out.println("My response to [" + socket.getInetAddress().getHostAddress() + "]: " + serverResponseMessage);

                } else {
                    System.out.println("I do not need to respond to this message");
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
                        String joinerUsername = fields[1];
                        int joinerRating = Integer.valueOf(fields[2]);

                        response = ResponseCode.OK + ResponseCode.DEL + pieceCode + ResponseCode.DEL + board;
                        acceptJoins = false;
                        remoteAddress = socket.getInetAddress();
                        OnlineGameLauncher launcher = (OnlineGameLauncher) frmVariantChooser.currentGameLauncher;
                        launcher.setOpponent(remoteAddress, joinerUsername, joinerRating);
                        launcher.setFirstMover(pieceCode == PieceType.WHITE.ordinal());

                        frmVariantChooser.currentGameLauncher.launch();
                        acceptMoves = true;

                        GameLobby.getInstance().close();

                    } else {
                        response = ResponseCode.REFUSED + "";
                    }
                    break;

                //Checks that the game is running and the IP is recognised.
                //Move is not executed locally if it is invalid. TODO: do something!
                case ClientToClientCode.SEND_MOVE :
                    if (acceptMoves && remoteAddress != null && remoteAddress.equals(socket.getInetAddress())) {
                        String extraField = fields.length > 5 ? fields[5] : "";

                        boolean successfulMove = gameController.handleRemoteMove(
                                Integer.valueOf(fields[1]), Integer.valueOf(fields[2]),
                                Integer.valueOf(fields[3]), Integer.valueOf(fields[4]),
                                extraField);

                        response = (successfulMove ? ResponseCode.OK : ResponseCode.INVALID) + "";
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

    public void setRemoteAddress(InetAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public void hostOpenGame(int pieceCode, String board) {
        acceptJoins = true;
        this.pieceCode = pieceCode;
        this.board = board;
    }

    public void removeOpenGame() {
        acceptJoins = false;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
