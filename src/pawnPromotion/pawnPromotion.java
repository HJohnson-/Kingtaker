package pawnPromotion;

import BasicChess.*;
import graphics.GraphicsControl;
import main.Location;
import pieces.ChessPiece;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Created by daniel on 14/10/28.
 */



public class pawnPromotion implements Runnable {

    JFrame frame = new JFrame("Pawn Promotion");

    public enum PromoteType{QUEEN,ROOK,BISHOP,KNIGHT}

    private ChessPiece promotedPiece;
    private ChessPiece pawn;

    // constructor
    public pawnPromotion (ChessPiece pawn){
        this.pawn = pawn;
    }

    // chess piece promotion
    public void promote(ChessPiece pawn, PromoteType promoteType) {

        //System.out.println("pawn cords" + pawn.cords+" clear space...");

        switch (promoteType) {
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
        if (promotedPiece.board.doDrawing) {
            promotedPiece.graphics.givePanel(pawn.graphics.panel);
            promotedPiece.graphics.panel.recalculateCellSize();
            promotedPiece.graphics.panel.repaint();
        }

    }


    //   A board GUI for choosing a promotion piece

    public void addComponentsToPane(Container pane) {
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        addAButton(PromoteType.QUEEN, "queen",pane);
        addAButton(PromoteType.ROOK,"rook", pane);
        addAButton(PromoteType.BISHOP,"bishop", pane);
        addAButton(PromoteType.KNIGHT,"knight", pane);
    }

    private void addAButton(final PromoteType promoteType,String name, Container container) {
        // new button and set size
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(150, 150));

        // load button icon image
        try {
            Image buttonImg = ImageIO.read(new File("media/" + name + "Black.png"));
            button.setIcon(new ImageIcon(buttonImg));
        } catch (IOException ex) {
        }

        // set button alignment
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // set button response
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                promote(pawn,promoteType);
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


