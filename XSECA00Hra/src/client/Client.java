package client;

import gui.GUIWin;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *  Client - Handles communication with server.
 *
 * @author Alois Sečkár (xseca00@vse.cz)
 * @version 1.0
 */
public class Client {
    
    // ************************** \\
    // *        CONSTANTS       * \\
    // ************************** \\

    // ************************** \\
    // *       PROPERTIES       * \\
    // ************************** \\
    
    // plr name
	private String plrName = "XSECA00 Player";
    
    // server com settings
    private String com_addr = "10.0.0.3";
    private int com_port = 4444;
    
    // server com properties
	private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    
    /**
     * Parent GUI window.
     */
    private GUIWin okno;
    
    /**
     * Checks if communication with server is opened.
     * Acessed from outside.
     */
    private boolean comOpened = false;

    // ************************** \\
    // *      CONSTRUCTORS      * \\
    // ************************** \\

    public Client (GUIWin okno) {
        this.okno = okno;
    }

    // ************************** \\
    // *     ACCESS METHODS     * \\
    // ************************** \\
    
    public String getCom_addr() {
        return com_addr;
    }
    
    public void setCOM_ADDR(String COM_ADDR) {
        this.com_addr = COM_ADDR;
    }

    public void setCOM_PORT(int COM_PORT) {
        this.com_port = COM_PORT;
    }

    public String getCom_port() {
        return Integer.valueOf(com_port).toString();
    }

    public String getPlrName() {
        return plrName;
    }

    public void setPlrName(String plrName) {
        this.plrName = plrName;
    }
    
    public boolean isComOpened() {
        return comOpened;
    }

    // ************************** \\
    // *     PUBLIC METHODS     * \\
    // ************************** \\
    
    /**
     * Login to server to start communication.
     */
	public void logIn() {
        boolean error = false;
        // login
        try {
            socket = new Socket(InetAddress.getByName(com_addr),com_port);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(okno, "Nepodařilo se připojit na server.", "XSECA00Hra - Chyba", JOptionPane.ERROR_MESSAGE);
            error = true;
        }
        // create input
        if (!error) {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(okno, "Nepodařilo se vytvořit input stream.", "XSECA00Hra - Chyba", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
        }
        // create output
        if (!error) {
            try {
                output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(okno, "Nepodařilo se vytvořit output stream.", "XSECA00Hra - Chyba", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
        }
        //
        if (error) {
            System.exit(100); 
        }
        //
        comOpened = true;
        // if all ok - send login msg to server
        sendRequest("C_LOGIN " + plrName);
	}
    
    /**
     * Logout from server.
     */
	public void logOut() {
        try {
            socket.close();
            comOpened = false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(okno, "Nepodařilo se korektně uzavřít komunikaci se serverem.", "XSECA00Hra - Chyba", JOptionPane.ERROR_MESSAGE);
        }
	}
    
    /**
     * Send request to server.
     */
	public void sendRequest(String request) {
        output.println(request);
        output.flush();
	}
    
    /**
     * Get request from server.
     */
    public String readRequest() {
        String msg = "";
        try {
            msg = input.readLine();
        } catch (Exception e) {
            // do nothing
            // JOptionPane.showMessageDialog(okno, "Chyba v komunikaci se serverem.", "XSECA00Hra - Chyba", JOptionPane.ERROR_MESSAGE);   
        }
        return msg;
	}
    
    /**
     * Parse request recieved from server and take action.
     */
    public void parseRequest(String request) {
        String[] params = request.split(" ");
        switch (params[0]) {
            case "S_HELLO": 
                // do nothing...
                // unnecessary
//                String msg = params[1];
//                for (int i=2;i<params.length;i++) {
//                    msg = msg + " " + params[i];
//                }
//                JOptionPane.showMessageDialog(okno, msg, "XSECA00Hra - Zpráva", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "S_BYE": 
                // do nothing...
                // unnecessary
//                String msg = params[1];
//                for (int i=2;i<params.length;i++) {
//                    msg = msg + " " + params[i];
//                }
//                JOptionPane.showMessageDialog(okno, msg, "XSECA00Hra - Zpráva", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "S_RED":
                okno.setPlayer(true);
                break;
            case "S_BLUE":
                okno.setPlayer(false);
                break;
            case "S_WAIT": 
                JOptionPane.showMessageDialog(okno, "Čeká se na druhého hráče...", "XSECA00Hra - Oznámení", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "S_START": 
                JOptionPane.showMessageDialog(okno, "Hra byla zahájena", "XSECA00Hra - Oznámení", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "S_TOSS":
                if (params[1].equals("head")) {
                    JOptionPane.showMessageDialog(okno, "Začínat bude hráč 1 (červený)", "XSECA00Hra - Oznámení", JOptionPane.INFORMATION_MESSAGE);
                    okno.startGame(true);
                } else {
                    JOptionPane.showMessageDialog(okno, "Začínat bude hráč 2 (modrý)", "XSECA00Hra - Oznámení", JOptionPane.INFORMATION_MESSAGE);
                    okno.startGame(false);
                }
                break;
            case "C_MOVE":
                try {
                    // rebuild field index where was the turn
                    Point fieldIndex = new Point(Integer.valueOf(params[5]), Integer.valueOf(params[6]));
                    // rebuild card that was used
                    Card newCard = new Card(Integer.valueOf(params[1]), Integer.valueOf(params[2]), Integer.valueOf(params[3]), Integer.valueOf(params[4]));
                    // evaluate the turn
                    okno.evaluateOpponentsTurn(fieldIndex, newCard);
                } catch (Exception e) {
                    // any error
                    JOptionPane.showMessageDialog(okno, "Přijato neplatné oznámení tahu", "XSECA00Hra - Varování", JOptionPane.ERROR_MESSAGE);
                    sendRequest("C_ERROR Invalid turn noftification");
                }                
                break;
            case "S_END": 
                try {
                    if (params[1].equals("red")) {
                        okno.endGame(true);
                    } else {
                        okno.endGame(false);
                    }
                } catch (Exception e) {
                    // any error
                    JOptionPane.showMessageDialog(okno, "Přijato neplatné oznámení konce hry", "XSECA00Hra - Varování", JOptionPane.ERROR_MESSAGE);
                    sendRequest("C_ERROR Invalid turn noftification");
                }  
                break;
            case "S_BREAK":
                okno.breakGame();
                break;
            case "S_ERROR":
                JOptionPane.showMessageDialog(okno, "Přijato oznámení o chybě:" + params[1], "XSECA00Hra - Varování", JOptionPane.ERROR_MESSAGE); 
                break;
            case "C_ERROR":
                JOptionPane.showMessageDialog(okno, "Přijato oznámení o chybě:" + params[1], "XSECA00Hra - Varování", JOptionPane.ERROR_MESSAGE); 
                break;
            case "":
                // security...
                break;
            default: 
                JOptionPane.showMessageDialog(okno, "Přišel neznámý příkaz od serveru - " + request + ". Nebyla provedena žádná akce.", "XSECA00Hra - Varování", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // ************************** \\
    // *    PRIVATE METHODS     * \\
    // ************************** \\
}
