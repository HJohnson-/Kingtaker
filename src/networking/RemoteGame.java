package networking;

import java.net.InetAddress;

/**
 * Created by jc4512 on 29/10/14.
 */
public class RemoteGame {
    public InetAddress ip;
    public String hostUsername;
    public int hostRating;
    public int variantId;

    public RemoteGame(InetAddress ip, String hostUsername, int hostRating, int variantId) {
        this.ip = ip;
        this.hostUsername = hostUsername;
        this.hostRating = hostRating;
        this.variantId = variantId;
    }
}
