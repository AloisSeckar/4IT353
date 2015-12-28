package gui;



import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *  GUIServer - //TODO class description
 *
 * @author Ellrohir
 * @version 1.0
 */
public class GUIServer extends JFrame {

    // ************************** \\
    // *        CONSTANTS       * \\
    // ************************** \\

    // ************************** \\
    // *       PROPERTIES       * \\
    // ************************** \\
    
    /**
     * Text area that will be updated with received and sent commands.
     */
    private JTextArea statusText = new JTextArea();

    // ************************** \\
    // *      CONSTRUCTORS      * \\
    // ************************** \\
    
    public GUIServer() {
        statusText.setColumns(38);
        statusText.setRows(5);
        statusText.setLineWrap(true);
        statusText.setWrapStyleWord(true);
        statusText.setToolTipText("Stav komunikace se serverem...");
        statusText.setText("Server spuštěn...");
        statusText.setEditable(false);
        JScrollPane statusScrollPane = new JScrollPane(statusText); 
        statusScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(statusScrollPane);
        //
        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocation(100,100);
        this.setTitle("4IT353 - Hra XSECA00 - Server monitor");
    }

    // ************************** \\
    // *     ACCESS METHODS     * \\
    // ************************** \\

    // ************************** \\
    // *     PUBLIC METHODS     * \\
    // ************************** \\
    
    /**
     * Update given text area with received and sent commands.
     */
    public void updateServerStatus(String msg) {
        statusText.append("\nSERV -> ");
        statusText.append(msg);
    }
    
    /**
     * Used for giving reference to ServerCom thread.
     */
    public JTextArea getStatusText() {
        return this.statusText;
    }

    // ************************** \\
    // *    PRIVATE METHODS     * \\
    // ************************** \\

}
