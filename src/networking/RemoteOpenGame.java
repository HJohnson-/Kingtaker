package networking;

import main.*;
import networking.NetworkingCodes.ClientToClientCode;
import networking.NetworkingCodes.ResponseCode;

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
        String response = oms.sendMessage(message, true);

        if (response == null) {
            //Due to timeout
            return ResponseCode.EMPTY;
        } else if (response.equals(ResponseCode.REFUSED + "")) {
            //Game already joined by some other client (most likely).
            return ResponseCode.REFUSED;
        } else if (response.startsWith(ResponseCode.OK + ResponseCode.DEL)) {

            try {
                String[] fields = response.split(ResponseCode.DEL);
                PieceType remotePiece = PieceType.values()[Integer.valueOf(fields[1])];
                boolean localUserIsWhite = !remotePiece.equals(PieceType.WHITE);
                String boardState = fields[2];

                OnlineGameLauncher launcher = new OnlineGameLauncher(
                        ChessVariantManager.getInstance().getVariantByID(variantId),
                        ip,
                        hostUsername,
                        hostRating
                );
                launcher.setGameBoardLayout(boardState);
                launcher.setUserIsWhite(localUserIsWhite);
                MessageListener.getInstance().acceptMoves = true;
                MessageListener.getInstance().setRemoteAddress(ip);

                GameLauncher.currentGameLauncher = launcher;
                return ResponseCode.OK;
            } catch (Exception e) {
                //Loading the game failed due to a malformed board state
            }

        }

        //Response malformed/unexpected.
        return ResponseCode.UNSPECIFIED_ERROR;

    }
}
