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
        localUserIsWhite = new Random().nextInt(2) == 0;
    }

    @Override
    public void launch() {
        ChessVariant newVariant = variant.recreate(mode);
        newVariant.drawBoard();
        if (mode == GameMode.SINGLE_PLAYER) {
            newVariant.game.makeAIMove();
        }
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

    @Override
    public void broadcastEndGame() {
        // No-one cares who won either.
    }
}
