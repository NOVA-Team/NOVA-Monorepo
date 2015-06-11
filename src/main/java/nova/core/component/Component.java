package nova.core.component;

import nova.core.event.Event;
import nova.core.event.EventBus;
import nova.core.event.EventListener;
import nova.core.event.EventListenerHandle;
import nova.core.util.Identifiable;

/**
 * Base interface for all Components.
 * A Component is intended as a data holder and provides data to be processed in a ComponentProvider.
 * @author Calclavia
 */
public abstract class Component implements Identifiable {

	public final EventBus<Event> events = new EventBus<>();

	public <EVENT extends Event> EventListenerHandle<Event> on(EventListener<EVENT> listener, Class<EVENT> clazz) {
		return events.add(listener, clazz);
	}

	public <EVENT extends Event> EventListenerHandle<Event> on(EventListener<EVENT> listener, Class<EVENT> clazz, int priority) {
		return events.add(listener, clazz, priority);
	}

	@Override
	public String getID() {
		return getClass().getSimpleName();
	}
}
