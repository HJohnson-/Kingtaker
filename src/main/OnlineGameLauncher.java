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
        variant.drawBoard();
    }

    @Override
    public void setFirstMover(boolean localUserIsWhite) {
        this.localUserIsWhite = localUserIsWhite;
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
