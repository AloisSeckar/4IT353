package gui;

import client.Client;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *  Name Options - Player name settings.
 *
 * @author Alois Sečkár (xseca00@vse.cz)
 * @version 1.0
 */
class GameOptions extends JDialog {

    // ************************** \\
    // *        CONSTANTS       * \\
    // ************************** \\

    // ************************** \\
    // *       PROPERTIES       * \\
    // ************************** \\
    
    private Client gameClient;
    
    private JTextField name = new JTextField();

    // ************************** \\
    // *      CONSTRUCTORS      * \\
    // ************************** \\
    
    public GameOptions(Client client) {
        this.gameClient = client;
        //
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.add(createForm());
        this.add(Box.createRigidArea(new Dimension(10,10)));
        JButton button = new JButton("Nastavit");
        button.addActionListener(new SetNetButton());
        this.add(button);
        //
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle("Nastavení");
        this.setLocation(200, 200);
        this.pack();
    }

    // ************************** \\
    // *     ACCESS METHODS     * \\
    // ************************** \\

    // ************************** \\
    // *     PUBLIC METHODS     * \\
    // ************************** \\

    // ************************** \\
    // *    PRIVATE METHODS     * \\
    // ************************** \\
    
    private JPanel createForm() {
        JPanel panel = new JPanel();
        //
        JLabel l1 = new JLabel("Jméno hráče:  ");
        name.setText(gameClient.getPlrName());
        //
        panel.setLayout(new GridLayout(1, 1));
        panel.add(l1);
        panel.add(name);
        //
        return panel;
    }
    
    private class SetNetButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gameClient.setPlrName(name.getText());
            JOptionPane.showMessageDialog(null, "Jméno hráče bylo změněno", "Nastavení", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}