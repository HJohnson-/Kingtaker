package networking;

/**
 * Created by jc4512 on 09/11/14.
 */
public interface IMessageSender {

    // If waitForResponse = false, the sender will not attempt to read a
    // reply message back from the receiver (on a TCP ACK).

    public String sendMessage(String msg, boolean waitForResponse);

    public String sendMessage(String msg, boolean waitForResponse, int timeout);

}
