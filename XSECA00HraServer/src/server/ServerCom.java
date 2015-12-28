package server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Random;
import javax.swing.JTextArea;
import org.apache.log4j.Logger;

/**
 *  ServerCom - thread handling server side communication
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
     * Reference to parent server.
     * Used when asking about number of players joined in.
     */
    private Server server;
    
    /**
     * Text area that will be updated with received and sent commands.
     */
    private JTextArea status;
    
    /**
     * Log for logging server traffic.
     */
    private Logger log = Logger.getRootLogger();
   
    /**
     * Client side source of commands.
     */
    private BufferedReader plrIn;
    
    /**
     * Client side reciever of commands - client that sends in commands.
     */
    private PrintWriter senderOut;
    
    /**
     * Client side reciever of commands - the second client that waits for commands.
     */
    private PrintWriter listenerOut;
    
    /**
     * Determine if this thread works for player1 or player2.
     */
    private boolean plr1;
    
    /**
     * Determine if the communication wasn't started yet.
     * After receiving "C_LOGIN" message permanently turns to false.
     * Since then it never responds "C_LOGIN" message again.
     */
    private boolean initialConnection = true;
    
    /**
     * Determine if the game in progress or finshed.
     * When the game finished, this thread stops running.
     */
    private boolean gameInProgress = true;

    // ************************** \\
    // *      CONSTRUCTORS      * \\
    // ************************** \\
    
    public ServerCom(Server server, JTextArea status, PrintWriter sender, PrintWriter listener, BufferedReader in, boolean plr1) {
        this.server = server;
        this.status = status;
        this.senderOut = sender;
        this.listenerOut = listener;
        this.plrIn = in;
        this.plr1 = plr1;
    }

    // ************************** \\
    // *     ACCESS METHODS     * \\
    // ************************** \\

    // ************************** \\
    // *     PUBLIC METHODS     * \\
    // ************************** \\
    
    /**
     * Listening to client, awaiting commands.
     * Commands are handled and responses sent to (possibly) both sides.
     */
    @Override
    public void run() {
        while (gameInProgress) {
            String request = "";
            try {
                request = plrIn.readLine();
                status.append("\n" + this.getName() + " <- " + request);
            } catch (Exception e) {
                status.append("\n" + this.getName() + " <- " + "Error listening to client"); 
            }
            //
            handleRequest(request);
        }
    }
    
    /**
     * Necessary for plr1 communication - listner for plr2 is created later than the thread starts.
     */
    public void updateListener(PrintWriter listener) {
        this.listenerOut = listener;
    }

    // ************************** \\
    // *    PRIVATE METHODS     * \\
    // ************************** \\
    
    /**
     * Handle and response request given from client.
     */
    private void handleRequest(String request) {
        // log the request
        log.info("Handled client request: " + request);
        // handle the request
        String[] params = request.split(" ");
        switch (params[0]) {
            case "C_LOGIN": 
                // respond only if this message is recieved for the first time
                if (initialConnection) {
                    // player logged in - send him hello message
                    senderOut.println("S_HELLO Vítejte ve hře");
                    // save his name
                    String name = params[1];
                    for (int i=2;i<params.length;i++) {
                        name = name + " " + params[i];
                    }
                    // now depends if player 1 or player 2
                    if (plr1) {
                        server.setPlr1Name(name);
                        //
                        senderOut.println("S_RED");
                        senderOut.println("S_WAIT");
                    } else {
                        server.setPlr2Name(name);
                        //
                        senderOut.println("S_BLUE");
                        //
                        senderOut.println("S_START");
                        listenerOut.println("S_START");
                        //
                        sendToss();
                    }
                    //
                    initialConnection = false;
                }
                break;
            case "C_LOGOUT": 
                // player logged out - opponent wins
                senderOut.println("S_BYE Hezký den");
                listenerOut.println("S_BREAK");
                gameInProgress = false;
                break;
            case "C_LAST": 
                // last move was made
                // client also send info about red and blue player result
                // (stored in params[1] and params[2]
                if (Integer.valueOf(params[1])>Integer.valueOf(params[2])) {
                    senderOut.println("S_END red");
                    listenerOut.println("S_END red");
                } else {
                    senderOut.println("S_END blue");
                    listenerOut.println("S_END blue");
                }
                gameInProgress = false;
                break;
            default:
                // forward request to listener
                listenerOut.println(request);
        }
        //
        senderOut.flush();
        if (listenerOut!=null) { // this can happen for player 1 at the begining
            listenerOut.flush();
        }
        //
        status.append("\n" + this.getName() + " -> Response sent");
    }
    
    /**
     * Special operation performed at the begining of game.
     * Determine which player will start.
     */
    private void sendToss() {
        Random rand = new Random();
        if (rand.nextInt(100)%2==0) {
            senderOut.println("S_TOSS head");
            listenerOut.println("S_TOSS head");
        } else {
            senderOut.println("S_TOSS tail");
            listenerOut.println("S_TOSS tail");
        }
	}
}
