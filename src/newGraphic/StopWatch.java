package newGraphic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by daniel on 14/11/28.
 */

public class StopWatch implements Runnable {
    public StopWatch(){
        super();
    }

    public static final JLabel time = new JLabel();

    private int wCurrentSecond = 0;
    private int wMinute = 0;
    private int wHour = 0;

    private int bCurrentSecond = 0;
    private int bMinute = 0;
    private int bHour = 0;

    public static int isRunning =0;

    JFrame clockFrame = new JFrame("clock");
    JPanel clockPanel = new JPanel();
    JPanel clockPanel2 = new JPanel();
    JButton stopTimer = new JButton("stop");
    JButton startTimer = new JButton("start");
    public Boolean isWhite = Boolean.TRUE;


    public JPanel buildStopWatch() {
//        clockFrame.setSize(500, 500);
        time.setSize(800, 800);

//        init_buttons();
//        clockPanel.add(stopTimer);
//        clockPanel.add(startTimer);

        clockPanel2.add(time);

//        clockFrame.getContentPane().add(clockPanel,BorderLayout.SOUTH);
//        clockFrame.setMinimumSize(new Dimension(20, 20));

//        clockFrame.getContentPane().add(clockPanel2,BorderLayout.NORTH);
        clockPanel2.setMinimumSize(new Dimension(800, 800));



//        clockFrame.pack();
//        clockFrame.setVisible(true);
//    if(isRunning==0) {
//        this.run();
//    isRunning =1;
//    }
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
                System.out.println("stop!" + isWhite);
            }
        });

        stopTimer.setSize(1, 1);
        startTimer.setSize(1, 1);

    }

    @Override
    public void run() {

        while (true) {

            while (isWhite) {

                if (wCurrentSecond == 60) {
                    calTime();
                    System.out.println("reset");
                }
                time.setText(String.format("<html>White : %02d:%02d:%02d <br> Black : %02d:%02d:%02d<html>", wHour, wMinute, wCurrentSecond, bHour, bMinute, bCurrentSecond));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wCurrentSecond++;
            }

            while(!isWhite) {

                if (bCurrentSecond == 60) {
                    calTime();
                    System.out.println("reset");
                }
                time.setText(String.format("<html>White : %02d:%02d:%02d <br> Black : %02d:%02d:%02d<html>", wHour, wMinute, wCurrentSecond, bHour, bMinute, bCurrentSecond));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                bCurrentSecond++;
            }

        }
    }

}