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
 *  Net Options - Server connection settings.
 *
 * @author Alois Sečkár (xseca00@vse.cz)
 * @version 1.0
 */
class NetOptions extends JDialog {

    // ************************** \\
    // *        CONSTANTS       * \\
    // ************************** \\

    // ************************** \\
    // *       PROPERTIES       * \\
    // ************************** \\
    
    private Client gameClient;
    
    private JTextField addr = new JTextField();
    private JTextField port = new JTextField();

    // ************************** \\
    // *      CONSTRUCTORS      * \\
    // ************************** \\
    
    public NetOptions(Client client) {
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
        this.setTitle("Síťové nastavení");
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
        JLabel l1 = new JLabel("Síťová adresa:  ");
        JLabel l2 = new JLabel("Port:  ");
        addr.setText(gameClient.getCom_addr());
        port.setText(gameClient.getCom_port());
        //
        panel.setLayout(new GridLayout(2, 2));
        panel.add(l1);
        panel.add(addr);
        panel.add(l2);
        panel.add(port);
        //
        return panel;
    }
    
    private class SetNetButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                gameClient.setCOM_ADDR(addr.getText());
                gameClient.setCOM_PORT(Integer.valueOf(port.getText()));
                JOptionPane.showMessageDialog(null, "Nastavení bylo změněno.", "Síťové nastavení", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Zadány neplatné hodnoty.", "Síťové nastavení", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
