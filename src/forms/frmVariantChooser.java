package forms;

import main.*;
import networking.GameLobby;
import variants.VariantLoader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Created by jc4512 on 15/10/14.
 */
public class frmVariantChooser {
    private JPanel panel1;
    private JButton btnAcceptVariation;
    private JList lstVariationPicker;
    private DefaultListModel lstVariationPickerModel;
    private JTextArea txtVariationRulesDisplay;
    private boolean visibility = false;

    private ChessVariant selectedVariant = null;

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
        final JFrame frame = new JFrame("Choose a variant...");
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

                if (GameMode.currentGameMode == GameMode.SINGLE_PLAYER ||
                        GameMode.currentGameMode == GameMode.MULTIPLAYER_LOCAL) {
                    GameLauncher.lastGameLauncher = new OfflineGameLauncher(selectedVariant, GameMode.currentGameMode);
                    GameLauncher.lastGameLauncher.launch();
                } else if (GameMode.currentGameMode == GameMode.MULTIPLAYER_ONLINE) {
                    GameLauncher.onlineGameLauncher = new OnlineGameLauncher(selectedVariant);
                    GameLauncher.lastGameLauncher = GameLauncher.onlineGameLauncher;
                    GameLobby.getInstance().createLocalOpenGame(selectedVariant);
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
        //use this method that 'loads' java files from the current directory.
        //variants = VariantFactory.getInstance().getAllVariants();
        variants = VariantLoader.getInstance().getAllVariants();

        for (ChessVariant variant : variants) {
            lstVariationPickerModel.addElement(variant.getName());
        }
    }

    @SuppressWarnings("unchecked")
    private void createUIComponents() {
        //Do not make lstVariationPickerModel a DefaultListModel<String>
        //It breaks compatibility with Java 1.6.
        lstVariationPickerModel = new DefaultListModel();
        lstVariationPicker = new JList(lstVariationPickerModel);
    }
}
