package networking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jc4512 on 29/10/14.
 */
public class GameLobby {
    private List<RemoteGame> games;
    private GameLobbyFetcher gameLobbyFetcher;
    private Thread fetcherThread;


    public GameLobby() {
        games = new ArrayList<RemoteGame>();
        gameLobbyFetcher = new GameLobbyFetcher();
        fetcherThread = new Thread(gameLobbyFetcher);
        fetcherThread.run();
    }

    private class GameLobbyFetcher implements Runnable {
        @Override
        public void run() {

        }
    }
}
