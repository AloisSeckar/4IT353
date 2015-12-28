package gui;

import client.Client;
import javax.swing.JTextArea;

/**
 *  ServerCom - thread handling server communication
 *
 * @author Ellrohir
 * @version 1.0
 */
public class ServerCom extends Thread {
    
    // ************************** \\
    // *        CONSTANTS       * \\
    // ************************** \\

    // ************************** \\
    // *       PROPERTIES       * \\
    // ************************** \\
    
    /**
     * Text area that will be updated with received server commands.
     */
    private JTextArea status;
    
    /**
     * Client that will handle received server commands.
     */
    private Client client;

    // ************************** \\
    // *      CONSTRUCTORS      * \\
    // ************************** \\
    
    public ServerCom(JTextArea status, Client client) {
        this.status = status;
        this.client = client;
    }

    // ************************** \\
    // *     ACCESS METHODS     * \\
    // ************************** \\

    // ************************** \\
    // *     PUBLIC METHODS     * \\
    // ************************** \\
    
    /**
     * Listening to server, awaiting commands.
     * Commands are givent to client to handle them.
     */
    @Override
    public void run() {
        while (client.isComOpened()) {
            String request = client.readRequest();
            status.append("\n->" + request);
            client.parseRequest(request);
        }
    }

    // ************************** \\
    // *    PRIVATE METHODS     * \\
    // ************************** \\
}
