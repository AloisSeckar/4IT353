package gui;

import client.Card;
import client.Client;
import client.Game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *  GUIWin - Main graphic and gameplay window.
 *
 * @author Alois Sečkár (xseca00@vse.cz)
 * @version 1.0
 */
public class GUIWin extends JFrame {

    // ************************** \\
    // *        CONSTANTS       * \\
    // ************************** \\
    
    // ************************** \\
    // *       PROPERTIES       * \\
    // ************************** \\
    
    // gui elements
    private JFrame parent;
    
    private JPanel gameFieldPanel;
    
    private JTextArea statusText = new JTextArea();
    
    private JButton plr1Button = new JButton("Táhnout");
    private JButton plr2Button = new JButton("Táhnout");
    
    private JLabel turnLabel = new JLabel("Tah: ---");
    private JLabel actionLabel = new JLabel("Akce: ---");
    private JLabel scoreLabel = new JLabel("Stav: R - B -");
    
    /**
     * Index of currently selected card from players deck.
     * Ranged between 0 and 14 (15 cards total)
     */
    private int deckIndex = -1;
    
    /**
     * Coordinates of currenly selected card from game field.
     * Ranged within 5x5 grid.
     */
    private Point fieldIndex = null;
    
    /**
     * Instance of Game class responsible for gameplay logic.
     */
    private Game game;
    
    /**
     * Instance of Client class responsible for server communication.
     */
    private Client client;
    
    /**
     * Instance of ServerCom thread responsible for listening to server.
     */
    private ServerCom serverComReader = new ServerCom(statusText, client);
    
    /**
     * Determines if player can play or has to wait for opponent.
     */
    private boolean turnAllowed = false;
    
    /**
     * Determines if communication with server was started.
     * If yes, it needs to be closed in the end.
     */
    private boolean serverComInitialized = false;

    // ************************** \\
    // *      CONSTRUCTORS      * \\
    // ************************** \\
    
    public GUIWin() {
        super();
        
        MenuBar menu = createMenu();
        this.setMenuBar(menu);
        
        game = new Game();
        
        JPanel plr1Panel = createPlrPanel(true, game.getPlrCards(true));
        JPanel plr2Panel = createPlrPanel(false, game.getPlrCards(false));
        gameFieldPanel = createGameFieldPanel(game.getFieldCards());
        JPanel statusPanel = createStatusPanel();
        
        this.setLayout(new BorderLayout());
        this.add(plr1Panel, BorderLayout.LINE_START);
        this.add(gameFieldPanel, BorderLayout.CENTER);
        this.add(plr2Panel, BorderLayout.LINE_END);
        this.add(statusPanel, BorderLayout.PAGE_END);
        
        
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocation(100,100);
        this.setTitle("4IT353 - Hra XSECA00");
        this.pack();
        
        assignComponent();
    }
    
    /**
     * Create instances of classes, that cannot be referenced directly in constructor.
     */
    private void assignComponent() {
        parent = this;
        
        client = new Client(this);
    }

    // ************************** \\
    // *     ACCESS METHODS     * \\
    // ************************** \\

    // ************************** \\
    // *     PUBLIC METHODS     * \\
    // ************************** \\
    
    /**
     * Set GUI either to player1 (red) or player2 (blue).
     */
    public void setPlayer(boolean plr1) { 
        if (plr1) {
            plr2Button.setText("---");
        } else {
            plr1Button.setText("---");
        }
        //
        game.setPlayer(plr1); 
        game.preparePlrCards(); // player checked inside the method
    }
    
    /**
     * Start game.
     * RedPlayerStarts determines which side has first turn.
     */
    public void startGame(boolean redPlayerStarts) {
        if (redPlayerStarts) {
            switchTurn(game.getPlayer());
        } else {
            switchTurn(!game.getPlayer());
        }
    }
    
    /**
     * End game (regulary, after one side wins on points).
     */
    public void endGame(boolean redPlayer) {
        // update status labels
        actionLabel.setText("Akce: Konec hry");
        scoreLabel.setText("Stav: R " + game.getPlr1Result() + " B " + game.getPlr2Result());
        // inform player
        if (redPlayer) { // red player wins
            if (game.getPlayer()) {
                JOptionPane.showMessageDialog(parent, "Gratulujeme k VÍTĚZSTVÍ!!!", "XSECA00Hra - Oznámení", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "Bohužel jste byli poraženi.", "XSECA00Hra - Oznámení", JOptionPane.INFORMATION_MESSAGE);  
            }
        } else { // blue player wins
            if (game.getPlayer()) {
                JOptionPane.showMessageDialog(parent, "Bohužel jste byli poraženi.", "XSECA00Hra - Oznámení", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "Gratulujeme k VÍTĚZSTVÍ!!!", "XSECA00Hra - Oznámení", JOptionPane.INFORMATION_MESSAGE);  
            }
        }
        // clean server com
        closeServerCommunication();
    }
    
    /**
     * End game (after one side disconnects).
     */
    public void breakGame() {
        // update status labels
        actionLabel.setText("Konec hry");
        scoreLabel.setText("Stav: R " + game.getPlr1Result() + " B " + game.getPlr2Result());
        // inform player
        JOptionPane.showMessageDialog(parent, "Partner se odhlásil. Gratulujeme k VÍTĚZSTVÍ!!!", "XSECA00Hra - Oznámení", JOptionPane.INFORMATION_MESSAGE);
        // clean server com
        closeServerCommunication();
    }
    
    /**
     * Handle opponent's turn received from server.
     */
    public void evaluateOpponentsTurn(Point fieldIndex, Card newCard) {
        // perform recieved move
        game.evaluateOpponentsTurn(fieldIndex, newCard);
        // switch turn to player's turn
        switchTurn(true);
    }

    // ************************** \\
    // *    PRIVATE METHODS     * \\
    // ************************** \\
    
    /**
     * Properly close communication with server in the end.
     */
    private void closeServerCommunication() {
        client.logOut();
        serverComInitialized = false;
    }
    
    /**
     * Handle turn made by player.
     * Either refuse invalid turn, or process it.
     */
    private void evaluatePlayersTurn() {
        if ((deckIndex<0)&&(deckIndex>14)) {
            JOptionPane.showMessageDialog(parent, "Musíte vybrat svojí kartu, se kterou chcete táhnout.", "XSECA00Hra - Oznámení", JOptionPane.INFORMATION_MESSAGE);
        } else if (fieldIndex==null) {
            JOptionPane.showMessageDialog(parent, "Musíte vybrat pozici, na kterou chcete táhnout.", "XSECA00Hra - Oznámení", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (game.evaluateTurn(deckIndex, fieldIndex)) {
                // send info to second player
                Card card = game.getPlrCard(deckIndex, game.getPlayer()).getCard();
                client.sendRequest("C_MOVE " + card.getLeft() + " " + card.getRight() + " " + card.getTop() + " " + card.getBottom() + " " + fieldIndex.x + " " + fieldIndex.y);
                // check for game end
                if (game.isEnd()) {
                    client.sendRequest("C_LAST " + game.getPlr1Result() + " " + game.getPlr2Result());
                    statusText.append("\nKonec hry");
                } else {
                    // reset controls - wait for opponent
                    switchTurn(false);
                }
            } else {
                JOptionPane.showMessageDialog(parent, "Neplatný tah.", "XSECA00Hra - Oznámení", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    // ************************** \\
    // *      GUI CREATION      * \\
    // ************************** \\
    
    /**
     * Creating menu bar.
     */
    private MenuBar createMenu() {
        
        Menu gameMenu = new Menu("Hra");
        MenuItem newGame = new MenuItem("Nová hra");
        newGame.addActionListener(new MenuNewGame());
        MenuItem exitGame = new MenuItem("Konec");
        exitGame.addActionListener(new MenuExitGame());
        gameMenu.add(newGame);
        gameMenu.add(exitGame);
        
        Menu netMenu = new Menu("Nastavení");
        MenuItem gameButton = new MenuItem("Hra");
        gameButton.addActionListener(new MenuGameButton());
        MenuItem netButton = new MenuItem("Síť");
        netButton.addActionListener(new MenuNetButton());
        netMenu.add(gameButton);
        netMenu.add(netButton);
        
        Menu helpMenu = new Menu("Help");
        MenuItem helpButton = new MenuItem("O hře");
        helpButton.addActionListener(new MenuHelpButton());
        helpMenu.add(helpButton);
        
        MenuBar menu = new MenuBar();
        menu.add(gameMenu);
        menu.add(netMenu);
        menu.add(helpMenu);
        
        return menu;
    }

    /**
     * Creating side panel for players.
     */
    private JPanel createPlrPanel(boolean plr1, CardPanel[] deck) {
        JPanel panel = new JPanel();
        
        if (plr1) {
            panel.setBackground(Color.RED);
        } else {
            panel.setBackground(Color.BLUE);
        }
        
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        JLabel label = new JLabel();
        if (plr1) {
            label.setText("Hráč 1");
        } else {
            label.setText("Hráč 2");
        }
        Font font = new Font("Arial", Font.BOLD, 22);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        panel.add(label, c);
        
        c.gridy = 1;
        panel.add(Box.createRigidArea(new Dimension(5,20)), c);
        
        c.gridwidth = 1;
        c.gridy = 2;
        for (int i=0; i<15; i++) {
            // deck[i] = new CardPanel(0, 0, 0, 0, CardColors.COLOR_DECK, true, i, -1);
            deck[i].addMouseListener(new MouseDeck());
            panel.add(deck[i], c);
            //
            c.gridx++;
            if (c.gridx>=3) {
                c.gridx = 0;
                c.gridy++;
            }
        }
        
        c.gridwidth = 3;
        c.gridy++;
        panel.add(Box.createRigidArea(new Dimension(5,20)), c);
        
        c.gridy++;
        JButton plrButton;
        if (plr1) {
            plrButton = plr1Button;
        } else {
            plrButton = plr2Button;
        }
        plrButton.setToolTipText("Provést tah");
        plrButton.setEnabled(false);
        plrButton.addActionListener(new ActionPlrButton());
        panel.add(plrButton, c);
        
        return panel;
    }

    /**
     * Creating central panel for game field and server messages.
     */
    private JPanel createGameFieldPanel(CardPanel[][] gameField) {
        JPanel panel = new JPanel();
        
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 0;
        c.gridy = 0;
        for (int i=0; i<5; i++) {
            for (int i2=0; i2<5; i2++) {
                // int index = ((i2*5))+i;
                // gameField[i][i2] = new CardPanel(0, 0, 0, 0, CardColors.COLOR_FIELD, false, i, i2);
                gameField[i][i2].addMouseListener(new MouseField());
                panel.add(gameField[i][i2], c);
                //
                c.gridy++;
            }
            c.gridy = 0;
            c.gridx++;
        }
        
        c.gridwidth = 5;
        c.gridx = 0;
        c.gridy = 5;
        panel.add(Box.createRigidArea(new Dimension(5,20)), c);
        
        c.gridy++;
        JLabel label = new JLabel("Komunikace se serverem:");
        panel.add(label, c);
        
        c.gridy++;
        panel.add(Box.createRigidArea(new Dimension(5,5)), c);
        
        statusText.setColumns(38);
        statusText.setRows(5);
        statusText.setLineWrap(true);
        statusText.setWrapStyleWord(true);
        statusText.setToolTipText("Stav hry...");
        statusText.setText("Zahajte hru pomocí položky \"Nová hra\" v menu \"Hra\"...");
        statusText.setEditable(false);
        JScrollPane statusScrollPane = new JScrollPane(statusText); 
        statusScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        c.gridy++;
        panel.add(statusScrollPane, c);
        
        return panel;
    }

    /**
     * Creating bottom status panel.
     */
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel();
        
        panel.add(turnLabel);
        panel.add(actionLabel);
        panel.add(scoreLabel);
        
        return panel;
    }
    
    // ************************** \\
    // *   GUI STATE CHANGES    * \\
    // ************************** \\
    
    /**
     * Set GUI according to who is at turn.
     * Either enable or disable controls, change status labels and gameplay variables.
     */
    private void switchTurn(boolean turn) {
        if (game.getPlayer()) {
            plr1Button.setEnabled(turn);
            if (turn) {
                turnLabel.setText("Tah: Červená");
            } else {
                turnLabel.setText("Tah: Modrá");
            }
        } else {
            plr2Button.setEnabled(turn);
            if (turn) {
                turnLabel.setText("Tah: Modrá");
            } else {
                turnLabel.setText("Tah: Červená");
            }
        }
        // reset status panel
        if (turn) {
            actionLabel.setText("Akce: Čeká se na váš tah");
            turnAllowed = true;
        } else {
            actionLabel.setText("Akce: Čeká se na tah soupeře");
            turnAllowed = false;
        }
        // update score label
        scoreLabel.setText("Stav: R " + game.getPlr1Result() + " B " + game.getPlr2Result());
        // repaint central panel
        parent.pack();
        // reset selections
        deckIndex = -1;
        fieldIndex = null;
        // allow/disallow gameplay
        turnAllowed = turn;
    }
    
    // ************************** \\
    // *    ACTION LISTENERS    * \\
    // ************************** \\

    /**
     * Action listener - Menu button "New game"
     */
    private class MenuNewGame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!serverComInitialized) { // can be initialized just once
                client.logIn();
                //
                serverComReader = new ServerCom(statusText, client);
                serverComReader.start();
                //
                serverComInitialized = true;
            }
        }
    }

    /**
     * Action listener - Menu button "Exit"
     */
    private class MenuExitGame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int choice = JOptionPane.showConfirmDialog(parent, "Opravdu chcete ukončit program?", "Konec hry", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                if (serverComInitialized) {
                    // send logout message to server
                    client.sendRequest("C_LOGOUT");
                    // clean server com
                    closeServerCommunication();
                }
                // close window
                parent.dispose();
            }
        }
    }
    
    /**
     * Action listener - Menu button "Game"
     */
    private class MenuGameButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GameOptions gameOpt = new GameOptions(client);
            gameOpt.setModal(true);
            gameOpt.setVisible(true);
        }
    }

    /**
     * Action listener - Menu button "Net"
     */
    private class MenuNetButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            NetOptions netOpt = new NetOptions(client);
            netOpt.setModal(true);
            netOpt.setVisible(true);
        }
    }

    /**
     * Action listener - Menu button "Help"
     */
    private class MenuHelpButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String msg = "Úkolem hry je zabrat více hracích polí než soupeř.";
            msg += " Hrací pole obsadí hráč přiložením vybrané karty ze svého balíčku.";
            msg += " Může získat navíc až všechny 4 sousední už přiložené karty patřící soupeři,";
            msg += " pokud jsou číselné hodnoty jeho karty na přilehlých hranách větší,";
            msg += " než příslušné hodnoty soupeřových karet.\n";
            msg += " Hra končí obsazením posledního volného hracího pole. Vítězí hráč,";
            msg += " který v té chvíli drží více karet.";
            JOptionPane.showMessageDialog(parent, msg, "O hře", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Action listener - Game button "Turn"
     */
    private class ActionPlrButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (turnAllowed) {
                evaluatePlayersTurn();
            }
        }
    }
    
    // ************************** \\
    // *    MOUSE LISTENERS     * \\
    // ************************** \\
    
    /**
     * Mouse listener - Card panel from player's deck
     */
    private class MouseDeck implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (turnAllowed) {
                CardPanel card = (CardPanel) e.getSource();
                // check, if this card wasn't already used
                if (card.getColor()!=CardColors.COLOR_DECK_DEPLEATED) {
                    // check if there was previously selected card
                    if ((deckIndex>-1)&&(deckIndex<15)) {
                        CardPanel panel = game.getPlrCard(deckIndex, game.getPlayer());
                        if (game.getPlayer()) {
                            panel.recolor(CardColors.COLOR_PLR1);
                        } else {
                            panel.recolor(CardColors.COLOR_PLR2);
                        }
                    }
                    // update  currently selected card
                    deckIndex = card.getIndex();
                    game.getPlrCard(deckIndex, game.getPlayer()).recolor(CardColors.COLOR_DECK_SELECTED);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
        
    }
    
    /**
     * Mouse listener - Card panel from game field
     */
    private class MouseField implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (turnAllowed) {
                CardPanel card = (CardPanel) e.getSource();
                // check, if this card wasn't already played
                if ((card.getColor()!=CardColors.COLOR_PLR1)
                   &&(card.getColor()!=CardColors.COLOR_PLR2)){
                    if (fieldIndex!=null) {
                        game.getFieldCard(fieldIndex.x,fieldIndex.y).recolor(CardColors.COLOR_FIELD);
                    }
                    //
                    fieldIndex = new Point();
                    fieldIndex.x = card.getIndex();
                    fieldIndex.y = card.getIndex2();
                    game.getFieldCard(fieldIndex.x,fieldIndex.y).recolor(CardColors.COLOR_FIELD_SELECTED);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
        
    }
}
