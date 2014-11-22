package main;

import forms.frmLobby;
import networking.MessageListener;
import networking.NetworkingCodes.ClientCommandCode;
import networking.NetworkingCodes.ClientToClientCode;
import networking.NetworkingCodes.ResponseCode;
import networking.OpponentMessageSender;
import networking.ServerMessageSender;

import java.net.InetAddress;

/**
 * Created by jc4512 on 06/11/14.
 */
public class OnlineGameLauncher extends GameLauncher {
    private InetAddress ipOpponent;
    private ChessVariant variant;
    private String opponentName;
    private int opponentRating;
    private boolean localUserIsWhite;

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

        if (response == null) {
            handleRemoteUserDisconnection();
            return;
        }
        if (!response.equals(ResponseCode.OK + "")) {
            System.out.println("Other client rejected move!");
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


        ServerMessageSender sms = new ServerMessageSender();
        sms.sendMessageAsync(ClientCommandCode.REPORT_GAME_RESULT +
                ClientCommandCode.DEL + winnerParameter + opponentName);
    }

    public void setGameBoardLayout(String boardState) {
        variant.game = new GameController(variant.game.getBoard(), variant.getDecoder(), boardState, GameMode.MULTIPLAYER_ONLINE);
        variant.game.getBoard().setController(variant.game);
    }

    public void setOpponent(InetAddress remoteAddress, String opponentName,
                            int opponentRating) throws Exception {
        ipOpponent = remoteAddress;
        this.opponentRating = opponentRating;
        this.opponentName = opponentName;
    }

    //TODO: handle this properly. Send a report to the server and offer to complete the game with AI?
    private void handleRemoteUserDisconnection() {
        System.out.println("Cannot connect to opponent!");

    }
}
