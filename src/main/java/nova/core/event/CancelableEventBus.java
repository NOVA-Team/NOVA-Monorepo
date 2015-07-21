package nova.core.event;

/**
 * Variant of {@link EventBus} which stops publishing events after they are
 * canceled.
 *
 * @author Stan Hebben
 * @param <T> {@link CancelableEvent Cancelable} event type
 */
public class CancelableEventBus<T extends CancelableEvent> extends EventBus<T> {
	@Override
	public void publish(T event) {
		EventListenerNode current;

		current = first;

		while (current != null && !event.isCanceled()) {
			current.listener.onEvent(event);

			synchronized (this) {
				current = current.next;
			}
		}
	}
}
