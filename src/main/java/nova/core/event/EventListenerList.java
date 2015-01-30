package nova.core.event;

/**
 * Implements a list of event listeners. This class is thread-safe and listeners
 * can be added or removed concurrently, no external locking is ever needed.
 * 
 * @author Stan Hebben
 * @param <T> event type
 */
public class EventListenerList<T> {
	// TODO: actually test concurrency
	// implements a linked list of nodes
	private volatile EventListenerNode first = null;
	private EventListenerNode last = null;
	
	public synchronized void clear() {
		first = last = null;
	}
	
	public EventListenerHandle add(EventListener<T> listener) {
		EventListenerNode node = new EventListenerNode(listener, last, null);
		
		synchronized(this) {
			if (first == null)
				first = node;
			
			if (last != null)
				last.next = node;
			
			last = node;
		}
		
		return node;
	}
	
	public synchronized boolean remove(EventListener<T> listener) {
		EventListenerNode current = first;
		
		while (current != null) {
			if (current.listener == listener)
			{
				current.close();
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isEmpty() {
		return first == null;
	}
	
	public void publish(T event) {
		EventListenerNode current;
		
		current = first;
		
		while (current != null) {
			current.listener.onEvent(event);
			
			synchronized(this) {
				current = current.next;
			}
		}
	}
	
	private class EventListenerNode implements EventListenerHandle {
		private final EventListener<T> listener;
		private EventListenerNode next;
		private EventListenerNode prev;
		
		public EventListenerNode(EventListener<T> handler, EventListenerNode prev, EventListenerNode next) {
			this.listener = handler;
			this.prev = prev;
			this.next = next;
		}
		
		@Override
		public void close() {
			synchronized (EventListenerList.this) {
				if (prev == null) {
					first = next;
				} else {
					prev.next = next;
				}

				if (next == null) {
					last = prev;
				} else {
					next.prev = prev;
				}
			}
		}
	}
}
