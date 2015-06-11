package nova.core.component;

import nova.core.event.Event;
import nova.core.event.EventBus;
import nova.core.util.Buildable;
import nova.core.util.Factory;
import nova.core.util.Identifiable;
import nova.internal.core.Game;

/**
 * Base interface for all Components.
 * A Component is intended as a data holder and provides data to be processed in a ComponentProvider.
 * @author Calclavia
 */
public abstract class Component implements Buildable {

	public final EventBus<Event> events = new EventBus<>();

	/**
	 * Will be injected by factory.
	 */
	@SuppressWarnings("unused")
	private String ID;

	public final String getID() {
		return ID;
	}

	public final Factory<Component> factory() {
		return Game.components().getFactory(getID()).get();
	}

}
