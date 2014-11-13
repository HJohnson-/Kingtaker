package main;

import java.util.Random;

/**
 * Created by jc4512 on 06/11/14.
 */
public class OfflineGameLauncher extends GameLauncher {
    private ChessVariant variant;
    private boolean localUserIsWhite;
    private GameMode mode;

    public OfflineGameLauncher(ChessVariant variant, GameMode mode) {
        this.mode = mode;
        this.variant = variant;
        localUserIsWhite = false;//new Random().nextInt(2) == 0;
        //temporary for debugging
    }

    @Override
    public void launch() {
        ChessVariant newVariant = variant.recreate(mode);
        newVariant.drawBoard();
//        newVariant.game.initialiseAI();
        //Why does the game board not draw????
    }

    @Override
    public void setUserIsWhite(boolean localUserIsWhite) {
        this.localUserIsWhite = localUserIsWhite;
    }

    @Override
    public boolean userIsWhite() {
        return localUserIsWhite;
    }

    @Override
    public void broadcastMove(Location oldL, Location newL, String extra) {
        // No-one cares about your move.
    }
}
