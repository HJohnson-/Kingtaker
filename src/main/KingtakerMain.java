package main;

import forms.frmJoinRequest;
import variants.BasicChess.BasicBoard;
import variants.BasicChess.BasicChessvar;
import variants.BasicChess.BasicDecoder;

//Used for testing local games. Bypasses main menu.
public class KingtakerMain {
    public static void main(String[] args) throws Exception {
        ChessVariant v = VariantFactory.getInstance().getVariantByID(1);
        GameLauncher.currentGameLauncher = new OfflineGameLauncher(v, GameMode.SINGLE_PLAYER);
        GameLauncher.currentGameLauncher.setUserIsWhite(false);
        GameLauncher.currentGameLauncher.launch();
    }
}
