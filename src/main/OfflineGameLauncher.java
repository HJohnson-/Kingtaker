package main;

/**
 * Created by jc4512 on 06/11/14.
 */
public class OfflineGameLauncher extends GameLauncher {
    private ChessVariant variant;

    public OfflineGameLauncher(ChessVariant variant) {
        this.variant = variant;
    }

    @Override
    public void launch() {
        variant.drawBoard();
    }
}
