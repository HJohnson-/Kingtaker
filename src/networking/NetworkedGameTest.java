package networking;

import forms.frmVariantChooser;
import main.ChessVariant;
import main.ChessVariantManager;
import main.GameLauncher;
import main.OnlineGameLauncher;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

/**
 * Created by jc4512 on 09/11/14.
 */
public class NetworkedGameTest {
    public static void main(String[] args) throws Exception {
        String host1 = "line24.doc.ic.ac.uk";
        String host2 = "line25.doc.ic.ac.uk";
        ChessVariant variant = ChessVariantManager.getInstance().getVariantByID(0);

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

        OnlineGameLauncher o = new OnlineGameLauncher(variant,
                InetAddress.getByName(server), "playa" + (new Random()).nextInt(100000) + "", 1000);
        o.setUserIsWhite(isWhite);
        GameLauncher.currentGameLauncher = o;


        o.launch();
    }
}
