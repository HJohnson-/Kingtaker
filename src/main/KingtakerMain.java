package main;

//Used for testing local games. Bypasses main menu.
public class KingtakerMain {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
            //Alternatively, load from a jar file:
            //ChessVariant v = VariantLoader.getInstance().getVariantByID(i);
            ChessVariant v = VariantFactory.getInstance().getVariantByID(3);
            GameLauncher.lastGameLauncher = new OfflineGameLauncher(v, GameMode.SINGLE_PLAYER);
            GameLauncher.lastGameLauncher.launch();
        }
    }
}
