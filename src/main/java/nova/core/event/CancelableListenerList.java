package nova.core.event;

/**
 * Variant of EventListenerList which stops publishing events after they are canceled.
 *
 * @author Stan Hebben
 */
public class CancelableListenerList<T extends Cancelable> extends EventListenerList<T> {
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
