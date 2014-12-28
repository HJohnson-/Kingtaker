package networking;

import forms.frmLobby;
import main.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

/**
 * Created by jc4512 on 09/11/14.
 */
public class NetworkedGameTest {
    public static void main(String[] args) throws Exception {

        String host2 = "MERCURY.lan";
        String host1 = "macbook2011.lan";
        ChessVariant variant = VariantFactory.getInstance().getVariantByID(0);

        MessageListener.getInstance().acceptMoves = true;

        boolean isWhite = InetAddress.getLocalHost().getHostName().equals(host1);
        String server = InetAddress.getLocalHost().getHostName().equals(host1) ? host2 : host1;
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
                InetAddress.getByName(server), "playa" + (new Random()).nextInt(100000) + "", 1000);
        o.setUserIsWhite(isWhite);
        GameLauncher.currentGameLauncher = o;
        frmLobby.showInstance(GameLobby.getInstance());

        o.launch();
    }
}
