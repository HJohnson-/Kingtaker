package networking;

import forms.frmLobby;
import main.*;
import variants.VariantLoader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by jc4512 on 09/11/14.
 */
public class NetworkedGameTest {
    public static void main(String[] args) throws Exception {

        String host2 = "MERCURY";
        String host1 = "macbook2011";
        ChessVariant variant = VariantLoader.getInstance().getVariantByID(4);

        //Test requires server to be running!
        MessageListener.getInstance().acceptMoves = true;

        boolean isWhite = InetAddress.getLocalHost().getHostName().startsWith(host1);
        String server = InetAddress.getLocalHost().getHostName().startsWith(host1) ? host2 : host1;

        String toAuth = InetAddress.getLocalHost().getHostName().startsWith(host1) ? host1 : host2;
        GameLobby.getInstance().attemptLogin(toAuth, "stupid".toCharArray());

        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket(server, 4445);
                socket.close(); //very very important here
                break;
            } catch (IOException e) {
            }
        }

        MessageListener.getInstance().setRemoteAddress(socket.getInetAddress());

        GameMode.currentGameMode = GameMode.MULTIPLAYER_ONLINE;
        OnlineGameLauncher o = new OnlineGameLauncher(variant,
                InetAddress.getByName(server), server, 1000);
        o.setUserIsWhite(isWhite);
        GameLauncher.onlineGameLauncher = o;
        frmLobby.showInstance(GameLobby.getInstance());

        o.launch();
    }
}
