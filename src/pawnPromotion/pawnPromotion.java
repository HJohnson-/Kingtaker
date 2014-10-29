package pawnPromotion;

import graphics.GraphicsControl;
import main.PieceType;
import pieces.ChessPiece;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Created by daniel on 14/10/28.
 */
public class pawnPromotion implements  Runnable{

    private ChessPiece promotedPiece;

    public boolean promote(ChessPiece pawn, PieceType promoteType, String promoteImg) {

        this.promotedPiece = pawn;

        promotedPiece.type = promoteType;
        promotedPiece.img = promoteImg;


        promotedPiece.board.clearSpace(promotedPiece.cords);
        promotedPiece.board.placePiece(promotedPiece.cords, promotedPiece);

        promotedPiece.graphics = new GraphicsControl(promotedPiece.cords, promotedPiece.cords);

        return true;
    }



        public  void addComponentsToPane(Container pane) {
            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
            addAButton("queen", pane);
            addAButton("rook", pane);
            addAButton("bishop", pane);
            addAButton("knight", pane);
        }

        private  void addAButton(String image, Container container) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(150, 150));

            try {
                Image buttonImg = ImageIO.read(new File("media/" + image + "Black.png"));
                button.setIcon(new ImageIcon(buttonImg));
            } catch (IOException ex) {
            }
            System.out.println(
                    "Loading button image failed!!!!!!!!!!!!!!!!!!!!!s");
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(button);
        }

        /**
         * Create the GUI and show it.  For thread safety,
         * this method should be invoked from the
         * event-dispatching thread.
         */
        private void createAndShowGUI() {
            //Create and set up the window.
            JFrame frame = new JFrame(" Pawn Promotion ");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            //Set up the content pane.
            addComponentsToPane(frame.getContentPane());

            //Set JFrame Size
            frame.setSize(1000,1000);

            frame.setLocationRelativeTo(null);

            //Display the window.
            frame.pack();
            frame.setVisible(true);
        }

        public void run() {
            //Schedule a job for the event-dispatching thread:
            //creating and showing this application's GUI.
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });
        }
    }


