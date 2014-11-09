package main;

import java.util.Random;

/**
 * Created by jc4512 on 06/11/14.
 */
public class OfflineGameLauncher extends GameLauncher {
    private ChessVariant variant;
    private boolean localUserIsWhite;

    public OfflineGameLauncher(ChessVariant variant) {
        this.variant = variant;
        localUserIsWhite = new Random().nextInt(2) == 0;
    }

    @Override
    public void launch() {
        variant.drawBoard();
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
