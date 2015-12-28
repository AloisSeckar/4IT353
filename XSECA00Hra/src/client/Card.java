package client;

//import java.awt.Color;
import java.util.Random;

/**
 *  Card - Container holding info about card values.
 *
 * @author Alois Sečkár (xseca00@vse.cz)
 * @version 1.0
 */
public class Card {
    
    // ************************** \\
    // *        CONSTANTS       * \\
    // ************************** \\

    // ************************** \\
    // *       PROPERTIES       * \\
    // ************************** \\

    /**
     * Value at left side of the card.
     */
	private int left;
    
    /**
     * Value at right side of the card.
     */
	private int right;
    
    /**
     * Value at top side of the card.
     */
	private int top;
    
    /**
     * Value at bottom side of the card.
     */
	private int bottom;

    // ************************** \\
    // *      CONSTRUCTORS      * \\
    // ************************** \\
    
    /**
     * Plain constructor.
     */
    public Card() {
        this(0,0,0,0);
    }
    
    /**
     * Constructor buliding card from given values.
     */
    public Card(int left, int right, int top, int bottom) {
        this.left   = left;
        this.right  = right;
        this.top    = top;
        this.bottom = bottom;
    }

    // ************************** \\
    // *     ACCESS METHODS     * \\
    // ************************** \\

    /**
     * Getting value at left side.
     */
    public int getLeft() {
        return left;
    }

    /**
     * Getting value at right side.
     */
    public int getRight() {
        return right;
    }

    /**
     * Getting value at top side.
     */
    public int getTop() {
        return top;
    } 
    
    /**
     * Getting value at bottom side.
     */
    public int getBottom() {
        return bottom;
    }

    // ************************** \\
    // *     PUBLIC METHODS     * \\
    // ************************** \\
    
    /**
     * True if there are no values set (= all sides are "0").
     */
    public boolean checkIfVoid() {
        if ((this.left==0)&&(this.right==0)&&(this.top==0)&&(this.bottom==0)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Gives card very random values.
     * The only limit is their sum must be >=4 and <=20.
     */
    public void randomize() {
        Random rand = new Random(System.nanoTime());
        int newLeft;
        int newRight;
        int newTop;
        int newBottom;
        int sum;
        //
        do {
            newLeft   = rand.nextInt(10);
            newRight  = rand.nextInt(10);
            newTop    = rand.nextInt(10);
            newBottom = rand.nextInt(10);
            sum = newLeft + newRight + newTop + newBottom;
        } while ((sum<4)||(sum>20));
        //
        this.left   = newLeft;
        this.right  = newRight;
        this.top    = newTop;
        this.bottom = newBottom;
    }

    // ************************** \\
    // *    PRIVATE METHODS     * \\
    // ************************** \\
}
