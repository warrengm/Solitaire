package card;

import dataStructures.Stack;

/**
 * A {@link StackOfCards} that holds one card. Holding cells are used in several
 * forms of solitaire.
 * 
 * @author Warren Godone-Maresca
 *
 */
public class HoldingCell extends StackOfCards {
	/**
	 * Instantiates an empty <code>HoldingCell</code> where the card will have
	 * no size and be positioned at the origin.
	 */
	public HoldingCell(){}

	/**
	 * Instantiates an empty stackOfCards with given values.<p>
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
	public HoldingCell(int x, int y, int cardWidth){
		super(x, y, cardWidth, 0, 0);
	}

	/**
	 * Adds the given card to this stack. The card will then be the only card
	 * in the stack afterwards.
	 */
	public void push(Card card){
		clear();
		super.push(card);
	}

	/**
	 * Appends a given stack if it contains only 1 element.
	 * 
	 * @throws IllegalArgumentException if <code>stack</code> contains more than
	 * 			one element.
	 */
	public void appendStack(Stack<Card> stack) {
		if(stack.size() < 2){
			super.appendStack(stack);
		} else {
			throw new IllegalArgumentException();//TODO
		}
	}
}