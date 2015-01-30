package nova.core.event;

/**
 * Implements a list of event listeners. This class is thread-safe and listeners
 * can be added or removed concurrently, no external locking is ever needed.
 *
 * @param <T> event type
 * @author Stan Hebben
 */
public class EventListenerList<T> {
	// TODO: actually test concurrency

	// implements a linked list of nodes
	private volatile EventListenerNode first = null;
	private EventListenerNode last = null;

	public synchronized void clear() {
		first = last = null;
	}

	/**
	 * Adds an EventListener to the list.
	 *
	 * @param listener event listener
	 * @return event listener's handle
	 */
	public EventListenerHandle<T> add(EventListener<T> listener) {
		EventListenerNode node = new EventListenerNode(listener, last, null);

		synchronized (this) {
			if (first == null) {
				first = node;
			}

			if (last != null) {
				last.next = node;
			}

			last = node;
		}

		return node;
	}

	/**
	 * Removes an EventListener from the list.
	 *
	 * @param listener listener to be removed
	 * @return true if the listener was removed, false it it wasn't there
	 */
	public synchronized boolean remove(EventListener<T> listener) {
		EventListenerNode current = first;

		while (current != null) {
			if (current.listener == listener) {
				current.close();
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if there are any listeners in this list.
	 *
	 * @return true if empty
	 */
	public boolean isEmpty() {
		return first == null;
	}

	/**
	 * Publishes an event by calling all of the registered listeners.
	 *
	 * @param event event to be published
	 */
	public void publish(T event) {
		EventListenerNode current;

		current = first;

		while (current != null) {
			current.listener.onEvent(event);

			synchronized (this) {
				current = current.next;
			}
		}
	}

	// #######################
	// ### Private classes ###
	// #######################

	private class EventListenerNode implements EventListenerHandle<T> {
		private final EventListener<T> listener;
		private EventListenerNode next;
		private EventListenerNode prev;

		public EventListenerNode(EventListener<T> handler, EventListenerNode prev, EventListenerNode next) {
			this.listener = handler;
			this.prev = prev;
			this.next = next;
		}

		@Override
		public EventListener<T> getListener() {
			return listener;
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
