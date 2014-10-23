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
        MessageListener messageListener = new MessageListener();
        Thread messageListenerThread = new Thread(messageListener);
        messageListenerThread.start();

        InetAddress ip = null;
        try {
            ip = InetAddress.getByName("146.169.53.2");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        OpponentMessageSender opponentMessageSender = new OpponentMessageSender(ip);
        for (int i = 0; i < 1000; i++) {
            opponentMessageSender.sendMessage("test " + i);
        }

        BasicChess bc = new BasicChess();
        bc.initializeBoard();
		//System.out.println(bc.board.getPiece(new Location(0,0)).toString());
        bc.drawBoard();
    }

}
