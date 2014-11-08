package forms;

import BasicChess.BasicChess;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.List;

import RandomChess.RandomChess;
import GrandChess.GrandChess;
import main.*;
import networking.GameLobby;

/**
 * Created by jc4512 on 15/10/14.
 */
public class frmVariantChooser {
    private JPanel panel1;
    private JButton btnAcceptVariation;
    private JList lstVariationPicker;
    private DefaultListModel<String> lstVariationPickerModel;
    private JTextArea txtVariationRulesDisplay;
    private boolean visibility = false;

    private ChessVariant selectedVariant = null;

    public static GameMode currentGameMode;
    public static GameLauncher currentGameLauncher;
    private static frmVariantChooser instance;
    private List<ChessVariant> variants;

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
        initialiseChessVariantsList();

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

                selectedVariant = variants.get(lstVariationPicker.getSelectedIndex());

                if (currentGameMode == GameMode.SINGLE_PLAYER ||
                        currentGameMode == GameMode.MULTIPLAYER_LOCAL) {
                    currentGameLauncher = new OfflineGameLauncher(selectedVariant);
                    currentGameLauncher.launch();
                } else if (currentGameMode == GameMode.MULTIPLAYER_ONLINE) {
                    GameLobby.getInstance().createLocalOpenGame(selectedVariant);
                    currentGameLauncher = new OnlineGameLauncher(selectedVariant);
                }

            }
        });
        lstVariationPicker.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                toggleBtnAcceptVariation();
                txtVariationRulesDisplay.setText(variants.get(
                     lstVariationPicker.getSelectedIndex()).getDescription());
            }
        });

        toggleBtnAcceptVariation();
    }

    private void toggleBtnAcceptVariation() {
        btnAcceptVariation.setEnabled(!lstVariationPicker.isSelectionEmpty());
    }

    private void initialiseChessVariantsList() {
        variants = ChessVariantManager.getInstance().getAllVariants();

        for (ChessVariant variant : variants) {
            lstVariationPickerModel.addElement(variant.getName());
        }
    }


    private void createUIComponents() {
        lstVariationPickerModel = new DefaultListModel<String>();
        lstVariationPicker = new JList(lstVariationPickerModel);
    }
}
