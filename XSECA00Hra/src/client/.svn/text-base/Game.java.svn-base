package client;

import gui.CardColors;
import gui.CardPanel;
import java.awt.Color;
import java.awt.Point;

/**
 *  Game - Handles game state and progress changes.
 *
 * @author Alois Sečkár (xseca00@vse.cz)
 * @version 1.0
 */
public class Game {
    
    // ************************** \\
    // *        CONSTANTS       * \\
    // ************************** \\
    
    // ************************** \\
    // *       PROPERTIES       * \\
    // ************************** \\

    /**
     * Determines the current player.
     * True in window for player1, false in window for player2.
     */
    private boolean redPlayer;

    /**
     * Game field from 5x5 panels.
     */
	private CardPanel[][] gameField = new CardPanel[5][5];

    /**
     * Player1 deck from 5x3 small panels.
     */
	private CardPanel[] plr1Cards = new CardPanel[15];
    
    /**
     * Player2 deck from 5x3 small panels.
     */
	private CardPanel[] plr2Cards = new CardPanel[15];

    /**
     * Number of cards currently held by player1.
     */
	private int plr1Result = 0;

    /**
     * Number of cards currently held by player2.
     */
	private int plr2Result = 0;


    // ************************** \\
    // *      CONSTRUCTORS      * \\
    // ************************** \\
    
    public Game() {
        //
        for (int i=0;i<5;i++) {
            for (int i2=0;i2<5;i2++) {
                gameField[i][i2] = new CardPanel(CardColors.COLOR_FIELD, false, i, i2);
                gameField[i][i2].hideCard();
            }
        }
        //
        for (int i=0;i<15;i++) {
            plr1Cards[i] = new CardPanel(CardColors.COLOR_DECK, true, i, -1);
            plr1Cards[i].recolor(CardColors.COLOR_PLR1);
            plr1Cards[i].hideCard();
            plr2Cards[i] = new CardPanel(CardColors.COLOR_DECK, true, i, -1);
            plr2Cards[i].recolor(CardColors.COLOR_PLR2);
            plr2Cards[i].hideCard();
        }   
    }
    
    // ************************** \\
    // *     ACCESS METHODS     * \\
    // ************************** 
    
    public boolean getPlayer() {
        return redPlayer;
    }
    
    
    public void setPlayer(boolean plr1) {
        this.redPlayer = plr1;
    }
    
    public int getPlr1Result() {
        return plr1Result;
    }

    public int getPlr2Result() {
        return plr2Result;
    }
    
    public CardPanel[] getPlrCards(boolean plr1) {
        if (plr1) {
            return plr1Cards;
        } else {
            return plr2Cards;
        }
    }
    
    public CardPanel[][] getFieldCards() {
        return gameField;
    }
    
    /**
     * Get particular card from player's deck.
     */
    public CardPanel getPlrCard(int index, boolean plr1) {
        if ((index>-1)&&(index<15)) {
            if (plr1) {
                return plr1Cards[index];
            } else {
                return plr2Cards[index];
            }
        } else {
            return new CardPanel();
        }
    }
    
    /**
     * Get particular card from game field.
     */
    public CardPanel getFieldCard(int index, int index2) {
        if ((index>-1)&&(index<5)&&(index2>-1)&&(index2<5)) {
            return gameField[index][index2];
        } else {
            return new CardPanel();
        }
    }

    // ************************** \\
    // *     PUBLIC METHODS     * \\
    // ************************** \\
    
    /**
     * Prepare player's deck.
     */
    public void preparePlrCards() {
        for (int i=0;i<15;i++) {
            if (redPlayer) {
                plr1Cards[i].randomize();
            } else {
                plr2Cards[i].randomize();
            }
        }
    }
    
    /**
     * Check if given turn is possible.
     */
    public boolean validateTurn(int deckIndex, Point fieldIndex) {
        if (redPlayer) {
            if (plr1Cards[deckIndex].isHidden()) {
                return false; // selected empty card
            }
        } else {
            if (plr2Cards[deckIndex].isHidden()) {
                return false; // selected empty card
            }
        }
        // if passed first test...
        // now check if empty game field selected
        return gameField[fieldIndex.x][fieldIndex.y].isHidden();
	}

    /**
     * Evaluate given turn.
     * According to selection of deck card and game field position.
     */
	public boolean evaluateTurn(int deckIndex, Point fieldIndex) {
        // if valid turn
        if (validateTurn(deckIndex, fieldIndex)) {
            // set new card and color
            Color newColor;
            Card newCard;
            if (redPlayer) {
                newCard = plr1Cards[deckIndex].getCard();
                newColor = CardColors.COLOR_PLR1;
            } else {
                newCard = plr2Cards[deckIndex].getCard();
                newColor = CardColors.COLOR_PLR2;
            }
            // place in the new card
            gameField[fieldIndex.x][fieldIndex.y].update(newCard, newColor);
            // hide the card in deck
            if (redPlayer) {
                plr1Cards[deckIndex].hideCard();
                plr1Cards[deckIndex].recolor(CardColors.COLOR_DECK_DEPLEATED);
            } else {
                plr2Cards[deckIndex].hideCard();
                plr2Cards[deckIndex].recolor(CardColors.COLOR_DECK_DEPLEATED);
            }
            // possibly change up to all 4 surrounding card colors
            revalueSurroundingCards(fieldIndex, newCard, newColor);
            // recount numbers
            recountCardPossesions();
            // send info to second player
            //
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Evaluate given opponent's turn.
     * Recieved via server com.
     */
    public void evaluateOpponentsTurn(Point fieldIndex, Card newCard) {
        // we know, this is already evaluated, we just have to process the turn
        // set new card and color
        Color newColor;
        if (redPlayer) {
            newColor = CardColors.COLOR_PLR2; // inverse
        } else {
            newColor = CardColors.COLOR_PLR1; // inverse
        }
        // place in the new card
        gameField[fieldIndex.x][fieldIndex.y].update(newCard, newColor);
        // possibly change up to all 4 surrounding card colors
        revalueSurroundingCards(fieldIndex, newCard, newColor);
        // recount numbers
        recountCardPossesions();
    }

    /**
     * Check if the game ended.
     * Ends in the moment, when there is totally 25 cards owned on game field.
     */
	public boolean isEnd() {
        if (plr1Result+plr2Result>=25) {
            return true;
        } else {
            return false;
        }
	}

    // ************************** \\
    // *    PRIVATE METHODS     * \\
    // ************************** \\
    
    /**
     * Check if cards surrounding currently played card shouldn't change owner.
     */
    private void revalueSurroundingCards(Point fieldIndex, Card card, Color color) {
        // card at left (there must be one and must be occupied)
        if ((fieldIndex.x>0)&&(!gameField[fieldIndex.x-1][fieldIndex.y].isHidden())) {
            if (card.getLeft()>gameField[fieldIndex.x-1][fieldIndex.y].getCard().getRight()) {
                gameField[fieldIndex.x-1][fieldIndex.y].recolor(color);
            }
        }
        // card at right (there must be one and must be occupied)
        if ((fieldIndex.x<4)&&(!gameField[fieldIndex.x+1][fieldIndex.y].isHidden())) {
            if (card.getRight()>gameField[fieldIndex.x+1][fieldIndex.y].getCard().getLeft()) {
                gameField[fieldIndex.x+1][fieldIndex.y].recolor(color);
            }
        }
        // card at top (there must be one and must be occupied)
        if ((fieldIndex.y>0)&&(!gameField[fieldIndex.x][fieldIndex.y-1].isHidden())) {
            if (card.getTop()>gameField[fieldIndex.x][fieldIndex.y-1].getCard().getBottom()) {
                gameField[fieldIndex.x][fieldIndex.y-1].recolor(color);
            }
        }
        // card at bottom (there must be one and must be occupied)
        if ((fieldIndex.y<4)&&(!gameField[fieldIndex.x][fieldIndex.y+1].isHidden())) {
            if (card.getBottom()>gameField[fieldIndex.x][fieldIndex.y+1].getCard().getTop()) {
                gameField[fieldIndex.x][fieldIndex.y+1].recolor(color);
            }
        }
    }
        
    /**
     * Recount number of cards held by each player after a card was played.
     */
    private void recountCardPossesions() {
        int plr1 = 0;
        int plr2 = 0;
        //
        for (int i=0;i<5;i++) {
            for (int i2=0;i2<5;i2++) {
                if (gameField[i][i2].getColor()==CardColors.COLOR_PLR1) {
                    plr1++;
                } else if (gameField[i][i2].getColor()==CardColors.COLOR_PLR2) {
                    plr2++;
                }
            }
        }
        //
        this.plr1Result = plr1;
        this.plr2Result = plr2;
    }

}
