package nova.core.event.bus;

import java.io.Closeable;

/**
 * An EvenListenerHandle is returned when an EventListener is registered in an
 * EventListenerList, which can then be used to unregister the event listener.
 *
 * @param <T> Event type
 * @author Stan Hebben
 */
public interface EventListenerHandle<T> extends Closeable {
	/**
	 * Gets this handle's listener.
	 *
	 * @return listener
	 */
	EventListener<T> getListener();

	/**
	 * Closes (unregisters) the event listener from the list it was registered
	 * to.
	 */
	@Override
	void close();
}
