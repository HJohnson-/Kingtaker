package main;

import javax.swing.*;
import BasicChess.BasicChess;

import BasicChess.BasicBoard;
import BasicChess.BasicDecoder;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import GrandChess.GrandChess;

/**
 * Primitive main function for running tests until a menu is added
 */
public class KingtakerMain extends JFrame {

    public static void main(String[] args) {

		//String myState = "#$T:10~V:Basic$|N:Rook~L:0~X:0~Y:0~T:Black~M:||N:Bishop~L:0~X:0~Y:2~T:Black~M:||N:Queen~L:0~X:0~Y:3~T:Black~M:||N:King~L:0~X:0~Y:4~T:Black~M:||N:Bishop~L:0~X:0~Y:5~T:Black~M:||N:Knight~L:0~X:0~Y:6~T:Black~M:||N:Rook~L:0~X:0~Y:7~T:Black~M:||N:Pawn~L:0~X:1~Y:0~T:Black~M:F||N:Pawn~L:0~X:1~Y:2~T:Black~M:F||N:Pawn~L:0~X:1~Y:3~T:Black~M:F||N:Pawn~L:0~X:1~Y:5~T:Black~M:F||N:Pawn~L:0~X:1~Y:6~T:Black~M:F||N:Knight~L:8~X:2~Y:2~T:Black~M:||N:Pawn~L:2~X:3~Y:1~T:Black~M:T||N:Pawn~L:4~X:3~Y:4~T:Black~M:T||N:Pawn~L:6~X:3~Y:7~T:Black~M:T||N:Pawn~L:1~X:4~Y:1~T:White~M:T||N:Pawn~L:3~X:4~Y:3~T:White~M:T||N:Pawn~L:5~X:4~Y:6~T:White~M:T||N:Queen~L:9~X:5~Y:3~T:White~M:||N:Knight~L:7~X:5~Y:5~T:White~M:||N:Pawn~L:0~X:6~Y:0~T:White~M:F||N:Pawn~L:0~X:6~Y:2~T:White~M:F||N:Pawn~L:0~X:6~Y:4~T:White~M:F||N:Pawn~L:0~X:6~Y:5~T:White~M:F||N:Pawn~L:0~X:6~Y:7~T:White~M:F||N:Rook~L:0~X:7~Y:0~T:White~M:||N:Knight~L:0~X:7~Y:1~T:White~M:||N:Bishop~L:0~X:7~Y:2~T:White~M:||N:King~L:0~X:7~Y:4~T:White~M:||N:Bishop~L:0~X:7~Y:5~T:White~M:||N:Rook~L:0~X:7~Y:7~T:White~M:|#";
        //BasicChess bc = new BasicChess(new GameController( new BasicBoard(), new BasicDecoder(), myState));

		//BasicChess bc = new BasicChess();
		//bc.drawBoard();

		GrandChess gc = new GrandChess();
        gc.drawBoard();
    }

}
