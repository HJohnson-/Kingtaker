package main;

import forms.frmJoinRequest;
import variants.BasicChess.BasicBoard;
import variants.BasicChess.BasicChessvar;
import variants.BasicChess.BasicDecoder;

import java.awt.*;
import java.awt.event.KeyEvent;

//Used for testing local games. Bypasses main menu.
public class KingtakerMain {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
            ChessVariant v = VariantFactory.getInstance().getVariantByID(i);
            GameLauncher.currentGameLauncher = new OfflineGameLauncher(v, GameMode.SINGLE_PLAYER);
            GameLauncher.currentGameLauncher.setUserIsWhite(false);
            GameLauncher.currentGameLauncher.launch();
        }

    }
}