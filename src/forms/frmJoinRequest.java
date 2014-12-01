package forms;

import main.OnlineGameLauncher;
import networking.GameLobby;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by jc4512 on 01/12/14.
 */
public class frmJoinRequest {
    private JButton btnAccept;
    private JButton btnReject;
    private JLabel lblInfo;
    private JLabel lblJoiner;
    private JPanel panel1;
    private JFrame frame = new JFrame();

    private long creationTime = System.currentTimeMillis();
    private final String TITLE = "Request to Join - %d seconds left";
    private final String LBL_JOINER_TEXT = "%s (%d)";

    public frmJoinRequest(String joinerName, int rating, final OnlineGameLauncher launcher) {
        lblJoiner.setText(String.format(LBL_JOINER_TEXT, joinerName, rating));

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                launcher.rejectJoinToGame();
            }
        });
        btnAccept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    launcher.acceptJoinToGame();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnReject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                launcher.rejectJoinToGame();
            }
        });

        Timer joinTimeoutTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("joinTimeoutTimer" + System.currentTimeMillis());
                long msPassed = System.currentTimeMillis() - creationTime;
                if (msPassed < GameLobby.JOIN_GAME_TIMEOUT_MS) {
                    frame.setTitle(String.format(TITLE, (int) (JOIN_TIMEOUT_MS - msPassed) / 1000));
                } else {
                    launcher.rejectJoinToGame();
                    frame.dispose();
                }
            }
        });
        joinTimeoutTimer.start();

        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
