package main;

import javax.swing.*;
import BasicChess.BasicChess;
import ClientConnection.MessageListener;
import ClientConnection.OpponentMessageSender;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by hj1012 on 15/10/14.
 */
public class KingtakerMain extends JFrame {

    public static void main(String[] args) {

        BasicChess bc = new BasicChess();
        bc.game.getBoard().initializeBoard();
		//System.out.println(bc.board.getPiece(new Location(0,0)).toString());
        bc.drawBoard();
    }

}
