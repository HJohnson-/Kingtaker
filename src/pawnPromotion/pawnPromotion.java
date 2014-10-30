//package pawnPromotion;
//
//import graphics.GraphicsControl;
//import pieces.ChessPiece;
//
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.File;
//import java.io.IOException;
//import javax.imageio.ImageIO;
//import javax.swing.*;
//
///**
// * Created by daniel on 14/10/28.
// */
//
//public class pawnPromotion implements  Runnable{
//
//    private ChessPiece promotedPiece;
//    private ChessPiece pawn;
//
//
//    public  pawnPromotion (ChessPiece pawn){
//        this.pawn = pawn;
//    }
//
//    public void promote(ChessPiece pawn, String promoteName) {
//
//        this.promotedPiece = pawn;
//
//
//        promotedPiece.board.clearSpace(promotedPiece.cords);
//        promotedPiece.board.placePiece(promotedPiece.cords, promotedPiece);
//
//        promotedPiece.graphics = new GraphicsControl(promotedPiece.cords, promotedPiece.cords);
//
//    }
//
//
//    //   A board GUI for choosing a promotion piece
//
//        public  void addComponentsToPane(Container pane) {
//            pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
//            addAButton("queen", pane);
//            addAButton("rook", pane);
//            addAButton("bishop", pane);
//            addAButton("knight", pane);
//        }
//
//        private  void addAButton(final String name, Container container) {
//            // new button and set size
//            JButton button = new JButton();
//            button.setPreferredSize(new Dimension(150, 150));
//
//            // load button icon image
//            try {
//                Image buttonImg = ImageIO.read(new File("media/" + name + "Black.png"));
//                button.setIcon(new ImageIcon(buttonImg));
//            } catch (IOException ex) {
//            }
//
//            // set button alignment
//            button.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//            // set button response
//            button.addActionListener(new ActionListener()
//            {
//                public void actionPerformed(ActionEvent e)
//                {
//                    promote(pawn,name);
//                System.out.println("pawn :" + pawn + "     " + name);
//                }
//            });
//
//            // add button to container
//            container.add(button);
//        }
//
//
//
//    // create the frame GUI to contain the buttons
//
//        private void createAndShowGUI() {
//            //Create and set up the window
//            JFrame frame = new JFrame(" Pawn Promotion ");
//            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//
//            //Set up the content pane
//            addComponentsToPane(frame.getContentPane());
//
//            //Set JFrame Size
//            frame.setSize(1000,1000);
//
//            // set Frame Location relative
//            frame.setLocationRelativeTo(null);
//
//            //Display the window
//            frame.pack();
//            frame.setVisible(true);
//        }
//
//        public void run() {
//            //Schedule a job for the event-dispatching thread:
//            //creating and showing this application's GUI.
//            javax.swing.SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                    createAndShowGUI();
//                }
//            });
//        }
//    }
//
//
