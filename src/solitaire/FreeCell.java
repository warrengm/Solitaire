package solitaire;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import card.Card;
import card.HoldingCell;
import card.StackOfCards;
import card.Tableau;
import dataStructures.Stack;

/**
 * A common form of Solitaire. It is similar to {@link Klondike} except that
 * neither a stock nor a waste is used and all cards are dealt into the tableaux.
 * In addition, there are four holding cells and which the user may use to store
 * up to one card at a time. Also, the user is restricting to moving a certain 
 * number of cards between tableaux that is equal to the number of free holding
 * cells plus 1.
 * 
 * @author Warren Godone-Maresca
 */
public class FreeCell extends Klondike {
	/** The four holding cells.												*/
	protected HoldingCell[] holdingCells;

	/** Holds the number of free spaces.									*/
	private int emptyCells = 4;
	
	/** Holds the number of empty tableaux.									*/
	private int emptyTableaux = 0;

	/**
	 * Instantiates the game and the panel.
	 */
	public FreeCell(JPanel panel){
		super(panel);
	}

	/**
	 * Initializes all of the stacks used in the game.
	 */
	@Override
	protected void init(){
		initiallyHidden = false; //The cards are not initially hidden.
		StackOfCards source = StackOfCards.randomDeck();

		//Initializes the tableaux with an anonymous array.
		initTableaux(source, new int[] {7, 7, 7, 7, 6, 6, 6, 6});
		initFoundations(4);
		initHoldingCells();

		initialized = true;
		container.repaint();
	}

	/**
	 * Pre: The tableaux in {@link #tableaux} have been initialized.
	 */
	protected void initHoldingCells(){
		holdingCells = new HoldingCell[4];
		for(int i = 0; i < 4; i++){ //Each holding cell is initialized
			holdingCells[i] = new HoldingCell(tableaux[i].getX() - cardWidth/2, 
					yCoord, cardWidth);
		}
	}

	/**
	 * Determines if the given stack of cards is suitable to be removed from a
	 * tableau (assuming that it originated from a tableau).
	 * @return 	<code>true</code> if the given stack is visible, alternating in
	 * 			color, and in sequence, and its size does not exceed the number
	 * 			of free cells + 1 times two raised to the power of the number of
	 * 			empty tableaux, else <code>false</code>.
	 */
	@Override
	protected boolean removableFromTableaux(Stack<Card> cards){
		return super.removableFromTableaux(cards) &&
				cards.size() <= (emptyCells + 1)*(Math.pow(2, emptyTableaux));			
	}

	/**
	 * If a holding cell is pressed and that cell is not empty, then the card
	 * in the cell will be put in use.
	 * @param x The x coordinate of the mouse click.
	 * @param y The y coordinate of the mouse click.
	 * @return <code>true</code> if a card was put in use, else <code>false</code>
	 */
	protected boolean holdingCellsPressedAction(int x, int y){
		for(HoldingCell cell : holdingCells){
			//If the cell contains the click, then it is nonempty, so
			if(cell.contains(x, y)){
				inUse.push(cell.pop()); //add the card of the cell to inUse,
				lastStack = cell;		//point lastStack to the cell,
				return true; //The action was performed.
			}
		}
		return false; //If we arrive to this point, nothing was done.
	}
	
	/**
	 * If the mouse is released over a holding cell and that cell is empty,
	 * and one card is in use, then that card will be put in the cell, otherwise
	 * nothing is done.
	 * @param x The x coordinate of the mouse click.
	 * @param y The y coordinate of the mouse click.
	 * @return 	<code>true</code> if the above action is performed, otherwise 
	 * 			<code>false</code>.
	 */
	protected boolean holdingCellsReleasedAction(int x, int y){
		for(HoldingCell cell : holdingCells){
			if(cell.shapeOfNextCard().contains(x, y) //If the cell was clicked,
					&& cell.isEmpty()				 //and the cell is empty,
					&& inUse.size() == 1){			 //and 1 card is in uses,
				cell.push(inUse.pop());		//then add that card to the cell.
				return true; //The action was performed.
			}
		}
		return false;//The action was not performed if we have reached this line.
	}

	/**
	 * Performs all of the pressed action methods until an action is performed.
	 */
	@Override
	public void mousePressed(MouseEvent e){
		int x = e.getX(), y = e.getY(); //The coordinates.
		if(inUse.isEmpty() && !tableauxPressedAction(x, y)){
			//If the tableaux action was not done,
			holdingCellsPressedAction(x, y); //Then do the cell action.
		}
	}

	/**
	 * If cards are being dragged by the mouse and the mouse is released over one
	 * of the stacks (holding cells, tableaux, foundations), then the release action
	 * associated with that stack will be performed and the number of moves will
	 * be incremented and it will be checked if the user has won. Otherwise those
	 * cards will be returned to the last stack. No carsd will be in use after
	 * this method.
	 */
	@Override
	public void mouseReleased(MouseEvent e){
		int x = e.getX(), y = e.getY();
		if(inUse.isEmpty()){ //If no cards are in use,
			return;			 //then do nothing.
		}
		//If some cards are in use, the each released action method is called
		//until one action is done. If no action is performed, then the cards are
		//returned to the last stack.
		if(!tableauxReleasedAction(x, y) 
				&& !foundationsReleasedAction(x, y)
				&& !holdingCellsReleasedAction(x, y)){
			returnToLastStack();
		} else { //some action was done,
			moves++; //so increment the num of moves
			if(hasWon()){
				onWin(); //Then perform the on win actions.
			}
		}
		setEmptyVars(); //Update the number of empty stacks
		container.repaint(); //and repaint.
	}
	
	/**
	 * Updates the emptyCells and emptyTableaux.
	 */
	private void setEmptyVars(){
		emptyCells = (emptyTableaux = 0); //Reinitialize the variables.
		
		for(HoldingCell cell : holdingCells){
			if(cell.size() == 0) //If a cell has no cards
				emptyCells++;    //increment the num of empty cells.
		}
		for(Tableau tableau : tableaux){
			if(tableau.size() == 0)
				emptyTableaux++;
		}
	}
	
	/**
	 * Determines if the user has won.
	 */
	@Override
	protected boolean hasWon(){
		setEmptyVars();
		for(Tableau tableau : tableaux){
			if(!Tableau.inSequence(tableau) || !Tableau.alternatesInColor(tableau)){ 
				return false; //Then the user has not won.
			}
		}
		//If there are 4 or fewer filled tableaux and all cells are empty, the
		//user has effectively won.
		return emptyTableaux >= (tableaux.length - 4) && emptyCells == 4;
	}
	
	/**
	 * Draws all of the stacks being used.
	 */
	@Override
	public void paint(Graphics pane){
		if(initialized){
			for(StackOfCards cell : holdingCells){
				cell.draw(pane);
			}
			super.paint(pane);
		}
	}
}