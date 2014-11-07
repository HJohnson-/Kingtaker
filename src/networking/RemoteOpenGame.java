package networking;

import networking.NetworkingCodes.ClientToClientCode;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by jc4512 on 29/10/14.
 */
public class RemoteOpenGame {
    public InetAddress ip;
    public String hostUsername;
    public int hostRating;
    public int variantId;

    public RemoteOpenGame(String ipString, String hostUsername, int hostRating, int variantId) {
        try {
            this.ip = InetAddress.getByName(ipString);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        this.hostUsername = hostUsername;
        this.hostRating = hostRating;
        this.variantId = variantId;
    }

    public int attemptToJoin(LocalUserAccount localUser) {
        String message = ClientToClientCode.JOIN_OPEN_GAME + ClientToClientCode.DEL + localUser.getUsername() + ClientToClientCode.DEL + localUser.getRating();
        OpponentMessageSender oms = new OpponentMessageSender(ip);
        oms.sendMessage(message);
        //TODO: untested
    }
}
