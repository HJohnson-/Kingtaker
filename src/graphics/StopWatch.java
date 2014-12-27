package graphics;

import main.*;
import networking.GameLobby;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopWatch extends Thread {
    public static final String STOPWATCH_DISPLAY_HTML =
        "<HTML><html>\n" +
        "<table width=\"120\">\n" +
        " <tr>\n" +
        "  <td valign=top style='background:#C4BC96'>\n" +
        "  <p>%s %s<br>%02d:%02d:%02d</p>\n" +
        "  </td>\n" +
        " </tr>\n" +
        " <tr>\n" +
        "  <td valign=top style='background:black'>\n" +
        "  <p><span style='color:white'>%s %s<br>%02d:%02d:%02d</span></p>\n" +
        "  </td>\n" +
        " </tr>\n" +
        "</table>\n" +
        "</html></HTML>";

    private final String AI_NAME = "KingTaker AI";
    private final String NUMAN_NAME = "You";
    private final String TURN_INDICATOR = "&#x21D2;";

    public static final JLabel time = new JLabel();

    private int wSecond = 0;
    private int wMinute = 0;
    private int wHour = 0;

    private int bSecond = 0;
    private int bMinute = 0;
    private int bHour = 0;

    public static boolean isRunning = true;
    private boolean interrupted = false;

    private JFrame clockFrame = new JFrame("clock");
    private JPanel clockPanel = new JPanel();
    public JPanel clockPanel2 = new JPanel();
    private JButton stopTimer = new JButton("stop");
    private JButton startTimer = new JButton("start");
    public Boolean isWhite = Boolean.TRUE;

    public String wPlayerName;
    public String bPlayerName;

    public StopWatch(){
        super();
    }

    public JPanel buildStopWatch(Board board) {
        GameController gc = board.getController();

        switch (gc.gameMode) {
            case SINGLE_PLAYER:
                wPlayerName = gc.playerIsWhite ? NUMAN_NAME : AI_NAME;
                bPlayerName = !gc.playerIsWhite ? NUMAN_NAME : AI_NAME;
                break;
            case MULTIPLAYER_LOCAL:
                wPlayerName = "White";
                bPlayerName = "Black";
                break;
            case MULTIPLAYER_ONLINE:
                String localName =
                        GameLobby.getInstance().getUser().getUserString();
                String remoteName = ((OnlineGameLauncher)
                        GameLauncher.currentGameLauncher).getOpponentString();
                wPlayerName = gc.playerIsWhite ? localName : remoteName;
                bPlayerName = !gc.playerIsWhite ? localName : remoteName;
                break;
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
            wSecond = 0;
            wMinute++;
            if (wMinute == 60) {
                wHour++;
                wMinute = 0;
            }
        } else {
            bSecond = 0;
            bMinute++;
            if (bMinute == 60) {
                bHour++;
                bMinute = 0;
            }
        }
    }

    public void resetTime() {
        wSecond = 0;
        wMinute = 0;
        wHour = 0;

        bSecond = 0;
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

            while (isWhite && isRunning) {

                if (wSecond == 60) {
                    calTime();
                }
                time.setText(String.format(STOPWATCH_DISPLAY_HTML,
                        TURN_INDICATOR, wPlayerName, wHour, wMinute, wSecond,
                        "", bPlayerName, bHour, bMinute, bSecond));

                try {
                    this.sleep(1000);
                    if (!isRunning) {
                        this.currentThread().interrupt();
                    }
                } catch (InterruptedException e) {
                    interrupted = true;
                    return;
                }
                wSecond++;
            }
            if (interrupted) {
                resetTime();
                interrupted = false;
            }

            while(!isWhite && isRunning) {

                if (bSecond == 60) {
                    calTime();
                }

                time.setText(String.format(STOPWATCH_DISPLAY_HTML,
                        "", wPlayerName, wHour, wMinute, wSecond,
                        TURN_INDICATOR, bPlayerName, bHour, bMinute, bSecond));

                try {
                   this.sleep(1000);
                    if (!isRunning) {
                        this.currentThread().interrupt();
                    }
                } catch (InterruptedException e) {
                    interrupted = true;
                    return;
                }

                bSecond++;

            }

        }

        if (interrupted){
            resetTime();
            interrupted = false;
        }

    }

}