package dataStructures;

/**
 * @author Warren Godone-Maresca<p>
 * 
 * An abstract data type in which the last elements to be added are the first to
 * be removed.
 *
 * @param <T> The data type of objects to be held in this stack.
 */
public interface StackADT<T> {
	/**
	 * Must add some object of type T to the top of the stack in O(1) time.
	 * @param value The object to be pushed to the top of the stack.
	 */
	public void push(T value);
	
	/**
	 * Must return and remove the current top element of the stack, that is, the
	 * element in the stack that was added most recently. It must also be O(1).
	 * @return If this stack is not empty, the element at the top of the stack,
	 * 			otherwise, <code>null</code>.
	 */
	public T pop();
	
	/**
	 * Must return the element that occupies that top position of the stack in
	 * O(1) time.
	 * @return If this stack is not empty, the element at the top of the stack,
	 * 			otherwise, <code>null</code>.
	 */
	public T peek();
}
