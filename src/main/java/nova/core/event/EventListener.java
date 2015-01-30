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
}
