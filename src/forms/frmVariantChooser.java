package forms;

import BasicChess.BasicChess;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

/**
 * Created by jc4512 on 15/10/14.
 */
public class frmVariantChooser {
    private JPanel panel1;
    private JButton btnAcceptVariation;
    private JList lstVariationPicker;
    private JTextArea txtVariationRulesDisplay;

    private JFrame parentFrame;

    public frmVariantChooser(final JFrame parentFrame) {
        this.parentFrame = parentFrame;

        final JFrame frame = new JFrame("frmVariantChooser");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(parentFrame.getLocation());

        //Called when the variation picker form is closed - reopens last form if hidden
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                parentFrame.setVisible(true);
            }
        });

        btnAcceptVariation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //Close form on accept and start game
                frame.setVisible(false);

                switch (lstVariationPicker.getSelectedIndex()) {
                    case 0 :
                        BasicChess bc = new BasicChess();
                        bc.game.getBoard().initializeBoard();
                        bc.drawBoard();
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
        lstVariationPicker = new JList(new String[]{"Standard Chess"});
    }


}
