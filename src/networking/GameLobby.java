package networking;

import forms.frmLobby;
import main.ChessVariant;
import main.ChessVariantManager;
import networking.NetworkingCodes.ClientCommandCode;
import networking.NetworkingCodes.ResponseCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static networking.NetworkingUtils.checkInetAddressIsValid;

/**
 * Created by jc4512 on 29/10/14.
 */
public class GameLobby {
    private List<RemoteOpenGame> games;
    private LocalOpenGame localOpenGame;
    private LocalUserAccount localUser;

    private static GameLobby instance;

    private GameLobbyFetcher gameLobbyFetcher;
    private Thread fetcherThread;

    private boolean lobbyIsOpen;

    public static GameLobby getInstance() {
        if (instance == null) {
            instance = new GameLobby();
        }
        return instance;
    }

    public static boolean isOpen() {
        return instance != null && instance.lobbyIsOpen;
    }

    private GameLobby() {
        games = Collections.synchronizedList(new ArrayList<RemoteOpenGame>());
        gameLobbyFetcher = new GameLobbyFetcher();
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

                RemoteOpenGame remoteOpenGame = new RemoteOpenGame(
                    fields[0], fields[1], Integer.parseInt(fields[2]),
                        Integer.parseInt(fields[3])
                );
                games.add(remoteOpenGame);
            }
        }
    }

    public LocalUserAccount getUser() {
        return localUser;
    }

    public void close() {
        lobbyIsOpen = false;
        if (localOpenGame != null) {
            localOpenGame.destroy();
        }
        frmLobby.hideInstance();
    }
    public void open() {
        lobbyIsOpen = true;
        frmLobby.showInstance(instance);

        if (fetcherThread == null || !fetcherThread.isAlive()) {
            fetcherThread = new Thread(gameLobbyFetcher);
            fetcherThread.start();
        }
    }

    public int attemptLogin(String user, char[] password) {
        localUser = new LocalUserAccount(user, new String(password));
        return localUser.authenticate(ClientCommandCode.AUTHENTICATE_USER);
    }

    public int attemptRegister(String user, char[] password) {
        localUser = new LocalUserAccount(user, new String(password));
        return localUser.authenticate(ClientCommandCode.REGISTER_ACCOUNT);
    }

    public int attemptJoinGameByUsername(String username) {
        //Find remote open game.
        for (RemoteOpenGame game : games) {
            if (game.hostUsername == username) {
                return game.attemptToJoin(localUser);
            }
        }
        return ResponseCode.UNSPECIFIED_ERROR;
    }

    public void createLocalOpenGame(ChessVariant variant) {
        localOpenGame = new LocalOpenGame(variant);
        localOpenGame.host();

        //Forces game lobby to be refreshed.
        fetcherThread.interrupt();
    }

    public LocalOpenGame getLocalGame() {
        return localOpenGame;
    }

    public void destroyLocalGame() {
        localOpenGame.destroy();
        localOpenGame = null;
        //Forces game lobby to be refreshed.
        fetcherThread.interrupt();
    }

    //Repeatedly requests a new game list. Upon receipt, checks response code
    // and passes the list (minus the response code) to GameLobby. If the
    // connection is lost, the list is cleared and the connection status
    // is shown on the form. Stops running if the lobby is closed.
    private class GameLobbyFetcher implements Runnable {
        private final long REFRESH_MS = 10000;
        @Override

        public void run() {
            ServerMessageSender sms = new ServerMessageSender();
            boolean isOnline;
            while (lobbyIsOpen) {
                String response = sms.sendMessage(
                        ClientCommandCode.GET_GAME_LIST + "", true);
                if (response != null) {
                    isOnline = true;
                    if (response.startsWith(ResponseCode.OK + ResponseCode.DEL)) {
                        parseRemoteGameList(response.substring(2));
                    } else if (response.equals(ResponseCode.EMPTY + "")) {
                        games.clear();
                    }
                } else {
                    isOnline = false;
                    games.clear();
                }

                frmLobby.getInstance().setOpenGamesAndServerStatus(
                        games, isOnline, localUser);

                try {
                    Thread.sleep(REFRESH_MS);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
