package solitaire;

import java.awt.Container;
import java.awt.event.MouseEvent;

import card.Card;
import card.Foundation;
import card.StackOfCards;
import card.Tableau;
import dataStructures.Stack;

/**
 * A kind of Solitaire that is similar to {@link Klondike} except that two decks
 * are used, there are more tableaux, and cards aren't turned over to the waste
 * when the stock is clicked. Rather, a card is added to the top of each tableau
 * from left to right from the stock. 
 * Additionally, cards aren't directly sorted into the foundations by the user. 
 * Instead the user sorts the cards in the tabelaux and when the top cards in a 
 * tableau form a completed sequence of cards from ace to king. Once a substack
 * of a tableau is sorted from king to ace, the user must move the entire substack
 * to a foundation.
 * The gameplay of this form of Spider Solitaire is equivalent to the 
 * traditional 1 or 2-deck spider. However, unlike traditional 2-deck Spider,
 * the colors of cards must alternate to be placed on each other 
 * (just like the other forms of Solitaire in this program).
 * 
 * @author Warren Godone-Maresca
 */
public class Spider extends Klondike {

	/** Whether or not the the game should be easy. If the game is easy, then the
	 * color of cards is ignored, otherwise the cards' colors must alternate. */
	private boolean easy;

	/**
	 * Instantiates the game and the panel. Two suits will be used in the game
	 * when this constructor is used (medium difficulty).
	 * 
	 * @param container The container for the game.
	 * @param easy		Whether or not the the game should be easy. If the game
	 * 					is easy, then the color of cards is ignored, otherwise
	 * 					the cards' colors must alternate.
	 */
	public Spider(Container container, boolean easy){
		super(container);
		this.easy = easy;
	}

	/**
	 * Initializes all of the stacks.
	 */
	@Override
	protected void init(){
		StackOfCards deck = new StackOfCards();
		deck.fillBySuit();	
		deck.fillBySuit(); //Holds 104 cards.
		deck.shuffle();

		initTableaux(deck, new int[] {6, 6, 6, 6, 5, 5, 5, 5, 5, 5});
		initFoundations(8);
		initStockAndWaste(deck);

		initialized = true;
		container.repaint();
	}

	/**
	 * Just initializes the stock as the waste isn't used.
	 * Pre: the tableaux have been initialized.
	 */
	@Override
	protected void initStockAndWaste(StackOfCards deck){
		stock = new StackOfCards(tableaux[0].getX(), yCoord, cardWidth, 0, 0);
		stock.appendStack(deck); //The stock contains all of its cards.
		stock.peek().setHidden(true); //So that the stock is hidden.
	}


	/**
	 * Determines if the given stack of cards is suitable to be removed from a
	 * tableau (assuming that it originated from a tableau).
	 * @return 	<code>true</code> if the given stack is visible, in sequence,
	 * 			and, if not {@link #easy}, that the cards alternate in color.
	 */
	@Override
	protected boolean removableFromTableaux(Stack<Card> cards) {
		if(!easy){
			return super.removableFromTableaux(cards);
		} else {
			return cards != null 
					&& Tableau.isVisible(cards)
					&& Tableau.inSequence(cards);
		}
	}

	/**
	 * Adds a card to each of the tableaux from the stock as long as the stock
	 * has cards and if the stock contains the given location.
	 * @return 	<code>true</code> if the stock contains the given location, else
	 * 			<code>false</code>.
	 */
	@Override
	protected boolean stockPressedAction(int x, int y){
		if(!stock.contains(x, y)){
			return false;
		}

		for(Tableau tableau : tableaux){
			if(stock.isEmpty()) //If the stock is empty, then there are no cards
				break;			//to move to a tableau.

			stock.peek().setHidden(false);
			super.animateTopCardOf(stock, tableau);
		}
		if(!stock.isEmpty()){
			stock.peek().setHidden(true);
		}
		container.repaint();
		return true;
	}

	/**
	 * If a tableau in {@link #tableaux} contains the given coordinates and the 
	 * cards in {@link #inUse} is suitable to be appended, then
	 * the cards inUse will be appended to the tableau and inUse will be cleared.
	 * 
	 * @param x		The x coordinate of a mouse click.
	 * @param y		The y coordinate.
	 * @return <code>true</code> if the action above was performed, 
	 * 			else <code>false</code>
	 */
	protected boolean tableauxReleasedAction(int x, int y){
		if(!easy){ //Then the colors must alternate, which Klondike ensures.
			return super.tableauxReleasedAction(x, y);
		}

		for(Tableau tableau : tableaux){ //Check each of the tableaux
			if(tableau.contains(x, y) || tableau.shapeOfNextCard().contains(x, y)){
				//Then we check if the inUse stack can be appended to the
				//tableau per the rules of solitaire.

				//We do not need to check if the cards in use are in sequence
				//because they must already be in sequence from tabPressedAction.

				//Checks if bottom of this tableau is in sequence with the cards
				//in use.
				if(!tableau.isEmpty() && 
						inUse.reverseCopy().peek().compareTo(tableau.peek()) != -1){
					return false;
				}

				tableau.appendStack(inUse);
				inUse.clear();
				container.repaint();
				flipLastStack();
				return true;
			}
		}
		return false;//If we have reached this point, then no action was performed
	}

	/**
	 * Performs the actions associated with the stack that is pressed.
	 */
	@Override
	public void mousePressed(MouseEvent e){
		int x = e.getX(), y = e.getY();

		if(inUse.isEmpty() && !stockPressedAction(x, y)){
			//Do the tableaux action if the stock action wasn't done.
			tableauxPressedAction(x, y);
		}
	}

	/**
	 * Appends the cards in use to first available foundation if the cards in use
	 * are in sequence from ace to king.
	 * Note: The mouse can be released anywhere, the parameters were inherited
	 *       but are unused here.
	 *       
	 * @return 	<code>true</code> if the cards were in sequence and added to a
	 * 			foundation, otherwise <code>false</code>.
	 */
	@Override
	protected boolean foundationsReleasedAction(int x, int y){
		if(inUse.size() < 13 || inUse.peek().getValue() != 1){
			//Then the cards are not in sequence from ace to king.
			return false;
		} else {
			for(Foundation foundation : foundations){

				if(foundation.isEmpty()){ //If that foundation is empty,
					//Then we append each card in use to it.
					while(!inUse.isEmpty()){
						animateTopCardOf(inUse, foundation);
					}
					flipLastStack(); //Flips the top card of the last stack.
					return true; //The action was performed.
				}
			}
		}
		return false; //The action was not performed.
	}

	/**
	 * Determines if the user has won (if all foundations are nonempty).
	 */
	public  boolean hasWon(){
		for(Foundation foundation : foundations){
			if(foundation.isEmpty()){ //Then there exists an empty foundation.
				return false;
			}
		}
		return true; //Then no foundation is empty.
	}
}