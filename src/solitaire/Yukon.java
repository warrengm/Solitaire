package solitaire;

import java.awt.Container;
import java.awt.event.MouseEvent;

import card.Card;
import card.Foundation;
import card.StackOfCards;
import card.Tableau;
import dataStructures.Stack;

/**
 * A form of Solitaire that is similar to {@link Klondike}. Unlike Klondike,
 * there isn't a stock and all cards are dealt into the tableaux. Additionally,
 * cards do not have to be in sequence with alternating colors to be removed
 * from a tableaux. Moreover, only the bottom card in use and the top card of a
 * tableau is relevant for appending the cards in use to said tableau column.
 * 
 * @author Warren Godone-Maresca
 *
 */
public class Yukon extends Klondike {

	/** Do nothing constructor.												*/
	public Yukon(){}

	/**
	 * Instantiates the game with a {@link Container}.
	 * @param container The Container (such as window or applet) in which the 
	 * 					game will be played.
	 */
	public Yukon(Container container){
		super(container);
	}

	/**
	 * Initializes the game's stacks.
	 */
	@Override
	protected void init(){
		//The initial deck.
		StackOfCards deck = StackOfCards.randomDeck();

		//Calls initTableaux with the random deck and an anonymous array that
		//holds the initial tableau sizes.
		initTableaux(deck, new int[] {1, 6, 7, 8, 9, 10, 11});
		initFoundations(4);

		initialized = true; //Everything is initialized,
		container.repaint();//So we repaint.
	}

	/**
	 * Initializes the tableaux.
	 */
	@Override
	protected void initTableaux(StackOfCards source, int[] initialTableauxSizes){
		//Sets the number of tableau columns.
		tableaux = new Tableau[initialTableauxSizes.length];

		//Initializes each tableau
		for(int i = 0; i < tableaux.length; i++){
			//Instantiates each tableau
			tableaux[i] = new Tableau(
					(cardWidth+10)*(i+1), yCoord + cardWidth*2, cardWidth, offset);

			for(int j = 0; j < initialTableauxSizes[i]; j++){ //Moves cards from
				tableaux[i].push(source.pop());          //source to tableau

				if(j > initialTableauxSizes[i] - 6){ //We show the top 6 cards
					tableaux[i].peek().setHidden(false);
				} else {
					tableaux[i].peek().setHidden(true);
				}
			}
		}
	}

	/**
	 * Determines if the given stack of cards is suitable to be removed from a
	 * tableau (assuming that it originated from a tableau).
	 * @return 	<code>true</code> if all cards in the given stack is visible.
	 */
	@Override
	protected boolean removableFromTableaux(Stack<Card> cards){
		return Tableau.isVisible(cards);
	}

	/**
	 * Checks if the user has won, and if they have'nt, then performs
	 * the tableaux pressed action.
	 */
	@Override
	public void mousePressed(MouseEvent e){
		if(hasWon()){				//If the user has won,
			container.repaint();	//repaint and
			onWin();				//perform the on win action
			return;
		} else if(inUse.isEmpty()){
			tableauxPressedAction(e.getX(), e.getY());
		}
	}

	/**
	 * Releases the cards in use to the tableau that contains the given coordinates
	 * if the top card of that tableau and the bottom card in use are in
	 * sequence and have different colors.
	 * @return <code>true</code> if the cards in use have been appended to a 
	 * 		tableau (the above conditions have been met), else <code>false</code>.
	 */
	@Override
	protected boolean tableauxReleasedAction(int x, int y) {
		for(Tableau tableau : tableaux){ //Check each of the tableaux
			if(tableau.contains(x, y) || tableau.shapeOfNextCard().contains(x, y)){
				//Then we check if the inUse stack can be appended to the
				//tableau per the rules of solitaire.

				//Checks if bottom of this tableau is in sequence with the cards
				//in use and the colors alternate. If not, then we return false.
				Card bottom = inUse.reverseCopy().peek();
				if(!tableau.isEmpty() &&
						(bottom.compareTo(tableau.peek()) != -1
						|| bottom.colorEquals(tableau.peek()))){
					return false;
				}

				tableau.appendStack(inUse); //Else we append the cards in use.
				inUse.clear();
				container.repaint();
				flipLastStack();
				return true;
			}
		}
		return false;//If we have reached this point, then no action was performed
	}

	/**
	 * Determines if the user has won.
	 * @return 	<code>true</code> if each foundation has at least one card and
	 * 			4 or fewer tableaux have cards and those cards are sorted and
	 * 			not hidden.
	 */
	@Override
	protected boolean hasWon(){
		for(Foundation f : foundations){
			if(f.isEmpty()){
				return false; //a foundation is empty so the user hasn't won.
			}
		}
		
		int numOfNonEmptyTableaux = 0;
		for(Tableau tableau : tableaux){ //Checks each tableau if it is suitable.
			if(!Tableau.inSequence(tableau) || !Tableau.isVisible(tableau)){
				//Then the tableau is not suitable,
				return false;				  //the user has not won.
			} else if(tableau.size() != 0){
				numOfNonEmptyTableaux++;
			}
		}
		
		//If there are fewer than 4 nonempty tableaux, and the stock and waste
		//are empty, then the user has effectively won.
		return numOfNonEmptyTableaux <= 4;
	}
}