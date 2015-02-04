package nova.core.event;

/**
 * Implements a list of event listeners. This class is thread-safe and listeners
 * can be added or removed concurrently, no external locking is ever needed.
 * Also, it's very lightweight.
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
	 * Adds an EventListener to the list that only accepts a specific subclass
	 * of &lt;T&gt;
	 * 
	 * @param listener listener to register
	 * @param clazz class to listen for
	 * @return event listener's handle
	 */
	public <E extends T> EventListenerHandle<T> add(EventListener<E> listener, Class<E> clazz) {
		return add(new SingleEventListener<E, T>(listener, clazz));
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
			if (current.listener.equals(listener)) {
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

	// only to be used by MultiListenerList, because I can't cast it in there...
	protected void publishSafely(Object event) {
		publish((T) event);
	}

	// #######################
	// ### Private classes ###
	// #######################

	protected class EventListenerNode implements EventListenerHandle<T> {
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

	/**
	 * A wrapper for an event listener that only accepts a specific type of
	 * event.
	 *
	 * @author Vic Nightfall
	 * @param <E> event type
	 * @param <T> super type
	 */
	protected static class SingleEventListener<E extends T, T> implements EventListener<T> {
		private final Class<E> eventClass;
		private final EventListener<E> wrappedListener;

		/**
		 * Constructs a new single typed Event listener.
		 *
		 * @param wrappedListener The listener which gets called when the event
		 *        was accepted.
		 * @param eventClass The event to listen for, Any posted event that is
		 *        an instance of said class will get passed through to the
		 *        wrapped listener instance.
		 */
		public SingleEventListener(EventListener<E> wrappedListener, Class<E> eventClass) {
			this.eventClass = eventClass;
			this.wrappedListener = wrappedListener;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onEvent(T value) {
			if (eventClass.isInstance(value)) {
				wrappedListener.onEvent((E) value);
			}
		}
	}
}
