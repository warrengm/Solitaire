package solitaire;

import java.awt.Component;

import card.StackOfCards;

/**
 * Moves a {@link StackOfCards} from a source location to a the location of a 
 * destination {@link StackOfCards}, then upon arrival, appends (then clears) 
 * the stack to be moved to the destination stack. This class does not do the 
 * actual animation but does move the stack (in a new {@link Thread}).
 * 
 * @author Warren Godone-Maresca
 */
public class StackOfCardsAnimator implements Runnable {
	
	/** The stack to be moved.												*/
	private StackOfCards cards;
	
	/** The destination stack.												*/
	private StackOfCards destination;

	/** The position of cards while being moved in double precision.		*/
	private double x, y;

	/** The velocity of the stack being moved.								*/
	private double dx, dy;

	/** The acceleration of the stack being moved.							*/
	private double accelerationX, accelerationY;

	/** The component to be repainted.										*/
	private Component component;

	/**
	 * Instantiates the animation. The stack <code>cards</code> will be moved
	 * (with acceleration) towards the destination. Then the when <code>cards
	 * </code> arrives to the destination, it will be appended to <code>
	 * destination</code>. Afterwards, <code>cards</code> will be cleared of its
	 * elements. The position of <code>destination</code> will be unmodified.
	 * Additionally, all of the cards in <code>cards</code> will be moved 
	 * together and will be appended in the original order.
	 * <p>
	 * If a non-null component is given, then the component will be repainted
	 * in each step.
	 * 
	 * @param cards			The stack to be moved and appended.
	 * @param destination	The stack to receive the cards.
	 * @param component		The component to be repainted (if it is given to be
	 * 						<code>null</code>, then no repainting will occur.
	 * @throws NullPointerException if either stack of cards is null.
	 */
	public StackOfCardsAnimator(StackOfCards cards, StackOfCards destination,
			Component component){
		this.cards = cards;
		this.destination = destination;
		
		//The new position of cards.
		int destinationX = (int)destination.shapeOfNextCard().getCenterX();
		int destinationY = (int)destination.shapeOfNextCard().getCenterY();

		this.component = component;

		//The difference in x and y between the two stacks and the hypotenuse
		//of the resulting triangle.
		int deltaX = destinationX - cards.getX();
		int deltaY = destinationY - cards.getY();
		double hyp = Math.sqrt(deltaX*deltaX + deltaY*deltaY);

		dx = deltaX / hyp; //cosine of the angle.
		dy = deltaY / hyp; //sine of the angle.
		accelerationX = dx; //The acceleration and velocity vectors point in the
		accelerationY = dy; //same direction with equal initial values.

		x = cards.getX();
		y = cards.getY();

		Thread thread = new Thread(this);
		thread.start(); //Starts the thread to run the animation.
	}

	/**
	 * Moves <code>cards</code> from its initial location to the destination
	 * stack then appends it to the destination stack.
	 */
	@Override
	public void run(){
		while(!hasArrived()){ //until cards arrives to the destination.
			x += dx; //update the position,
			y += dy;

			dx += accelerationX; //and the velocity.
			dy += accelerationY;

			cards.setLocation((int)x, (int)y); //Set the location.

			try {
				Thread.sleep(10); //Then wait a few milliseconds.
			} catch (InterruptedException e){}

			if(component != null){	 //If its not null,
				component.repaint(); //repaint.
			}
		}

		try{
			destination.appendStack(cards); //appends the cards.
		} catch(IllegalArgumentException e){}//some stacks may throw an exception.
		
		cards.clear();
		component.repaint();
	}

	/**
	 * Determines if the cards have arrived (have past the destination).
	 * @return <code>true</code> if it has arrived, else <code>false</code>.
	 */
	private boolean hasArrived(){
		boolean arrivedX, arrivedY;

		//The sign of the velocity indicates the which side of the destination
		//that cards where initially on. If cards is still on that side,
		//then it hasn't arrived.
		if(dx < 0){
			arrivedX = cards.getX() <= destination.shapeOfNextCard().getCenterX();
		} else {
			arrivedX = cards.getX() >= destination.shapeOfNextCard().getCenterX();
		}
		if(dy < 0){
			arrivedY = cards.getY() <= destination.shapeOfNextCard().getCenterY();
		} else {
			arrivedY = cards.getY() >= destination.shapeOfNextCard().getCenterY();
		}
		return arrivedX && arrivedY; //Cards must arrive in both dimensions.
	}
}