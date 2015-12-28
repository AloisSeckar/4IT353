package gui;

import java.awt.Color;

/**
 *  CardColors - Container for color constants.
 *
 * @author Alois Sečkár (xseca00@vse.cz)
 * @version 1.0
 */
public class CardColors {

    // ************************** \\
    // *        CONSTANTS       * \\
    // ************************** \\
    
    /**
     * Color for player1 - red.
     */
    public static final Color COLOR_PLR1 = new Color(178, 34, 34);
    
    /**
     * Color for player1 - blue.
     */
    public static final Color COLOR_PLR2 = new Color(65, 105, 225);
    
    /**
     * Color for cards in deck (without possesion).
     */
    public static final Color COLOR_DECK = Color.LIGHT_GRAY;
    
    /**
     * Color for currently selected card from deck.
     */
    public static final Color COLOR_DECK_SELECTED = new Color(240, 230, 140);
    
    /**
     * Color for card from deck that was already used.
     */
    public static final Color COLOR_DECK_DEPLEATED = Color.GRAY;
    
    /**
     * Color for empty game field card.
     */
    public static final Color COLOR_FIELD = Color.GRAY;

    /**
     * Color for currently selected card from game field.
     */
    public static final Color COLOR_FIELD_SELECTED = new Color(240, 230, 140);

}
