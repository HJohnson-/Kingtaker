package forms;

import BasicChess.BasicChess;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import RandomChess.RandomChess;
import GrandChess.GrandChess;
import main.ChessVariant;
import main.GameMode;

/**
 * Created by jc4512 on 15/10/14.
 */
public class frmVariantChooser {
    private JPanel panel1;
    private JButton btnAcceptVariation;
    private JList lstVariationPicker;
    private JTextArea txtVariationRulesDisplay;
    private boolean visibility = false;

    private ChessVariant selectedVariant = null;

    public static GameMode currentGameMode;
    private static frmVariantChooser instance;

    public static void showInstance() {
        if (instance == null || !instance.visibility) {
            instance = new frmVariantChooser();
        }
    }

    public static boolean isVisible() {
        return instance != null && instance.visibility;
    }

    public static ChessVariant getSelectedVariant() {
        return (instance != null) ? instance.selectedVariant : null;
    }

    private frmVariantChooser() {
        final JFrame frame = new JFrame("frmVariantChooser");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        visibility = true;

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                visibility = false;
            }
        });
        btnAcceptVariation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //Close form on accept and start game
                frame.setVisible(false);
                visibility = false;

                //TODO: needs refactoring - perhaps GameLauncher class. Need to consult group on this.
                switch (lstVariationPicker.getSelectedIndex()) {
                    case 0 :
                        BasicChess bc = new BasicChess();
                        selectedVariant = bc;
                        break;
                    case 1:
                        RandomChess rc = new RandomChess();
                        selectedVariant = rc;
                        break;
                    case 2:
                        GrandChess gc = new GrandChess();
                        selectedVariant = gc;
                        break;
                }
                if (currentGameMode == GameMode.SINGLE_PLAYER ||
                        currentGameMode == GameMode.MULTIPLAYER_LOCAL) {
                    selectedVariant.drawBoard();
                } else if (currentGameMode == GameMode.MULTIPLAYER_ONLINE) {

                }

            }
        });
        lstVariationPicker.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                toggleBtnAcceptVariation();
            }
        });

        toggleBtnAcceptVariation();
    }

    private void toggleBtnAcceptVariation() {
        btnAcceptVariation.setEnabled(!lstVariationPicker.isSelectionEmpty());
    }

    private void createUIComponents() {
        //TODO need to be able to enumerate variants
        lstVariationPicker = new JList(new String[]{"Standard Chess", "Random Chess", "Grand Chess"});
    }


}
