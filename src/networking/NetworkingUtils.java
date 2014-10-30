package networking;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by jc4512 on 30/10/14.
 */
public class NetworkingUtils {
    public static boolean checkInetAddressIsValid(String ip) {
        try {
            InetAddress.getByName(ip);
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }
}
