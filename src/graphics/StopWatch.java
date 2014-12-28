package graphics;

import main.Board;
import main.GameMode;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by daniel on 14/11/28.
 */

public class StopWatch extends Thread {
    public StopWatch(){
        super();
    }

    public static final JLabel time = new JLabel();

//    public ImageIcon background =new ImageIcon("media/stopWatchLable.png");

    private int wCurrentSecond = 0;
    private int wMinute = 0;
    private int wHour = 0;

    private int bCurrentSecond = 0;
    private int bMinute = 0;
    private int bHour = 0;

    public static boolean isRunning =true;
    private boolean interrupted = false;

    private JFrame clockFrame = new JFrame("clock");
    private JPanel clockPanel = new JPanel();
    public JPanel clockPanel2 = new JPanel();
    private JButton stopTimer = new JButton("stop");
    private JButton startTimer = new JButton("start");
    public Boolean isWhite = Boolean.TRUE;

    public String playerA ;
    public String playerB ;
    public String playerAFontColour= "lime";
    public String playerBFontColour= "black";


    public JPanel buildStopWatch(Board board) {

        if (board.getController().gameMode == GameMode.SINGLE_PLAYER) {
            playerA = "WHITE";
            playerB = "YOU";
        }else{
            if (board.getController().gameMode == GameMode.MULTIPLAYER_LOCAL) {
                playerA = "WHITE";
                playerB = "BLACK";
            }else{
                if (board.getController().gameMode == GameMode.MULTIPLAYER_ONLINE) {
                    playerA = "WHITE";
                    playerB = "BLACK";
                }
            }
        }

        time.setBackground(GraphicsTools.BOARD_WHITE);
        time.setOpaque(true);
        time.setHorizontalTextPosition(JLabel.CENTER);
        time.setVerticalTextPosition(JLabel.CENTER);

        clockPanel2.setLayout(new BorderLayout());
        clockPanel2.setOpaque(true);
        clockPanel2.setBackground(GraphicsTools.BOARD_WHITE);
        clockPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        clockPanel2.add(time, BorderLayout.CENTER);

        return clockPanel2;
    }


    private void calTime() {
        if (isWhite) {
            wCurrentSecond = 0;
            wMinute++;
            if (wMinute == 60) {
                wHour++;
                wMinute = 0;
            }
        } else {
            bCurrentSecond = 0;
            bMinute++;
            if (bMinute == 60) {
                bHour++;
                bMinute = 0;
            }
        }
    }

    public void resetTime() {
        wCurrentSecond = 0;
        wMinute = 0;
        wHour = 0;

        bCurrentSecond = 0;
        bMinute = 0;
        bHour = 0;
    }

    public void init_buttons() {

        startTimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isWhite = Boolean.TRUE;
                time.setBackground(Color.green);
            }
        });

        stopTimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isWhite = Boolean.FALSE;
                time.setBackground(Color.red);
            }
        });

        stopTimer.setSize(1, 1);
        startTimer.setSize(1, 1);

    }

    @Override
    public void run() {
        resetTime();

        while (isRunning) {

            while (isWhite&&isRunning) {

                if (wCurrentSecond == 60) {
                    calTime();
                }
                time.setText(String.format("<html> <font color=%s> %s : %02d:%02d:%02d </font> <html> <br> <html> <font color=%s>%s : %02d:%02d:%02d </font> <html>",playerAFontColour,playerA, wHour, wMinute, wCurrentSecond,playerBFontColour, playerB,bHour, bMinute, bCurrentSecond));
                try {
                    this.sleep(1000);
                    if(!isRunning) {

                        this.currentThread().interrupt();
                    }
                } catch (InterruptedException e) {
                    interrupted=true;
                    return;
                }
                wCurrentSecond++;
            }
            if(interrupted){
                resetTime();
                interrupted=false;
            }

            while(!isWhite&&isRunning) {

                if (bCurrentSecond == 60) {
                    calTime();
                }

                time.setText(String.format("<html> <font color=%s> %s : %02d:%02d:%02d </font> <html> <br> <html> <font color=%s>%s : %02d:%02d:%02d </font> <html>",playerAFontColour,playerA, wHour, wMinute, wCurrentSecond,playerBFontColour, playerB,bHour, bMinute, bCurrentSecond));
                try {
                   this.sleep(1000);
                    if(!isRunning) {
                        this.currentThread().interrupt();

                    }
                } catch (InterruptedException e) {
                    interrupted=true;
                    return;
                }

                bCurrentSecond++;

            }

        }
        if(interrupted){
            resetTime();
            interrupted=false;
        }

    }

}