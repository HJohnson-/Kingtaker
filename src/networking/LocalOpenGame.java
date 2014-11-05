package networking;

import main.ChessVariant;
import networking.NetworkingCodes.ClientCommandCode;

import java.util.Date;

/**
 * Created by jc4512 on 01/11/14.
 */
public class LocalOpenGame {
    public ChessVariant variant;
    private Date creationDate;

    public LocalOpenGame(ChessVariant variant) {
        this.variant = variant;
        creationDate = new Date();
    }

    public void host() {
        ServerMessageSender sms = new ServerMessageSender();
        String response = sms.sendMessage(ClientCommandCode.CREATE_GAME
                + ClientCommandCode.DEL + variant.getVariationID(), false);
    }

    public void destroy() {
    }
}
