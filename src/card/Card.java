package card;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

/**
 * This class represents a drawable playing card. All cards have a {@link Suit}
 * and value. Values are ranked from Ace (value = 1) to King (value = 13). 
 * Cards are immutable with respect to suit and value. Cards are also comparable
 * with respect to value.<p>
 * 
 * All cards are drawn simply, the value and suit are drawn in the top left and
 * bottom right corners and the suit drawn again at the center.
 * 
 * @author Warren Godone-Maresca
 */
public class Card implements Comparable<Card> {
	/** The suit of the card.												*/
	private final Suit SUIT;

	/** The card's value as an integer. Values between 2 and 10 inclusive 
	 *  represent that number, values of 1 represents ace, 11 - Jack, 12 - Queen,
	 *  and 13 - King.														*/
	private final int VALUE;

	/**	Holds whether or not the card is hidden from the user. If it is hidden,
	 * 	the back of the card will be drawn, otherwise, the front.			*/
	private boolean hidden;

	/** The card's center's coordinates.							  		*/
	private int x, y;

	/** The card's width and height.										*/
	private int width, height;

	/**
	 * Instantiates the card to be the ace of spades at the origin with no
	 * dimensions and not hidden.
	 */
	public Card(){
		this(Suit.SPADES, 1, 0, 0, 0, false);
	}

	/**
	 * Instantiates the card with a given suit, value, location, and dimension;
	 * and whether or not the card is hidden from the user.
	 *  
	 * @param suit	The card's suit.
	 * @param value The card's value between 1 and 13 inclusive. A value of 1
	 * 				represents Ace, 11 represents Jack, 12 represents Queen, and
	 * 				13 represents king. All other numbers in the range represent
	 * 				the corresponding number card.
	 * @param x 	The center x coordinate.
	 * @param y 	The center y coordinate.
	 * @param width	The width of the card in px. The card's height will be
	 * 				calculated from the value of width and be based roughly on 
	 * 				standard card dimensions. So the card's height will be 1.5 
	 * 				times the given width.
	 * @param hidden Whether or not the card will be oriented to the user when
	 * 				 drawn.
	 * @throws IllegalArgumentException if <code>value</code> < 1 OR 
	 * 										<code>value</code> > 13
	 */
	public Card(Suit suit, int value, int x, int y, 
			int width, boolean hidden){

		if(value < 1 || value > 13){
			throw new IllegalArgumentException("Value out of range.");
		}

		setSize(width);
		this.x = x;
		this.y = y;
		this.SUIT = suit;
		this.VALUE = value;
		this.hidden = hidden;
	}

	/**
	 * Returns the card's suit.
	 */
	public Suit getSuit(){
		return SUIT;
	}

	/**
	 * Returns the card's value.
	 * @return 	The integer value card's of the card's value. Number cards return
	 * 			the number, Aces return 1, Jacks return 11, Queens return 12,
	 * 			and Kings return 13.
	 */
	public int getValue(){
		return VALUE;
	}

	/**
	 * Flips the orientation of the card. If the card was hidden before calling
	 * this method, it won't be afterwards. And if it was not hidden, then it
	 * will be hidden after the call.
	 */
	public void flip(){
		hidden = !hidden;
	}

	/**
	 * Returns whether or not this card is hidden.
	 * @return <code>true</code> if hidden, else <code>false</code>.
	 */
	public boolean isHidden(){
		return hidden;
	}

	/**
	 * Sets whether or not the card will be hidden when drawn.
	 * @param hidden The boolean value for hidden.
	 */
	public void setHidden(boolean hidden){
		this.hidden = hidden;
	}

	/**
	 * Returns the x coordinate of the middle of the card.
	 */
	public int getX(){
		return x;
	}

	/**
	 * Returns the y coordinate of the middle of the card.
	 */
	public int getY(){
		return y;
	}

	/**
	 * Sets the coordinates of the center of the card.
	 * @param x The center x coordinate.
	 * @param y The center y coordinate.
	 */
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the size of the card based on the given width. The height of the card
	 * will be based roughly on standard card dimensions. So the card's height
	 * is 1.5 times the given width.
	 * @param width The width of the card in px.
	 */
	public void setSize(int width){
		this.width = width;
		height = width * 3/2;
	}

	/**
	 * Draws the back of the card in which the suit and value is hidden.
	 */
	private void drawBack(Graphics pane){
		//Draws the blue center,
		pane.setColor(new Color(0, 0, 150));
		pane.fillRoundRect(x - width/2 + width/20, y - height/2 + height/20, 
				width*9/10, height*9/10, width/10, height/10);
	}


	/**
	 * Draws the front of the card with the suit and value.
	 */
	private void drawFront(Graphics pane){
		//Draws the value character in the top left corner.
		pane.setColor(SUIT.getColor());
		pane.setFont(new Font("Monospaced", Font.BOLD, width/4));
		pane.drawString(valueToString(), (int)(x - width*2/5), (int)(y - height/3));

		//Then the bottom right corner.
		pane.setFont(new Font("Monospaced", Font.BOLD, -width/4));
		pane.drawString(valueToString(), (int)(x + width*2/5), (int)(y + height/3));

		SUIT.draw(pane, x, y, width/3); //Draws the suit in the center,
		SUIT.draw(pane, x - width/3 + 2, y - height/5, width/5); //top left,
		SUIT.draw(pane, x + width/3 - 2, y + height/5, -width/5);//& bottom right
	}

	/**
	 * Draws the front of the card with the suit and value if it is not hidden,
	 * otherwise the back of the card will be drawn.
	 */
	public void draw(Graphics pane){
		//Draws the shape of the card.
		pane.setColor(Color.WHITE);
		pane.fillRoundRect(x - width/2, y - height/2, 
				width, height, width/10, height/10);

		//Draws the thin black outline
		pane.setColor(Color.BLACK);
		pane.drawRoundRect(x - width/2, y - height/2, 
				width, height, width/10, height/10);

		if(hidden){
			drawBack(pane);
		} else { 
			drawFront(pane);
		}
	}

	/**
	 * Returns the card's value as a String.
	 * @return The integer value of the card as a String if 1 < value < 11. Else,
	 * "A" if value == 1, "J" if value == 11, "Q" if value == 12, "K" if 
	 * value == 13. These letters represent ace or a face card respectively. 
	 */
	private String valueToString(){
		switch(VALUE){
		case 1: return "A";	 //Ace
		case 11: return "J"; //Jack
		case 12: return "Q"; //Queen
		case 13: return "K"; //King
		default: return Integer.toString(VALUE);//Number card.
		}
	}

	/**
	 * Returns whether or not some given coordinates are within the outline of
	 * the card.
	 * @param x Some x coordinate.
	 * @param y Some y coordinate.
	 * @return <code>true</code> if the given coordinates are inside this card,
	 * otherwise <code>false</code>.
	 */
	public boolean contains(int x, int y){
		return getShape().contains(x, y);
	}

	/**
	 * Returns the {@link Shape} of the card.
	 * @return A new instance of {@link RoundRectangle2D.Double} with the 
	 * coordinates and dimensions of this card.
	 */
	public Shape getShape(){
		return new RoundRectangle2D.Double(x - width/2, y - height/2, 
				width, height, width/10, width/10);
	}

	/**
	 * Compares this card with some other card. Per the specifications, a negative
	 * integer, zero, or a positive integer will be returned if this card is less
	 * than, equal to, or greater than the given card respectively. Specifically,
	 * the number returned is this card's value minus the given card's value. 
	 */
	@Override
	public int compareTo(Card card) {
		return VALUE - card.getValue();
	}

	/**
	 * Compares the color of this card with some other card.
	 * @param card The card to be compared with.
	 * @return <code>true</code> if this card and the other card have the same
	 * color, otherwise <code>false</code>.
	 */
	public boolean colorEquals(Card card){
		return SUIT.getColor() == card.getSuit().getColor();
	}
}