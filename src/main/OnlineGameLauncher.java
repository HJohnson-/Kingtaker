package main;

import forms.MessageBoxAlert;
import networking.MessageListener;
import networking.NetworkingCodes.ClientCommandCode;
import networking.NetworkingCodes.ClientToClientCode;
import networking.NetworkingCodes.ResponseCode;
import networking.OpponentMessageSender;

import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by jc4512 on 06/11/14.
 */
public class OnlineGameLauncher extends GameLauncher {
    private Socket sktOpponent;
    private ChessVariant variant;
    private String opponentName;
    private int opponentRating;
    private boolean localUserIsWhite;

    public OnlineGameLauncher(ChessVariant variant) {
        this.variant = variant;
    }

    public OnlineGameLauncher(ChessVariant variant, Socket sktOpponent, String opponentName, int opponentRating) {
        this.sktOpponent = sktOpponent;
        this.variant = variant;
        this.opponentName = opponentName;
        this.opponentRating = opponentRating;
    }

    @Override
    public void launch() {
        MessageListener.getInstance().setGameController(variant.game);
        variant.drawBoard();
    }

    @Override
    public void setFirstMover(boolean localUserIsWhite) {
        this.localUserIsWhite = localUserIsWhite;
    }

    @Override
    public void broadcastMove(Location oldL, Location newL, String extra) {
        OpponentMessageSender oms = new OpponentMessageSender(sktOpponent.getInetAddress());

        StringBuilder message = new StringBuilder(ClientToClientCode.SEND_MOVE + ClientToClientCode.DEL);
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

        if (response == null || !response.equals(ResponseCode.OK)) {
            System.out.println("Other client rejected move!");
        }
    }

    public void setGameBoardLayout(String boardState) {
        variant.game = new GameController(variant.game.getBoard(), variant.getDecoder(), boardState);
    }

    public void setOpponent(InetAddress remoteAddress, String opponentName,
                            int opponentRating) throws Exception {
        sktOpponent = new Socket(remoteAddress, MessageListener.LISTENER_PORT);
        this.opponentRating = opponentRating;
        this.opponentName = opponentName;
    }
}
