package ClientConnection;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by daniel on 14/10/20.
 */


public class ClientRequestSender implements Serializable {

    public enum ClientCommandCode {
        GET_GAME_LIST,
        AUTHENTICATE_USER,
        CREATE_GAME,
        REMOVE_GAME,
        REPORT_PLAYER
    }

    String requestType = "-1";
    String requestMessage = "Message Missing !!";
    ConnectionToServer connection;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;

    public void send(String requestType, String requestMessage, ConnectionToServer connection) throws Exception {


        this.requestType = requestType;
        this.requestMessage = requestMessage;
        this.connection = connection;
        this.outputStream = connection.getOutputStream();
        this.inputStream = connection.getInputStream();

        // build up our sending Message
        StringBuilder sb = new StringBuilder();
        sb.append(requestType);
        sb.append(",");
        sb.append(requestMessage);
        String sendingMessage = sb.toString();

        //write to output stream
        outputStream.writeObject(sendingMessage);
        System.out.println("Sending Message" + sendingMessage);

        // flush
        outputStream.flush();

        System.out.println("Message sent");

        //Message receive from server response
        String input = (String) inputStream.readObject();

        System.out.println("Reaction to Server Reply :" + input);

    }


}
