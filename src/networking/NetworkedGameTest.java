package networking;

import forms.frmVariantChooser;
import main.ChessVariant;
import main.ChessVariantManager;
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

        String server = InetAddress.getLocalHost().getHostName().equals(host1) ? host2 : host1;

        while (s == null) {
            try {
                s = new Socket(server, 4445);
                s.close(); //very very important here
                break;
            } catch (IOException e) {
            }
        }

        MessageListener.getInstance().setRemoteAddress(s.getInetAddress());

        OnlineGameLauncher o = new OnlineGameLauncher(variant,
                InetAddress.getByName(server), "playa" + (new Random()).nextInt(100000) + "", 1000);
        frmVariantChooser.currentGameLauncher = o;

        o.launch();
    }
}
