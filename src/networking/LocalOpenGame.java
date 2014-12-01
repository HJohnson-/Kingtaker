package networking;

import main.ChessVariant;
import main.OnlineGameLauncher;
import main.PieceType;
import networking.NetworkingCodes.ClientCommandCode;

import java.util.Date;
import java.util.Random;

/**
 * Created by jc4512 on 01/11/14.
 */
public class LocalOpenGame {
    public ChessVariant variant;

    public OnlineGameLauncher launcher;
    private PieceType pieceType;
    private Date creationDate;

    public LocalOpenGame(ChessVariant variant) {
        this.variant = variant;
        creationDate = new Date();

        //Coin flip to decide whether playing first. Host decides! 0=white
        pieceType = new Random().nextInt(2) == 0 ? PieceType.WHITE : PieceType.BLACK;
    }

    public void host() {
        ServerMessageSender sms = new ServerMessageSender();
        String response = sms.sendMessage(ClientCommandCode.CREATE_GAME
                + ClientCommandCode.DEL + variant.getVariationID(), true);

        int pieceTypeCode = pieceType.ordinal();
        launcher.setUserIsWhite(pieceType == PieceType.WHITE);
        launcher.setHostedGameCode(variant.game.toCode());
        MessageListener.getInstance().hostOpenGame();
    }

    // Removes open game from server lobby.
    // MessageListener is told to refuse any join requests from now on.
    public void destroy() {
        ServerMessageSender sms = new ServerMessageSender();
        sms.sendMessage(ClientCommandCode.REMOVE_GAME + "", false);
        MessageListener.getInstance().removeOpenGame();
    }
}
