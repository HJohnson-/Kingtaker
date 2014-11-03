package networking;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by jc4512 on 29/10/14.
 */
public class RemoteGame {
    public InetAddress ip;
    public String hostUsername;
    public int hostRating;
    public int variantId;

    public RemoteGame(String ipString, String hostUsername, int hostRating, int variantId) {
        try {
            this.ip = InetAddress.getByName(ipString);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        this.hostUsername = hostUsername;
        this.hostRating = hostRating;
        this.variantId = variantId;
    }
}
