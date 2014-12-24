package main;

import forms.MessageBoxAlert;
import forms.frmJoinRequest;
import forms.frmLobby;
import networking.*;
import networking.NetworkingCodes.ClientCommandCode;
import networking.NetworkingCodes.ClientToClientCode;
import networking.NetworkingCodes.ResponseCode;

import java.net.InetAddress;

public class OnlineGameLauncher extends GameLauncher {
    private InetAddress ipOpponent;
    private ChessVariant variant;
    private String opponentName;
    private int opponentRating;
    private boolean localUserIsWhite;
    private String hostedGameCode;

    private String pendingName;
    private int pendingRating;
    private InetAddress pendingIP;


    public OnlineGameLauncher(ChessVariant variant) {
        this.variant = variant;
        variant.game.gameMode = GameMode.MULTIPLAYER_ONLINE;
    }

    public OnlineGameLauncher(ChessVariant variant, InetAddress ipOpponent,
                              String opponentName, int opponentRating) {
        this.variant = variant;
        variant.game.gameMode = GameMode.MULTIPLAYER_ONLINE;
        this.opponentName = opponentName;
        this.opponentRating = opponentRating;
        this.ipOpponent = ipOpponent;
    }

    //Called on both host and client when a new game has been agreed.
    //Initialises the game controller and board (host decides layout)
    //Creates game form and resets the lobby form.
    @Override
    public void launch() {
        frmLobby.getInstance().setBtnCreateRemoveGame(false);
        variant.game.gameMode = GameMode.MULTIPLAYER_ONLINE;
        MessageListener.getInstance().setGameController(variant.game);
        variant.game.getBoard().setController(variant.game);
        variant.drawBoard();
    }

    @Override
    public void setUserIsWhite(boolean localUserIsWhite) {
        this.localUserIsWhite = localUserIsWhite;
        variant.game.playerIsWhite = localUserIsWhite;
    }

    @Override
    public boolean userIsWhite() {
        return localUserIsWhite;
    }

    //When the player locally makes a move, this method is called so that the
    //remote player is told of this move.
    @Override
    public void broadcastMove(Location oldL, Location newL, String extra) {
        OpponentMessageSender oms = new OpponentMessageSender(ipOpponent);

        StringBuilder message = new StringBuilder(
                ClientToClientCode.SEND_MOVE + ClientToClientCode.DEL);
        message.append(oldL.getX());
        message.append(ClientToClientCode.DEL);
        message.append(oldL.getY());
        message.append(ClientToClientCode.DEL);
        message.append(newL.getX());
        message.append(ClientToClientCode.DEL);
        message.append(newL.getY());
        message.append(ClientToClientCode.DEL);
        message.append(extra);
        String response = oms.sendMessage(message.toString(), true);

        if (response == null || !response.equals(ResponseCode.OK + "")) {
            System.out.println("Other client rejected move or disconnected!");
            handleRemoteUserDisconnection();
        }
    }

    //Called by both clients when they detect the game has ended.
    //This contacts the server which will only act on the result if both
    //results match.
    public void broadcastEndGame() {
        int winnerParameter = -1;
        switch (variant.game.getResult()) {
            case DRAW:
                winnerParameter = ClientCommandCode.PARAM_GAME_DRAW;
                break;
            case WHITE_WIN:
                winnerParameter = localUserIsWhite ?
                        ClientCommandCode.PARAM_GAME_WIN :
                        ClientCommandCode.PARAM_GAME_LOSS;
                break;
            case WHITE_LOSS:
                winnerParameter = localUserIsWhite ?
                        ClientCommandCode.PARAM_GAME_LOSS :
                        ClientCommandCode.PARAM_GAME_WIN;
                break;
        }

        MessageListener.getInstance().acceptMoves = false;

        ServerMessageSender sms = new ServerMessageSender();
        String response = sms.sendMessage(ClientCommandCode.REPORT_GAME_RESULT +
                ClientCommandCode.DEL + winnerParameter + opponentName, true);
        if (response != null) {
            String newRatingStr = response.replace("0,","");
            if (newRatingStr.matches("\\d+")) {
                int newRating = Integer.valueOf(newRatingStr);
                LocalUserAccount localUser = GameLobby.getInstance().getUser();
                MessageBoxAlert mba = new MessageBoxAlert();
                mba.showNewRating(localUser.getUsername(), localUser.getRating(), newRating);
                localUser.setRating(newRating);
            }
        }
    }

    public void setGameBoardLayout(String boardState) {
        variant.game = new GameController(variant.game.getBoard(), variant.getDecoder(), boardState, GameMode.MULTIPLAYER_ONLINE);
        variant.game.getBoard().setController(variant.game);
    }

    public void setOpponent(InetAddress remoteAddress, String opponentName,
                            int opponentRating) throws Exception {
        ipOpponent = remoteAddress;
        MessageListener.getInstance().setRemoteAddress(ipOpponent);
        this.opponentRating = opponentRating;
        this.opponentName = opponentName;
    }

    //TODO: handle this properly. Send a report to the server and offer to complete the game with AI?
    private void handleRemoteUserDisconnection() {
        System.out.println("Cannot connect to opponent!");
        variant.game.initialiseAI(1);
        variant.game.gameMode = GameMode.SINGLE_PLAYER;
        (new MessageBoxAlert()).showDisconnectedOpponent(opponentName);

    }

    public void considerJoinRequest(InetAddress ip, String user, int rating) {
        pendingIP = ip;
        pendingName = user;
        pendingRating = rating;
        frmJoinRequest frmJoinRequest = new frmJoinRequest(user, rating, this);
    }

    //Called when user clicks the 'accept' button on the 'this person wants
    //to join your game' form, frmJoinRequest. Starts the game and tells opp.
    public void acceptJoinToGame() throws Exception {
        String acceptanceMessage = ClientToClientCode.JOIN_OPEN_GAME_REQUEST_OK
                + ClientToClientCode.DEL + (localUserIsWhite ? "0" : "1")
                + ClientToClientCode.DEL + hostedGameCode;
        setOpponent(pendingIP, pendingName, pendingRating);
        OpponentMessageSender oms = new OpponentMessageSender(ipOpponent);
        oms.sendMessage(acceptanceMessage, false);

        launch();
        MessageListener.getInstance().acceptMoves = true;
        GameLobby.getInstance().close();
    }

    //Called when user clicks the 'reject' button, closes the window or the
    //timeout elapses on frmJoinRequest. Tells opponent they are rejected.
    public void rejectJoinToGame() {
        MessageListener.getInstance().acceptJoins = true;

        String response = ClientToClientCode.JOIN_OPEN_GAME_REQUEST_NO + "";
        OpponentMessageSender oms = new OpponentMessageSender(pendingIP);
        oms.sendMessage(response, false);

        pendingIP = null;
        pendingName = null;
        pendingRating = 0;
    }

    public void setHostedGameCode(String hostedGameCode) {
        this.hostedGameCode = hostedGameCode;
    }


}
