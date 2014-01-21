package card;
/**
 * A foundation is a {@link StackOfCards} in which all cards must be of the same
 * suit and each card's value is 1 more than that of the card below it. The bottom
 * card must be an ace.
 * @author Warren Godone-Maresca
 */
public class Foundation extends StackOfCards {
	/**
	 * Instantiates an empty <code>Foundation</code> where all cards will have
	 * no size and positioned at the origin.
	 */
	public Foundation(){}

	/**
	 * Instantiates an empty foundation with given values.<p>
	 * 
	 * Note: the height of the cards will be based on the width of cards and the
	 * dimensions of a standard card.
	 * 
	 * @param x			The x coordinate for the center of the card on the 
	 * 					bottom of the stack.
	 * @param y			The y coordinate for the center of the card on the 
	 * 					bottom of the stack.
	 * @param cardWidth	The width of each card in the stack.
	 */
	public Foundation(int x, int y, int cardWidth){
		super(x, y, cardWidth, 0, 0);
	}

	/**
	 * Adds a card to the top of the stack. If this stack was previously empty,
	 * then <code>card</code> must be an ace (have a value of 1), otherwise
	 * <code>card</code> must be 1 greater than the value of the card already on
	 * the top of this stack and have the same suit.
	 * @throws IllegalArgumentException if <code>card</code> does not meet the
	 * 			above conditions.
	 */
	@Override
	public void push(Card card){
		if(isEmpty()){                //If this is empty,
			if(card.getValue() == 1){ //then the card must be an ace to be pushed.
				super.push(card);
			} else {
				throw new IllegalArgumentException();//TODO ""
			}
		} else {
			//Otherwise the card's value must be 1 greater than the top card
			//and be of the same suit.
			if(card.getValue() == peek().getValue() + 1 
					&& card.getSuit() == peek().getSuit()){
				super.push(card);
			} else {
				throw new IllegalArgumentException();
			}
		}
	}	
}