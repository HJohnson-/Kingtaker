package main;

import javax.swing.*;
import BasicChess.BasicChess;

/**
 * Created by hj1012 on 15/10/14.
 */
public class KingtakerMain extends JFrame {

    public static void main(String[] args) {
        ChessVariant bc = new BasicChess();
		//System.out.println(bc.board.getPiece(new Location(0,0)).toString());
        bc.drawBoard();
    }

}
