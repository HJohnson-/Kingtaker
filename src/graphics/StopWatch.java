package graphics;

import javax.swing.*;
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


    public JPanel buildStopWatch() {

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
                time.setText(String.format("<html>White : %02d:%02d:%02d <br> Black : %02d:%02d:%02d<html>", wHour, wMinute, wCurrentSecond, bHour, bMinute, bCurrentSecond));
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
                time.setText(String.format("<html>White : %02d:%02d:%02d <br> Black : %02d:%02d:%02d<html>", wHour, wMinute, wCurrentSecond, bHour, bMinute, bCurrentSecond));
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