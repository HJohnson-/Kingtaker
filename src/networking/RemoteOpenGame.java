package networking;

import main.ChessVariant;
import main.GameLauncher;
import main.OnlineGameLauncher;
import main.PieceType;
import networking.NetworkingCodes.ClientToClientCode;
import networking.NetworkingCodes.ResponseCode;
import variants.VariantLoader;

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
        String message = ClientToClientCode.JOIN_OPEN_GAME_REQUEST +
                ClientToClientCode.DEL + localUser.getUsername() +
                ClientToClientCode.DEL + localUser.getRating();
        OpponentMessageSender oms = new OpponentMessageSender(ip);
        String response = oms.sendMessage(message, true);

        int responseCode = Integer.parseInt(response);
        if (responseCode == ResponseCode.OK) {
            String secondResponse =
                    MessageListener.getInstance().getHostJoinResponse();

            if (secondResponse == null) {
                //Time out
                return ResponseCode.EMPTY;
            } else if (secondResponse.startsWith(
                    ClientToClientCode.JOIN_OPEN_GAME_REQUEST_NO + "")) {
                return ResponseCode.REFUSED;
            } else if (secondResponse.startsWith(
                    ClientToClientCode.JOIN_OPEN_GAME_REQUEST_OK + "")) {
                try {
                    String[] fields = secondResponse.split(ResponseCode.DEL);
                    PieceType remotePiece = PieceType.values()[Integer.valueOf(fields[1])];
                    boolean localUserIsWhite = !remotePiece.equals(PieceType.WHITE);
                    String boardState = fields[2];

                    OnlineGameLauncher launcher = new OnlineGameLauncher(
                            VariantLoader.getInstance().getVariantByID(variantId),
                            ip,
                            hostUsername,
                            hostRating
                    );

					ChessVariant chosenVariant = VariantLoader.getInstance().getVariantByID(variantId);
					launcher.setGameBoardLayout(chosenVariant.game, boardState); //changed to accommodate different controller
                    launcher.setUserIsWhite(localUserIsWhite);
                    MessageListener.getInstance().acceptMoves = true;
                    MessageListener.getInstance().setRemoteAddress(ip);

                    GameLauncher.onlineGameLauncher = launcher;
                    return ResponseCode.OK;
                } catch (Exception e) {
                    //Loading the game failed due to a malformed board state
                }
            }

            //Report the hoster refused the join request otherwise.
            return ResponseCode.REFUSED;
        } else if (responseCode == ResponseCode.INVALID) {
            //Game already started or deleted at host end.
        }

        //Response malformed/unexpected.
        return responseCode;

    }
}
