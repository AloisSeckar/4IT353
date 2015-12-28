package server;



import gui.GUIServer;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    // ************************** \\
    // *        CONSTANTS       * \\
    // ************************** \\

    // ************************** \\
    // *       PROPERTIES       * \\
    // ************************** \\
    
    // server com properties
    private ServerSocket serverSocket;
	private Socket plr1Socket;
    private Socket plr2Socket;
    
    // outputs and inputs
    private PrintWriter plr1Out;
    private PrintWriter plr2Out;
    private BufferedReader plr1In;
    private BufferedReader plr2In;
    
    /**
     * Thread that listens and responds to player1.
     */
    private ServerCom serverListener1;
    
    /**
     * Thread that listens and responds to player2.
     */
    private ServerCom serverListener2;
    
    /**
     * Server status GUI that will monitor the communication.
     */
    private final GUIServer guiWin;
    
    /**
     * Number of players logged in.
     * Acessed from outside to determine how to react on C_LOGIN message.
     * First player is handled differently from second player.
     */
    private int players;

    /**
     * Stored player1 name.
     * Currently unused...
     */
	private String plr1Name;

    /**
     * Stored player2 name.
     * Currently unused...
     */
	private String plr2Name;

    // ************************** \\
    // *      CONSTRUCTORS      * \\
    // ************************** \\
    
    public Server() {
        
        guiWin = new GUIServer();
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                guiWin.setVisible( true );
            }
        });
        
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            //TODO error handle
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }
    }

    // ************************** \\
    // *     ACCESS METHODS     * \\
    // ************************** \\
    
    

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public String getPlr1Name() {
        return plr1Name;
    }

    public void setPlr1Name(String plr1Name) {
        this.plr1Name = plr1Name;
    }

    public String getPlr2Name() {
        return plr2Name;
    }

    public void setPlr2Name(String plr2Name) {
        this.plr2Name = plr2Name;
    }

    // ************************** \\
    // *     PUBLIC METHODS     * \\
    // ************************** \\
    
    /**
     * Create connection and wait for both players to join
     */
    public void connection() {
        try {
            guiWin.updateServerStatus("IP Adresa: " + InetAddress.getLocalHost().getHostAddress());
            guiWin.updateServerStatus("Čekání na spojení...");
            //
            plr1Socket = serverSocket.accept();
            handlePlayer1();
            plr2Socket = serverSocket.accept();
            handlePlayer2();
            //
        } catch (Exception e) {
            //TODO error handle
            System.err.println("Accept failed.");
            System.exit(1);
        }
    }

    // ************************** \\
    // *    PRIVATE METHODS     * \\
    // ************************** \\
    
    /**
     * Prepare communication thread for player1.
     */
    private void handlePlayer1() {
        // get reader and writer for plr1
        // input
        try {
            plr1In = new BufferedReader(new InputStreamReader(plr1Socket.getInputStream(), "UTF-8"));
        } catch (Exception e) {
            // TODO error msg
            System.out.println("Chyba ve vytvoreni input plr1!");
            System.exit(0);
        }
        // output
        try {
            plr1Out = new PrintWriter(new OutputStreamWriter(plr1Socket.getOutputStream(), "UTF-8"));
        } catch (Exception e) {
            // TODO error msg
            System.out.println("Chyba ve vytvoreni output plr1!");
            System.exit(0);
        }
        //
        guiWin.updateServerStatus("Přihlášen klient 1");
        guiWin.updateServerStatus("Čekání na klienta 2");
        // create communication thread
        serverListener1 = new ServerCom(this, guiWin.getStatusText(), plr1Out, plr2Out, plr1In, true);
        serverListener1.setName("RED");
        serverListener1.start();
    }
    
    /**
     * Prepare communication thread for player2.
     */
    private void handlePlayer2() {
        // get reader and writer for plr1
        // input
        try {
            plr2In = new BufferedReader(new InputStreamReader(plr2Socket.getInputStream(), "UTF-8"));
        } catch (Exception e) {
            // TODO error msg
            System.out.println("Chyba ve vytvoreni input plr2!");
            System.exit(0);
        }
        // output
        try {
            plr2Out = new PrintWriter(new OutputStreamWriter(plr2Socket.getOutputStream(), "UTF-8"));
        } catch (Exception e) {
            // TODO error msg
            System.out.println("Chyba ve vytvoreni output plr2!");
            System.exit(0);
        }
        //
        guiWin.updateServerStatus("Přihlášen klient 2");
        guiWin.updateServerStatus("Probíhá příprava hry");
        // update plr1 com thread
        serverListener1.updateListener(plr2Out);
        // create communication thread
        serverListener2 = new ServerCom(this, guiWin.getStatusText(), plr2Out, plr1Out, plr2In, false);
        serverListener2.setName("BLUE");
        serverListener2.start();
    }
}
