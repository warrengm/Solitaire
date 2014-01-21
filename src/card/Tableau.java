package card;

import dataStructures.Stack;

/**
 * A {@link StackOfCards} with operations based on tableaux in common forms of
 * Solitaire. The isSuitable method is based on the rules Klondike and Free Cell.
 * However, additional methods are included to meet the requirements of suitabality
 * of other forms of solitaire.
 * <p>
 * Note: Tableaux are expected to have a positive offset in the y direction but 
 * no offset x direction.
 * 
 * @author Warren Godone-Maresca
 */
public class Tableau extends StackOfCards {
	/**
	 * Instantiates an empty stack where all cards will have
	 * no size and positioned at the origin.
	 */
	public Tableau(){}

	/**
	 * Note: the height of the cards will be based on the width of cards and the
	 * dimensions of a standard card.
	 * 
	 * @param x			The x coordinate for the center of the card on the 
	 * 					bottom of the stack.
	 * @param y			The y coordinate for the center of the card on the 
	 * 					bottom of the stack.
	 * @param cardWidth	The width of each card in the stack.
	 * @param offsetY	The difference in y coordinates of a card in the stack
	 * 					with that of the card below it.
	 */
	public Tableau(int x, int y, int cardWidth, int offsetY){
		super(x, y, cardWidth, 0, offsetY);
	}

	/**
	 * Pops all cards below and containing the given y coordinate if the tableau
	 * contains the y coordinate.
	 * @param y The given y coordinate.
	 * @return All cards below the given y coordinate in a {@link Stack} if the 
	 * 		   tableau contains y else <code>null</code>.
	 */
	public Stack<Card> popCardsBelow(int y){
		if(!contains(this.x, y)) //Then y is not in the boundaries of this stack,
			return null;		//so null is returned.

		//Holds the cards to be returned.
		Stack<Card> temp = new Stack<Card>();

		int numOfCards = 1;  //One card is added to temp to start with.
		while(!peek().contains(x, y + (numOfCards-1)*offsetY) &&
				numOfCards <= size){ //counts the num.
			numOfCards++;    			//of cards below the given y coordinate
			//System.out.println(peek().contains(x, y));
			//System.out.println(y + " " + peek().getY() + " " + this.shapeOfNextCard().getCenterY());
			//System.out.println(numOfCards + " will be popped");//TODO
		}

		for(int i = 0; i < numOfCards; i++){//Move numOfCards cards from this
			temp.push(pop());				//stack to temp.
		}

		temp.reverse(); //Because cards were added out of order.
		return temp;
	}

	/**
	 * Pops all cards in order in this tableau below or containing the given 
	 * y-coordinate if:
	 * <ul>
	 * <li> The tableau contains the y coordinate.
	 * <li> The value of all cards below the y coordinate descend decrementally.
	 * <li> No two adjacent cards below the y coordinate have the same color.
	 * <li> No card below the y coordinate is hidden.
	 * </ul>
	 * If one of the conditions isn't met, <code>null</code> will be returned
	 * and the cards will not be removed.
	 * @param y The y coordinate of the first card to be popped. Must be within
	 * 			the boundaries of the stack.
	 * @return A {@link Stack} containing the all cards below the given
	 * 			y if the above conditioned are met, otherwise <code>null</code>.
	 */
	public Stack<Card> popSuitableCardsBelow(int y){
		Stack<Card> temp = popCardsBelow(y);
		if(temp == null || !isSuitable(temp)){
			//If temp is not suitable, then its cards should not be removed, 
			appendStack(temp); //so we append them back to this stack and
			return null;       //return null.
		} else {
			return temp;	   //Otherwise temp is returned.
		}
	}

	/**
	 * Unlike {@link #appendStack(dataStructures.Stack)}, this will only append 
	 * the stack if all cards in <code>stack</code> are sequentially increasing
	 * in value from the top and alternate in color. Otherwise an exception will
	 * be thrown. This is a common operation in many Solitaires for moving cards
	 * between stacks.
	 * 
	 * @param stack The stack to be appended.
	 * @throws 	IllegalArgumentException if the cards in <code>stack</code> aren't
	 * 			sequentially increasing in value from the top and alternate in color.
	 */
	public void appendSuitableCards(Stack<Card> stack){
		Card bottom = stack.reverseCopy().pop();

		/* Checks if:
		 * -the given stack alternates in color.
		 * -the given stack is in sequence with regards to value.
		 * -the bottom card of the given stack differs in color with this
		 *  tableau's top card
		 * -the bottom card's value is one less than the that of the top card.
		 * If any of these conditions isn't met, an exception is thrown.   */
		if(!isEmpty() && (this.peek().compareTo(bottom) != 1
				|| this.peek().colorEquals(bottom))){

			String message = "The given stack is not suitable.";
			throw new IllegalArgumentException(message);
		}

		appendStack(stack); //Then the given stack can be appended.
	}
	
	/**
	 * Determines whether the given stack is completely visible, alterntes in
	 * color, and is in sequence from low to high values.
	 * @param stack The given stack.
	 * @return 	<code>true</code> if the given stack is suitable,
	 * 			else <code>false</code>.
	 */
	public static boolean isSuitable(Stack<Card> stack){
		return alternatesInColor(stack) && inSequence(stack) && isVisible(stack);
	}

	/**
	 * Determines if all cards in the stack are visible and alternate in color.
	 * (the colors are ignored if any card is hidden).
	 * @param stack the stack to be checked.
	 * @return <code>true</code> if all no two adjacent
	 * 		   cards have the same color, else <code>false</code>.
	 */
	public static boolean alternatesInColor(Stack<Card> stack){
		if(stack.size() < 2){//Simple case
			return true;
		}

		//Temporary stack in which elements will be removed to be checked. Another
		Stack<Card> copy = stack.copy();//stack is made to maintain the original
		Card toCompare = copy.pop(); //To compare against other elements.

		while(!copy.isEmpty()){
			Card current = copy.pop(); //To compare against toCompare.
			//If they have the same color or current is hidden
			if(current.colorEquals(toCompare)){
				return false; //then return false.
			}
			toCompare = current; //update toCompare.
		}
		return true; //If we have reached this point, then stack is suitable.
	}
	
	/**
	 * Determines if all cards in the stack are visible and alternate in color.
	 * (the colors are ignored if any card is hidden).
	 * @param stack the stack to be checked.
	 * @return <code>true</code> if all no two adjacent
	 * 		   cards have the same color, else <code>false</code>.
	 */
	public static boolean isVisible(Stack<Card> stack){
		Stack<Card> copy = stack.copy();
		while(!copy.isEmpty()){
			if(copy.pop().isHidden()){
				return false;
			}
		}
		return true; //If we have reached this point, then stack is suitable.
	}

	/**
	 * Checks if the values of the cards in the given stack is in sequence from
	 * low to high.
	 * @param stack The given stack.
	 * @return 	<code>true</code> if the given stack is in sequence, 
	 * 			else <code>false</code>.
	 */
	public static boolean inSequence(Stack<Card> stack){
		if(stack.size() < 2){//Simple case
			return true;
		}

		//Temporary stack in which elements will be removed to be checked. Another
		Stack<Card> copy = stack.copy();//stack is made to maintain the original
		Card toCompare = copy.pop(); //To compare against other elements.

		while(!copy.isEmpty()){
			Card current = copy.pop(); //To compare against toCompare.
			//If they aren't sequentially ordered:
			if(current.compareTo(toCompare) != 1){
				return false; //then return false.
			}
			toCompare = current; //update toCompare.
		}
		return true; //If we have reached this point, then stack is in sequence.
	}
}