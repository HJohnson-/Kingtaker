package graphics;

import javax.swing.*;
import java.awt.*;

public class ProgressBarForAI {

    /** The progress bar. */
    public JProgressBar progressBar;



    public JProgressBar build(int min, int max) {
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, min,max) {
            public Dimension getPreferredSize() {
                return new Dimension(150, super.getPreferredSize().height);
            }
        };

        progressBar.getAccessibleContext().setAccessibleName(" AI Calculation");
        progressBar.setStringPainted(true);//* add by Jack Jiang for test

//        JPanel p1 = new JPanel();
//        p1.add(progressBar);
//        return p1;
    return progressBar;

    }

    JProgressBar pbIndeterminate = new JProgressBar()
    {
        public Dimension getPreferredSize() {
//			    	return new Dimension(15,300 );
            return new Dimension(100, 15);
        }
    };

    public void aiCalculation() {

                if(progressBar.getValue() < progressBar.getMaximum()) {
                    progressBar.setValue(progressBar.getValue() + 1);
                }
    }
}