package main;

/**
 * Created by jc4512 on 06/11/14.
 */
abstract public class GameLauncher {
    //Global instances, onlineGameLauncher is used during the game
    // and should not be overwritten.
    public static OnlineGameLauncher onlineGameLauncher;
    public static GameLauncher lastGameLauncher;

    public abstract void launch();

    public abstract void setUserIsWhite(boolean localUserIsWhite);

    public abstract boolean userIsWhite();

    public abstract void broadcastMove(Location oldL, Location newL, String extra);

    public abstract void broadcastEndGame();
}
