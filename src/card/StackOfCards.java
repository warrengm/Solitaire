package card;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import dataStructures.Stack;

/**
 * A {@link Stack} of {@link Card}s. This class has all of the functionality of
 * a typical stack ADT (push, pop, etc.) with some additional features for cards.
 * <p>
 * A <code>StackOfCards</code> object has coordinates for the bottom card in the
 * stack and offset value to set the location of a card relative the card below
 * it in the stack. Additionally, all cards in the stack will have the same size.
 * <p>
 * An empty stack can be instantiated with a constructor. Additionally,
 * the static method <code>randomDeck()</code> can be used to return a new 
 * <code>StackOfCards</code> object filled with 52 cards in random order. Although
 * all cards in that stack will have no size and be located at the origin. However,
 * these values can be set later with appropriate set methods.
 * 
 * @author Warren Godone-Maresca
 */
public class StackOfCards extends Stack<Card> {

	/** The coordinates of the center of the bottom card.					*/
	protected int x, y;

	/** The width of all cards in the stack.								*/
	protected int cardWidth;

	/** The difference in x and y coordinates respectively of a card in the stack
	 * with the card below it. The bottom card will have coordinates (x,y).	 */
	protected int offsetX, offsetY;

	/**
	 * Instantiates an empty <code>StackOfCards</code> where all cards will have
	 * no size and be  positioned at the origin.
	 */
	public StackOfCards(){}

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
	 * @param offsetX	The difference in x positions of a card in the stack and
	 * 					the card below it. If offsetX is passed as 0, then all 
	 * 					cards will be placed directly on top of each other. If >0,
	 * 					then the cards will be placed to right of the previous 
	 * 					card.
	 * @param offsetY	The difference in y coordinates of a card in the stack
	 * 					with that of the card below it.
	 */
	public StackOfCards(int x, int y, int cardWidth, int offsetX, int offsetY){
		this.x = x;
		this.y = y;
		this.cardWidth = cardWidth;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	/**
	 * Returns a new <code>StackOfCards</code> object with 52 cards where all cards
	 * are at the origin with no size and are not hidden.
	 */
	public static StackOfCards randomDeck(){
		StackOfCards deck = new StackOfCards();
		deck.fillBySuit();
		deck.shuffle();           //then shuffled.
		return deck;
	}

	/**
	 * Adds <code>card</code> to this stack. The card's x coordinate will be
	 * <code>x</code> + (<code>offsetX</code>) * (the previous size). The y
	 * coordinate will be set similarly (with <code>offsetY</code> used
	 * instead). The size of <code>card</code> will be set according to 
	 * <code>cardWidth</code>.
	 */
	public void push(Card card){
		//Note: size has not been incremented at this point, it will be incremented
		//in the super method.
		//The location of the card is changed to match the stack.
		card.setLocation(x + offsetX*size, y + offsetY*size);
		card.setSize(cardWidth); //And so is the size.
		super.push(card);
	}

	/**
	 * Adds 52 cards by suit.
	 */
	public void fillBySuit(){
		for(Suit suit : Suit.values()){
			for(int i = 1; i < 14; i++){
				push(new Card(suit, i, cardWidth, x, y, false));
			}
		}
	}
	
	
	/**
	 * Reverses the stack and sets each of the card's location accordingly. The
	 * new bottom card will be located at (x, y).
	 */
	public void reverse(){
		super.reverse();
		setLocation(x, y); //The location of each card is updated.
	}

	/**
	 * Reorders all of the cards randomly. The positions of the cards will be
	 * changed accordingly.
	 */
	public void shuffle(){
		//First merge shuffle is performed and the Knuth/Fisher-Yates shuffle is.
		//done. After several tests, these shuffles in this order had the most
		//uniform odds of some permutation of the cards being selected.
		head = knuthShuffle(mergeShuffle(head));
		setLocation(x, y);
	}

	/**
	 * Shuffles the deck by randomly by performing the Knuth/Fisher-Yates shuffle.
	 * 
	 * @param head 	The head of the list to be shuffled. For this method, it
	 * 				is intended for node to be head.
	 * @return A node that heads a shuffled deck.
	 */
	private Node<Card> knuthShuffle(Node<Card> head){
		if(size < 2) //Then no shuffling needs to be performed.
			return head;

		int tempSize = size;//The number of nodes that still need to be shuffled.
		
		//For each node in the list:
		for(Node<Card> node = head; node.getNext() != null; node = node.getNext()){
			
			Node<Card> randomNode = node; //A random node.
			
			//The position of the random node relative to node.
			int numOfIterations = (int)(Math.random() * tempSize--);
			
			//Advances the random node to its position.
			for(int i = 0; i < numOfIterations; i++){
				randomNode = randomNode.getNext();
			}	
			swap(node, randomNode); //And swap node with randomNode
		}
		return head;
	}
	
	
	/**
	 * Swaps the data in node1 and node2 between each other.
	 */
	private void swap(Node<Card> node1, Node<Card> node2){
		Card tempCard = node1.getValue();	
		node1.setValue(node2.getValue());
		node2.setValue(tempCard);
	}

	/**
	 * Shuffles the given list/node by recursively dividing it into two sublists
	 * of equal length
	 * @param node
	 * @return
	 */
	private Node<Card> mergeShuffle(Node<Card> node){
		//Base case. 
		if(node== null || node.getNext() == null){ //Then there does exists
			return node;					 //another permutation for the list.
		}
		
		Node<Card> headL = node;		//Heads the left list.

		Node<Card> slowCounter = node;	//Holds the element before the middle element
		Node<Card> fastCounter = node.getNext();	//To iterate through the list.

		//The following loop finds the middle node. Slow counter will point to
		//the middle node.
		//We will advance the fastCounter twice and slowCounter once in each
		//iteration. When fastCounter is null, slowCounter will be pointing to
		//the middle of node.
		while(fastCounter != null && fastCounter.getNext() != null){
			slowCounter = slowCounter.getNext();
			fastCounter = fastCounter.getNext().getNext(); //advanced twice.
		}

		Node<Card> headR = slowCounter.getNext();//The right sublist.
		slowCounter.setNext(null); //Severs the origin list into two.
		headL = mergeShuffle(headL); //Shuffle the left.
		headR = mergeShuffle(headR); //Shuffle the right
		return randomizedMerge(headL, headR); //And merge them.
	}

	/**
	 * Merges the two given lists in random order. For best results (most random
	 * order), the two given lists should have similar sizes.
	 * @param left  One of the lists to be merged.
	 * @param right The other list to be merged.
	 * @return The merged list.
	 */
	private Node<Card> randomizedMerge(Node<Card> left, Node<Card> right){
		if(left == null) //Then there is no merging to be done.
			return right;
		if(right == null)
			return left;

		Node<Card> randomHead = null; //Heads the merged list.

		if(Math.random() <= 0.5){ //Randomly selects the left or right list.
			randomHead = left;
			randomHead.setNext(randomizedMerge(left.getNext(), right));
		} else {
			randomHead = right;
			randomHead.setNext(randomizedMerge(left, right.getNext()));
		}
		return randomHead;
	}


	/**
	 * Sets the location of all cards in the stack according to the existing offset
	 * and the given (x,y) coordinates.
	 * @param x The x coordinate of the center of the bottom card.
	 * @param y The y coordinate of the center of the bottom card.
	 */
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
		//Then the location of all elements are updated.
		int i = 0;
		for(Node<Card> node = head; node != null; node = node.getNext()){
			node.getValue().setLocation( //Continued next line.
					x + (size- i - 1)*offsetX, y + (size - i - 1)*offsetY);
			i++;
		}
	}

	/**
	 * Sets the offset between a card in the stack and the card below in both
	 * dimensions. An offset of 0 will give all cards the same x and/or y 
	 * coordinate. An offset >0 will have a card located to the right and/or lower
	 * than the card underneath it in the stack (depending on x and y respectively).
	 * An offset <0 will do the reverse, the card will be to the left and/or above.
	 * 
	 * @param offsetX	The offset in the x direction.
	 * @param offsetY	The offset in the y direction.
	 */
	public void setOffset(int offsetX, int offsetY){
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		setLocation(x, y); //The location is update to match the new offset.
	}

	/**
	 * Determines whether a given point is within the stack.
	 * @param x The point's x coordinate.
	 * @param y The point's y coordinate.
	 * @return <code>true</code> if at least one card contains the point, else
	 *			<code>false</code>.
	 */
	public boolean contains(int x, int y){
		//Each card is checked to see if it contains the given location.
		for(Node<Card> node = head; node != null; node = node.getNext()){
			if(node.getValue().contains(x, y))
				return true;
		}
		return false;
	}

	/**
	 * Draws all of the cards in order of the stack. A card will be drawn
	 * on top of all cards below it in the stack.
	 * But if the stack is empty, the shape of a card will be drawn where a card
	 * would be located if added and with the corresponding size.
	 */
	public void draw(Graphics pane){
		if(isEmpty()){
			drawOutlineOfNextCard(pane);
		} else {
			draw(pane, head);	
		}
	}

	/**
	 * Draws all cards below a given node containing a card.
	 */
	private void draw(Graphics pane, Node<Card> node){
		if(node != null){
			draw(pane, node.getNext());
			node.getValue().draw(pane);
		}
	}

	/**
	 * Returns the shape of a card where a card will be located if added
	 * and with the dimensions of this stack's cards.
	 * @return A {@link RoundRectangle2D} in the shape and location of a
	 * 			card.
	 */
	public RoundRectangle2D.Double shapeOfNextCard(){
		return new RoundRectangle2D.Double(
				x - cardWidth/2 - offsetX*size, y - cardWidth*3/4 + offsetY*size, 
				cardWidth, cardWidth*3/2, cardWidth/10, cardWidth/10);
	}

	/**
	 * Draws the {@link #shapeOfNextCard()} in light gray.
	 */
	public void drawOutlineOfNextCard(Graphics pane){
		pane.setColor(Color.LIGHT_GRAY);
		((Graphics2D)pane).fill(shapeOfNextCard());
	}

	/**
	 * Returns the x coordinate of where the center bottom card would be.
	 */
	public int getX(){
		return x;
	}

	/**
	 * Returns the y coordinate of where the center bottom card would be.
	 */
	public int getY(){
		return y;
	}
}