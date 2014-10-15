package forms;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by jc4512 on 15/10/14.
 */
public class frmVariantChooser {
    private JPanel panel1;
    private JButton btnAcceptVariation;
    private JList lstVariationPicker;
    private JTextArea txtVariationRulesDisplay;


    public frmVariantChooser() {
        JFrame frame = new JFrame("frmVariantChooser");
        frame.setContentPane(new frmVariantChooser().panel1);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

        btnAcceptVariation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //Close form

            }
        });
        lstVariationPicker.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {

            }
        });
    }
}
