package gui;

import client.Card;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *  CardPanel - Element handling card displaying.
 *
 * @author Alois Sečkár (xseca00@vse.cz)
 * @version 1.0
 */
public class CardPanel extends JPanel {

    // ************************** \\
    // *        CONSTANTS       * \\
    // ************************** \\

    // ************************** \\
    // *       PROPERTIES       * \\
    // ************************** \\
    
    // card value labels
    private JLabel valueN = new JLabel();
    private JLabel valueE = new JLabel();
    private JLabel valueS = new JLabel();
    private JLabel valueW = new JLabel();
    
    /**
     * Background color GUI element.
     */
    private JPanel colorPanel = new JPanel();
    
    /**
     * Card that is represented by this panel.
     */
    private Card card;
    
    /**
     * Current color of this panel.
     */
    private Color color;
    
    /**
     * Index for quick search in player deck cards.
     * Alternatively first index of game field card.
     */
    private int index;
    
    /**
     * Second index for quick search in game field cards.
     */
    private int index2;

    // ************************** \\
    // *      CONSTRUCTORS      * \\
    // ************************** \\
    
    public CardPanel() {
        this(0, 0, 0, 0, CardColors.COLOR_DECK, true, -1, -1);
    }
    
    public CardPanel(Color color, boolean small, int index, int index2) {
        this(0, 0, 0, 0, color, small, index, index2);
    }
    
    public CardPanel (int v1, int v2, int v3, int v4, Color color, boolean small, int index, int index2) {
        colorPanel.setLayout(new GridLayout(3,3));
        colorPanel.add(Box.createRigidArea(new Dimension(5,10)));
        colorPanel.add(valueN);
        colorPanel.add(Box.createRigidArea(new Dimension(5,10)));
        colorPanel.add(valueE); 
        colorPanel.add(Box.createRigidArea(new Dimension(5,10)));
        colorPanel.add(valueW);
        colorPanel.add(Box.createRigidArea(new Dimension(5,10)));
        colorPanel.add(valueS);
        colorPanel.add(Box.createRigidArea(new Dimension(5,10)));
        //
        this.add(colorPanel);
        //
        this.card = new Card(v1, v2, v3, v4);
        this.color = color;
        //
        changeValues(card);
        changeColor(color);
        changeDiameter(small);
        //
        this.index = index;
        this.index2 = index2;
    }

    // ************************** \\
    // *     ACCESS METHODS     * \\
    // ************************** \\

    public int getIndex() {
        return index;
    }

    public int getIndex2() {
        return index2;
    }

    public Color getColor() {
        return color;
    }
    
    public Card getCard() {
        return card;
    }

    // ************************** \\
    // *     PUBLIC METHODS     * \\
    // ************************** \\
    
    /**
     * Update this card with random values.
     */
    public void randomize() {
        Card newCard = new Card();
        newCard.randomize();
        update(newCard);
    }
    
   /**
     * Recolor this card with given color.
     */
    public void recolor(Color color) {
        this.color = color;
        changeColor(color);
    }
 
    /**
     * Update this card to given card.
     */
    public void update(Card card) {
        this.card = card;
        //
        changeValues(card);
    }
    
    /**
     * Update this card to given card and color.
     */
    public void update(Card card, Color color) {
        this.card = card;
        this.color = color;
        //
        changeValues(card);
        changeColor(color);
    }
    
    /**
     * Hide card value labels.
     */
    public void hideCard() {
        valueN.setText(" -");
        valueE.setText(" -");
        valueS.setText(" -");
        valueW.setText(" -");
    }
   
    /**
     * Check if value labels are marked hiden.
     */
    public boolean isHidden() {
        if ((valueN.getText().equals(" -"))
          &&(valueE.getText().equals(" -"))
          &&(valueS.getText().equals(" -"))
          &&(valueW.getText().equals(" -"))) {
            return true;
        } else {
            return false;
        }
    }

    // ************************** \\
    // *    PRIVATE METHODS     * \\
    // ************************** \\
    
    /**
     * Change card value lables to new values.
     */
    private void changeValues(Card card) {
        valueN.setText(" " + Integer.valueOf(card.getTop()).toString());
        valueE.setText(" " + Integer.valueOf(card.getLeft()).toString());
        valueS.setText(" " + Integer.valueOf(card.getBottom()).toString());
        valueW.setText(" " + Integer.valueOf(card.getRight()).toString());
    }
    
    /**
     * Change card background to new color.
     */
    private void changeColor(Color color) {
        colorPanel.setBackground(color);
    }
    
    /**
     * Change size of card panel.
     * Deck card panels are smaller than field card panels.
     */
    private void changeDiameter(boolean small) {
        Font font;
        if (small) {
            // panel size
            colorPanel.setPreferredSize(new Dimension(55,55));
            // fonts size  
            font = new Font("Arial", Font.BOLD, 17);
        } else {
            colorPanel.setPreferredSize(new Dimension(75,75));
            // fonts size  
            font = new Font("Arial", Font.BOLD, 20);
        }
        // set font styles
        valueN.setFont(font);
        valueE.setFont(font);
        valueS.setFont(font);
        valueW.setFont(font);
    }
}
