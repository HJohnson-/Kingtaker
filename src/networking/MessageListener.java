package networking;

import main.GameController;
import main.GameLauncher;
import main.OnlineGameLauncher;
import networking.NetworkingCodes.ClientToClientCode;
import networking.NetworkingCodes.ResponseCode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageListener implements Runnable {
    public static final int LISTENER_PORT = 4445;
    private ServerSocket sktListener;
    private static MessageListener instance;
    private InetAddress remoteAddress;

    private Thread thread;

    public boolean acceptJoins = false;
    public boolean acceptMoves = false;

    private final long JOIN_REQUEST_TIMEOUT_MS = GameLobby.JOIN_GAME_TIMEOUT_MS + 1000;
    private String joinResponse = "";

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
            //Just exit immediately if already running a listening server.
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("MessageListener running");
        //Run listening loop
        while (true) {
            System.out.println("acceptMoves = " + acceptMoves);
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
                case ClientToClientCode.JOIN_OPEN_GAME_REQUEST:
                    if (acceptJoins) {
                        acceptJoins = false;
                        String joinerUsername = fields[1];
                        int joinerRating = Integer.valueOf(fields[2]);

                        //Host will send OK message to confirm it has received the request.
                        OnlineGameLauncher launcher = (OnlineGameLauncher) GameLauncher.currentGameLauncher;
                        launcher.considerJoinRequest(socket.getInetAddress(), joinerUsername, joinerRating);
                        return ResponseCode.OK + "";
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

                        //Just disconnect the game and replace opponent with AI if they are trying to cheat.
                        if (!successfulMove) {
                            OnlineGameLauncher launcher = (OnlineGameLauncher) GameLauncher.currentGameLauncher;
                            launcher.handleRemoteUserDisconnection();
                        }
                        response = (successfulMove ? ResponseCode.OK : ResponseCode.INVALID) + "";

                        //update display on separate thread.
                    } else {
                        response = ResponseCode.REFUSED + "";
                    }
                    break;

                //Message will be picked up by getHostJoinResponse()
                case ClientToClientCode.JOIN_OPEN_GAME_REQUEST_OK :
                case ClientToClientCode.JOIN_OPEN_GAME_REQUEST_NO :
                    System.out.println(System.currentTimeMillis() +  message);
                    joinResponse = message;
                    System.out.println(System.currentTimeMillis() +  message);
                    return null;
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

    public void hostOpenGame() {
        acceptJoins = true;
    }

    public void removeOpenGame() {
        acceptJoins = false;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    //Warning: will block current thread for 10 seconds, or until a
    //response is gotten.
    public String getHostJoinResponse() {
        String joinResponseOld = joinResponse;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < JOIN_REQUEST_TIMEOUT_MS) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
            if (!joinResponseOld.equals(joinResponse)) {
                return joinResponse;
            }
        }

        return null;
    }
}
