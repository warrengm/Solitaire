package dataStructures;

/**
 * @author Warren Godone-Maresca<p>
 * 
 * An implementation of a standard stack data type. It has operations for standard
 * stack operations, push, pop, and peek, as well as some additional methods for
 * to check the size, copy, reverse, and clear the stack.
 *
 * @param <T> The data type of objects to be held in this stack.
 */
public class Stack<T> implements StackADT<T> {
	/** Points to the node that contains the top element of the stack.		*/
	protected Node<T> head;

	/** Holds the number of elements in the stack.							*/
	protected int size = 0;

	/**
	 * Instantiates the stack.
	 */
	public Stack(){} //There is nothing to initialize.

	/**
	 * Adds the given value to the top of the stack.
	 */
	public void push(T value){
		if(head == null){ //Then we create a new head.
			head = new Node<T>(value, null);
		} else { //we insert a new node before head.
			Node<T> latest = new Node<T>(value, head);
			head = latest;
		}
		size++;
	}

	/**
	 * Returns and removes the top (most recently added) element of the stack if
	 * such an element exists.
	 * @return 	If this stack is not empty, the element at the top of the stack,
	 * 			otherwise, null.
	 */
	public T pop(){
		if(isEmpty()){ //Then there is nothing to pop.
			return null;
		}
		size--;
		T temp = head.getValue(); //Holds the object to return
		head = head.getNext();
		return temp;
	}

	/**
	 * Returns the top element of the stack if such an element exists.
	 * @return 	If this stack is not empty, the element at the top of the stack,
	 * 			otherwise, null.
	 */
	public T peek(){
		if(isEmpty()){
			return null;
		}
		return head.getValue();
	}

	/**
	 * Determines whether or not the stack is empty.
	 * @return 	<code>true</code> if the stack has no elements, otherwise
	 * 			<code>false</code>
	 */
	public boolean isEmpty(){
		return head == null;
	}

	/**
	 * Removes all elements from the stack.
	 */
	public void clear(){
		head = null;
		size = 0;
	}

	/**
	 * Returns the number of elements in the stack.
	 */
	public int size(){
		return size;
	}

	/**
	 * Reverses the order of all elements in the stack. This stack will be
	 * modified as a result.
	 */
	public void reverse(){
		reverse(head, null);//There are no nodes before the head so we pass null.
	}

	/**
	 * Reverse the order of all nodes after <code>node</code>.
	 * @param node The node to begin the reversal.
	 * @param prev The node below <code>node</code>.
	 */
	private void reverse(Node<T> node, Node<T> prev){
		if(node != null){ //condition to recurse.
			reverse(node.getNext(), node); //Reverse the rest of the stack.
			if(node.getNext() == null){ // Then it is the new head
				head = node;
			}
			node.setNext(prev);
		}
	}

	/**
	 * Returns a shallow copy of this stack with the elements in reversed order.
	 */
	public Stack<T> reverseCopy(){
		Stack<T> temp = new Stack<T>(); //The reversed copy.
		for(Node<T> node = head; node != null; node = node.getNext()){
			temp.push(node.getValue());
		}
		return temp;
	}

	/**
	 * Returns a shallow copy of this stack in which the order of the elements is
	 * preserved.
	 */
	public Stack<T> copy(){
		Stack<T> temp = reverseCopy();
		temp.reverse(); //reverse the reversed copy to put it in order.
		return temp;
	}

	/**
	 * Appends a given stack to this stack. The top element of the given stack
	 * will be the top element in this stack after this method is called. The order
	 * of all elements of the given stack will be preserved in this stack. The 
	 * bottom element of the given stack will be on top of the previouse
	 * top element of this stack.<p>
	 * 
	 * Additionally, the given stack object will be unmodified. It will still exists
	 * as a shallow copy of the top elements of this stack. The objects will not
	 * be copied and shall be contained in both stacks.
	 * 
	 * @param stack The stack to be appended to this stack.
	 */
	public void appendStack(Stack<T> stack){
		if(stack == null || stack.isEmpty()){
			return;
		}
		//So that the given stack is unmodified. It is reversed so that the
		//the elements are added in the proper order.
		Stack<T> temp = stack.reverseCopy();
		while(!temp.isEmpty()){
			push(temp.pop()); //We append each element of the
		}
	}

	/**
	 * A node class to contain an element and point to the next node in the stack.
	 */
	@SuppressWarnings("hiding")
	protected class Node<T> {
		/** The object held by this node.									*/
		private T value;
		
		/** Points to the next node in the stack.							*/
		private Node<T> next;
		
		/**
		 * Instantiates the node with a object value and a next node.
		 * @param value The object to be contained in this node.
		 * @param next	The next node in the list after this node. This may be
		 * 				<code>null</code> to instantiate this node as an isolated
		 * 				<code>Node</code>.
		 */
		public Node(T value, Node<T> next){
			this.value = value;
			this.next = next;
		}

		/**
		 * Sets object value to be in this node.
		 * @param value The object to be contained in this node.
		 */
		public void setValue(T value){
			this.value = value;
		}

		/**
		 * Returns the value contained in this node.
		 */
		public T getValue(){
			return value;
		}

		/**
		 * Sets the node to follow this node in the list.
		 * @param next	The next node in the list after this node. This may be
		 * 				<code>null</code> to remove any other node following
		 * 				this node.
		 */
		public void setNext(Node<T> next){
			this.next = next;
		}

		/**
		 * Returns the node that immediately follows this node.
		 * @return 	The node that is pointed to by this node. <code>null</code>
		 * 			will be returned if no nodes come after this node in a list.
		 */
		public Node<T> getNext(){
			return next;
		}
	}
}