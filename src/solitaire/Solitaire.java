package solitaire;

import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * A JPanel that plays Solitaire. This class contains a main method that will
 * open a new JFrame with this JPanel. The window contains a menu for user to
 * select the kind of solitaire being played. {@link Klondike} Solitaire is played
 * by default but the user also has the option the choose {@link FreeCell} or 
 * {@link Spider} Solitaire.
 * 
 * @author Warren Godone-Maresca
 */
public class Solitaire extends JPanel implements ActionListener {
	/** The Solitaire game.													*/
	private Klondike game;

	/** Points to menu items in the window.									*/
	private JMenuItem klondikeItem, freeCellItem, 
					  easySpiderItem, hardSpiderItem, yukonItem;

	/** Holds the button to display the rules.								*/
	private JMenuItem rulesItem;

	/** 
	 * Instantiates this with Klondike Solitaire by default.
	 */
	public Solitaire(){
		game = new Klondike(this);
	}

	/** 
	 * Draws the game.
	 */
	@Override
	protected void paintComponent(Graphics pane) {
		super.paintComponent(pane);
		game.paint(pane);
	}

	/** 
	 * Returns a menu bar to select the form of Solitaire.
	 */
	private JMenuBar makeMenuBar(){
		JMenuBar bar = new JMenuBar(); //Holds all of the buttons.
		JMenu newGameMenu = new JMenu("New Game");
		
		klondikeItem = new JMenuItem("Klondike"); 
		klondikeItem.addActionListener(this);
		newGameMenu.add(klondikeItem);   //Adds the Klondike item.

		freeCellItem = new JMenuItem("Free Cell"); //And Free Cell item.
		freeCellItem.addActionListener(this);
		newGameMenu.add(freeCellItem);

		JMenu spiderMenu = new JMenu("Spider"); //Holds the easy and hard items.
		easySpiderItem = new JMenuItem("Easy  "); //The easy spider item.
		easySpiderItem.addActionListener(this);
		spiderMenu.add(easySpiderItem);
		
		hardSpiderItem = new JMenuItem("Hard  "); //The hard spider item.
		hardSpiderItem.addActionListener(this);
		spiderMenu.add(hardSpiderItem);
		newGameMenu.add(spiderMenu);
		
		yukonItem = new JMenuItem("Yukon"); //And Free Cell item.
		yukonItem.addActionListener(this);
		newGameMenu.add(yukonItem);

		bar.add(newGameMenu);

		JMenu rulesMenu = new JMenu("Rules"); //To display the rules.
		rulesItem = new JMenuItem("Open");
		rulesItem.addActionListener(this);
		rulesMenu.add(rulesItem);
		bar.add(rulesMenu);

		return bar; //The bar has been created.
	}


	/**
	 * Responds to menu events to set the form of Solitaire.
	 */
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == rulesItem){
			try { //Opens the rules //TODO
				Desktop.getDesktop().open(new File("User Manual.pdf"));
			} catch (IOException ex){}
			
			return; //So we don't remove the listeners.
		}
		
		//The listeners need to be removed or else there will still be a
		//reference to the previous game object.
		this.removeMouseListener(game);
		this.removeMouseMotionListener(game);

		if(e.getSource() == klondikeItem){
			game = new Klondike(this);
		} else if(e.getSource() == freeCellItem){
			game = new FreeCell(this);
		} else if(e.getSource() == easySpiderItem){
			game = new Spider(this, true);
		} else if(e.getSource() == hardSpiderItem){
			game = new Spider(this, false);
		} else if (e.getSource() == yukonItem){
			game = new Yukon(this);
		}
		
		repaint();
	}

	/**
	 * Makes a window containing this and the menu bar.
	 * @param args
	 */
	public static void main(String[] args){
		Solitaire gamePanel = new Solitaire();		//The game panel
		JFrame window = new JFrame();				//The window.

		window.setTitle("Solitaire");				//Sets the title,
		window.setLocation(10, 10); 				//location,
		window.getContentPane().setBackground(gamePanel.getBackground()); //BG,
		window.setJMenuBar(gamePanel.makeMenuBar()); //menu bar,
		window.add(gamePanel);						 //panel,
		window.setSize(gamePanel.getPreferredSize());//and size.
		window.setVisible(true);	//Set to visible, and
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//to exit on close
	}
}