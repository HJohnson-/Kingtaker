package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Created by hj1012 on 15/10/14.
 */
class Surface extends JPanel {

    private Rectangle2D rect;

    public Surface() {
        initSurface();
    }

    private void initSurface() {
        this.addMouseListener(new HitTestAdapter());

        rect = new Rectangle2D.Float(0f, 50f, 50f, 50f);
    }


    private void doDrawing(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(255, 255, 255));
        for (int x = 0; x < 400; x += 100) {
            for (int y = 0; y < 400; y += 100) {
                g2.fillRect(x, y, 50, 50);
                g2.fillRect(x + 50, y + 50, 50, 50);
            }
        }

        g2.setColor(new Color(0, 0, 0));
        for (int x = 50; x < 450; x += 100) {
            for (int y = 0; y < 400; y += 100) {
                g2.fillRect(x, y, 50, 50);
                g2.fillRect(x - 50, y + 50, 50, 50);
            }
        }

        g2.setColor(new Color(255, 0, 241));
        g2.fill(rect);

    }

    class RectRunnable implements Runnable {

        private Thread runner;

        public RectRunnable() {
            initThread();
        }

        private void initThread() {
            runner = new Thread(this);
            runner.start();
        }

        @Override
        public void run() {
            double y = rect.getY();
            for (double c = 0; c <= 50; c++) {
                rect.setRect(0, y + c, 50, 50);
                repaint();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.exit(1);
                }

            }
        }

    }

    class HitTestAdapter extends MouseAdapter {

        private RectRunnable rectAnimator;

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            if (rect.contains(x, y)) {
                rectAnimator = new RectRunnable();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

}

public class KingtakerMain extends JFrame {

    public KingtakerMain() {
        initUI();
    }

    private void initUI() {
        setTitle("Chess");

        add(new Surface());

        setSize(400, 430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                KingtakerMain km = new KingtakerMain();
                km.setVisible(true);
            }
        });
    }

}
