package networking;

import forms.frmLobby;
import networking.NetworkingCodes.ClientCommandCode;
import networking.NetworkingCodes.ResponseCode;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static networking.NetworkingUtils.checkInetAddressIsValid;

/**
 * Created by jc4512 on 29/10/14.
 */
public class GameLobby {
    private List<RemoteGame> games;
    private GameLobbyFetcher gameLobbyFetcher;
    private Thread fetcherThread;

    private frmLobby frm;

    public GameLobby(JFrame parentFrame) {
        games = Collections.synchronizedList(new ArrayList<RemoteGame>());
        gameLobbyFetcher = new GameLobbyFetcher();
        fetcherThread = new Thread(gameLobbyFetcher);
        fetcherThread.start();

        frm = new frmLobby(parentFrame, this);
    }

    private void parseRemoteGameList(String list) {
        games.clear();

        String[] lobbyLines = list.split("\\{");
        for (String line : lobbyLines) {
            String[] fields = line.replace("},","").replace("}", "").split(",");
            if (fields.length == 4 && checkInetAddressIsValid(fields[0]) &&
                    fields[1].matches("\\w+") &&
                    fields[2].matches("\\d+") &&
                    fields[3].matches("\\d+")) {

                RemoteGame remoteGame = new RemoteGame(
                    fields[0], fields[1], Integer.parseInt(fields[2]),
                        Integer.parseInt(fields[3])
                );
                games.add(remoteGame);
            }
        }
    }

    public void close() {
        fetcherThread.interrupt();
        //TODO: remove any open games
    }

    private class GameLobbyFetcher implements Runnable {
        private final long REFRESH_MS = 10000;
        @Override

        //Keep requesting a new game list. Upon receipt, check the response code
        // and pass the list (minus the response code) to GameLobby.
        //Runs until interrupted (thread is disposed).
        public void run() {
            ServerMessageSender sms = new ServerMessageSender();
            while (true) {
                String response = sms.sendMessage(
                        ClientCommandCode.GET_GAME_LIST + "", true);
                if (response != null && response.startsWith(ResponseCode.OK + ",")) {
                    parseRemoteGameList(response.substring(2));
                    frm.displayOpenGames(games);
                }

                try {
                    Thread.sleep(REFRESH_MS);
                } catch (InterruptedException e) {
                    //TODO: test this, not sure how java handles threads
                    return;
                }
            }
        }
    }
}
