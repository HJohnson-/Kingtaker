package networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TimedHostChecker implements Runnable {
    private final int TIMER_INTERVAL_MS = 5000;
    private final int CONNECT_TIMEOUT_MS = 10000;

    private Thread thread;
    private InetAddress ip;
    private int port;
    private boolean reachable = true;

    public TimedHostChecker(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
        thread = new Thread(this);
        thread.start();
    }

    public boolean isOnline() {
        return reachable;
    }

    @Override
    public void run() {
        while (true) {
            try {
                final Socket testSocket = new Socket();
                testSocket.connect(new InetSocketAddress(ip, port), CONNECT_TIMEOUT_MS);
            } catch (IOException e) {
                reachable = false;
                return;
            }

            try {
                Thread.sleep(TIMER_INTERVAL_MS);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void stop() {
        thread.interrupt();
    }
}
