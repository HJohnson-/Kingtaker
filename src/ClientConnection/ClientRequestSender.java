package ClientConnection;

import java.io.*;
import java.sql.Connection;

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
    private BufferedReader input;
    private PrintWriter output;

    public void send(String requestType, String requestMessage, ConnectionToServer connection) throws Exception {


        this.requestType = requestType;
        this.requestMessage = requestMessage;
        this.connection = connection;

        // set output writer
        this.output =  new PrintWriter(connection.getSocket().getOutputStream(), true);

        // set input reader
        this.input = new BufferedReader(new InputStreamReader(connection.getSocket().getInputStream()));

        // build up our sending Message
        StringBuilder sb = new StringBuilder();
        sb.append(requestType);
        sb.append(",");
        sb.append(requestMessage);
        String sendingMessage = sb.toString();

        //write to output stream
        System.out.println("Sending Message" + sendingMessage);
        output.write(sendingMessage);
        System.out.println("Message sent");

        //Message receive from server response
        String response = input.readLine();

        System.out.println("Reaction to Server Reply :" + response);

    }


}
