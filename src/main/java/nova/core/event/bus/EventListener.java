package nova.core.event.bus;

/**
 * Event listeners listen to a single event type.
 *
 * @param <T> event type
 * @author Stan Hebben
 */
@FunctionalInterface
public interface EventListener<T> {
	void onEvent(T event);
}
