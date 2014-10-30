package main;

import javax.swing.*;
import BasicChess.BasicChess;

/**
 * Primitive main function for running tests until a menu is added
 */
public class KingtakerMain extends JFrame {

    public static void main(String[] args) {

        BasicChess bc = new BasicChess();
        bc.game.getBoard().initializeBoard();
		//System.out.println(bc.board.getPiece(new Location(0,0)).toString());
        bc.drawBoard();
    }

}
