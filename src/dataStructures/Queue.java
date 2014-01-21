package dataStructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterable stack implementation of a queue. Elements are removable and 
 * accessible in the order in which they where added.
 * 
 * @author Warren Godone-Maresca
 *
 * @param <T> The type of this queue's elements.
 */
public class Queue<T> implements Iterable<T> {

	/** The elements of this queue.											 */
	private Stack<T> elements;
	
	/**
	 * Instantiates the queue with no elements
	 */
	public Queue(){
		elements = new Stack<T>();
	}
	
	/**
	 * Adds the given value to the queue.
	 * @param value The object to be added.
	 */
	public void enqueue(T value){
		elements.reverse();	 //First we reverse the elements so that the new 
		elements.push(value);//element will be at the bottom of the stack.
		elements.reverse();  //Then reverse again to put the elements in order.
	}

	/**
	 * Removes and returns the element that was added before all others.
	 * @return That element if this queue is not empty, else <code>null</code>
	 */
	public T dequeue(){
		return elements.pop();
	}
	
	/**
	 * Returns the element that was added before all others.
	 * @return That element if this queue is not empty, else <code>null</code>
	 */
	public T peek(){
		return elements.peek();
	}
	
	/**
	 * Returns the number of elements in the queue.
	 */
	public int size(){
		return elements.size();
	}
	
	/**
	 * Determines whether or not this queue contains any elements.
	 */
	public boolean isEmpty(){
		return elements.isEmpty();
	}

	/**
	 * Returns a new iterator for this queue.
	 */
	@Override
	public Iterator<T> iterator(){
		return new QueueIterator();
	}
	
	/**
	 * An iterator for this queue. next() and hasNext() are supported but remove()
	 * is not as elements may not be removed from the middle of a queue.
	 */
	private class QueueIterator implements Iterator<T> {
		/** The elements in this iteration.									*/
		private Stack<T> stack;
		
		/**
		 * Instantiates a new iterator.
		 */
		public QueueIterator(){
			stack = elements.copy();
		}
		
		/**
		 * Determines whether or not there are any more elements in this iteration.
		 * @return <code>true</code> if the iteration has more elements. (i.e.
		 * 		  returns true if {@link #next()} would return an object rather 
		 * 		  than throwing an exception.)
		 */
		@Override
		public boolean hasNext(){
			return !stack.isEmpty();
		}

		/**
		 * Returns the next element in the iteration.
		 * @throws NoSuchElementException if there are no elements to iterate.
		 */
		@Override
		public T next(){
			if(hasNext()){
				return stack.pop();
			} else {
				throw new NoSuchElementException();
			}
		}

		/**
		 * Unsupported. Elements cannot be removed from the middle of a queue.
		 * @throws UnsupportedOperationException when called.
		 */
		@Override
		public void remove(){
			throw new UnsupportedOperationException("Not Supported");
		}
	}
}