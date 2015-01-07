package variants.RollerBallChess;

import graphics.GraphicsTools;
import main.ChessVariant;
import main.GameController;
import main.GameMode;
import pieces.PieceDecoder;
import variants.BasicChess.BasicChessFrame;
import variants.BasicChess.BasicChessvar;

import java.util.Random;

public class RollerBallvar extends ChessVariant {

    @Override
    public int getVariationID() {
        return 4;
    }

    @Override
    public String getName() {
        return "Rollerball";
    }

    @Override
    public String getDescription() {
        return "Rollerball is a chess variant invented by Jean-Louis Cazaux in 1998. " +
                "The game was inspired by the 1975 science-fiction movie Rollerball, " +
                "specifically the futuristic and violent sport (similar to Roller Derby) " +
                "portrayed in the film. \n\nThe board comprises 7×7 squares with the central 3×3 " +
                "section missing. Pieces generally move clockwise around the board. Each " +
                "player starts with one king, one bishop, two rooks, and two pawns. " +
                "the objective is to either checkmate the opponent's King or to move ones own " +
                "King into the opponent King's starting position.";
    }

    @Override
    public PieceDecoder getDecoder() {
        return new RBDecoder();
    }

    public RollerBallvar() {
        this(GameMode.MULTIPLAYER_LOCAL);
    }

    public RollerBallvar(GameMode mode) {
        RBBoard board = new RBBoard();
        board.initializeBoard();
        game = new GameController(board, getVariationID(), new RBDecoder(), mode, new Random().nextBoolean());
        game.getBoard().setController(game);
    }

    public RollerBallvar(GameController game) {
        this.game = game;
        this.game.getBoard().setController(game);
    }

    @Override
    public boolean drawBoard() {
        GraphicsTools.create(new RBFrame(getName(), 500, 700, game.getBoard()));
        return true;
    }

    @Override
    public RollerBallvar recreate(GameMode mode) {
        return new RollerBallvar(mode);
    }
}

