package main;

import networking.MessageListener;

import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by jc4512 on 06/11/14.
 */
public class OnlineGameLauncher extends GameLauncher {
    private Socket sktOpponent;
    private ChessVariant variant;

    public OnlineGameLauncher(ChessVariant variant) {
        this.variant = variant;
    }

    public OnlineGameLauncher(ChessVariant variant, Socket sktOpponent) {
        this.sktOpponent = sktOpponent;
        this.variant = variant;
    }

    public void setOpponentAddress(InetAddress ip) throws Exception {
        sktOpponent = new Socket(ip, MessageListener.LISTENER_PORT);
    }

    @Override
    public void launch() {
        variant.drawBoard();
    }
}
