package main;

import gui.GUIWin;
import java.awt.EventQueue;

/**
 *  XSECA00Hra - Client side entry point.
 *
 * @author Alois Sečkár (xseca00@vse.cz)
 * @version 1.0
 */
public class XSECA00Hra {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // create and launch game client
        final GUIWin plr = new GUIWin();
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                plr.setVisible( true );
            }
        });
    }
}
