package nova.core.event;

/**
 * Event listeners listen to a single event type.
 * 
 * @author Stan Hebben
 * @param <T> event type
 */
@FunctionalInterface
public interface EventListener<T>
{
	public void onEvent(T value);
}
