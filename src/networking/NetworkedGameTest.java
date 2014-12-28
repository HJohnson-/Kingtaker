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

        String host2 = "MERCURY";
        String host1 = "macbook2011";
        ChessVariant variant = VariantFactory.getInstance().getVariantByID(0);

        //Test requires server to be running!
        GameLobby.getInstance().attemptRegister(InetAddress.getLocalHost().getHostName() + "_" + System.currentTimeMillis() % 100000,
                "stupid".toCharArray());

        MessageListener.getInstance().acceptMoves = true;

        boolean isWhite = InetAddress.getLocalHost().getHostName().startsWith(host1);
        String server = InetAddress.getLocalHost().getHostName().startsWith(host1) ? host2 : host1;
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
                InetAddress.getByName(server), "imposs2know", 1000);
        o.setUserIsWhite(isWhite);
        GameLauncher.currentGameLauncher = o;
        frmLobby.showInstance(GameLobby.getInstance());

        o.launch();
    }
}
