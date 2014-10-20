package ClientConnection;

import java.io.Serializable;

/**
 * Created by daniel on 14/10/20.
 */
public class ClientRequestSender implements Serializable {

    int requestType = -1;
    String requestMessage = "Message Missing !!";

    public void process(int requestType, String requestMessage) {

        this.requestType = requestType;
        this.requestMessage = requestMessage;

    }

    public class RequestType {
        public int LOGIN = 1;
        public int MATCHING = 2;
        public int CHESS_MOVEMENT = 3;
    }



}
