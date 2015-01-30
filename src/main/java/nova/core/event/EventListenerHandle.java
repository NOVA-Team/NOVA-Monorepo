package nova.core.event;

import java.io.Closeable;

/**
 * An EvenListenerHandle is returned when an EventListener is registered in an
 * EventListenerList, which can then be used to unregister the event listener.
 * 
 * @author Stan Hebben
 * @param <T> Event type
 */
public interface EventListenerHandle<T> extends Closeable
{
	/**
	 * Gets this handle's listener.
	 * 
	 * @return listener
	 */
	public EventListener<T> getListener();
	
	/**
	 * Closes (unregisters) the event listener from the list it was registered to.
	 */
	@Override
	public void close();
}
