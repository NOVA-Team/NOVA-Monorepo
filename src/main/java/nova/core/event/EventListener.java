package nova.core.event;

/**
 * Event listeners listen to a single event type.
 *
 * @param <T> event type
 * @author Stan Hebben
 */
@FunctionalInterface
public interface EventListener<T> {
	public void onEvent(T value);

	/**
	 * A wrapper for an event listener that only accepts a specific type of
	 * event.
	 * 
	 * @author Vic Nightfall
	 * @param <EVENT> event type
	 * @param <T> super type
	 */
	public static class SingleEventListener<EVENT extends T, T> implements EventListener<T> {
		private final Class<EVENT> eventClass;
		private final EventListener<EVENT> wrappedListener;

		/**
		 * Constructs a new single typed Event listener.
		 * 
		 * @param wrappedListener The listener which gets called when the event
		 *        was accepted.
		 * @param eventClass The event to listen for, Any posted event that is
		 *        an instance of said class will get passed through to the
		 *        wrapped listener instance.
		 */
		public SingleEventListener(EventListener<EVENT> wrappedListener, Class<EVENT> eventClass) {
			this.eventClass = eventClass;
			this.wrappedListener = wrappedListener;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onEvent(T value) {
			if (eventClass.isInstance(value)) {
				wrappedListener.onEvent((EVENT) value);
			}
		}
	}
}
