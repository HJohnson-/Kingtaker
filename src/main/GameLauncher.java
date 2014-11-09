package main;

/**
 * Created by jc4512 on 06/11/14.
 */
abstract public class GameLauncher {
    public static GameLauncher currentGameLauncher; //global instance

    public abstract void launch();

    public abstract void setFirstMover(boolean localUserIsWhite);

    public abstract void broadcastMove(Location oldL, Location newL, String extra);
}
