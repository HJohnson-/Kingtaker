package pieces;

import main.GameController;
import main.GameLauncher;
import main.GameMode;
import variants.BasicChess.Bishop;
import variants.BasicChess.Knight;
import variants.BasicChess.Queen;
import variants.BasicChess.Rook;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class PawnPromotion implements Runnable {

    JFrame frame = new JFrame("Pawn Promotion");

    private ChessPiece promotedPiece;
    private ChessPiece pawn;

    // constructor
    public PawnPromotion(ChessPiece pawn){
        this.pawn = pawn;
    }

    // chess piece promotion
    public void promote(PromotablePiece promotablePiece) {
        switch (promotablePiece) {
            case QUEEN:
                promotedPiece = new Queen(pawn.board,pawn.type,pawn.cords);
                break;
            case ROOK:
                promotedPiece = new Rook(pawn.board,pawn.type,pawn.cords);
                break;
            case BISHOP:
                promotedPiece = new Bishop(pawn.board,pawn.type,pawn.cords);
                break;
            case KNIGHT:
                promotedPiece = new Knight(pawn.board,pawn.type,pawn.cords);
                break;
        }

        pawn.board.clearSpace(pawn.cords);
        promotedPiece.board.placePiece(promotedPiece.cords, promotedPiece);
        promotedPiece.lastTurnMovedOn = pawn.lastTurnMovedOn;

        //Broadcast pawn promotion as a move (e.g. 0,2->0,2) with the enum
        //ordinal of the piece it was promoted to. Do not broadcast if
        //this pawn promotion occurred remotely.
        if (promotedPiece.isWhite() == promotedPiece.board.getController().playerIsWhite &&
                promotedPiece.board.getController().gameMode == GameMode.MULTIPLAYER_ONLINE) {
            GameLauncher.onlineGameLauncher.broadcastMove(pawn.cords, pawn.cords, "" + promotablePiece.ordinal());
        }

        if (promotedPiece.board.doDrawing) {
            promotedPiece.graphics.givePanel(pawn.graphics.panel);
            promotedPiece.graphics.panel.recalculateCellSize();
        }

        GameController control = promotedPiece.board.getController();
        control.previousTurn();
        if (control.checkMate()) {
            control.endGame(false);
        } else {
            control.nextPlayersTurn();
        }

        control.cacheInCheckStatus();
        promotedPiece.board.getController().promoting = false;
    }


    //   A board GUI for choosing a promotion piece

    public void addComponentsToPane(Container pane) {
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        addAButton(PromotablePiece.QUEEN, "queen",pane);
        addAButton(PromotablePiece.ROOK,"rook", pane);
        addAButton(PromotablePiece.BISHOP,"bishop", pane);
        addAButton(PromotablePiece.KNIGHT,"knight", pane);
    }

    private void addAButton(final PromotablePiece promotablePiece,String name, Container container) {
        // new button and set size
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(150, 150));

        // load button icon image
        try {
            Image buttonImg = ImageIO.read(new File("media/" + name + "Black.png"));
            button.setIcon(new ImageIcon(buttonImg));
        } catch (IOException ex) {
			ex.printStackTrace();
        }

        // set button alignment
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // set button response
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                promote(promotablePiece);
                frame.dispose();
            }
        });

        // add button to container
        container.add(button);
    }



    // create the frame GUI to contain the buttons

    private void createAndShowGUI() {
        //Create and set up the window

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Set up the content pane
        addComponentsToPane(frame.getContentPane());

        //Set JFrame Size
        frame.setSize(1000,1000);

        // set Frame Location relative
        frame.setLocationRelativeTo(null);

        //Display the window
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void run() {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}


