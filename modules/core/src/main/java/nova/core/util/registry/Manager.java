package nova.core.util.registry;

import nova.core.event.bus.Event;

/**
 * A manager is a singleton object that manages a specific aspect of the game.
 * @param <S> The self type
 * @author Calclavia
 */
public abstract class Manager<S extends Manager<S>> {
	/**
	 * Initializes the manager event
	 */
	@SuppressWarnings("unchecked")
	public abstract void init();

	/**
	 * An event that is published when the manager is capable of registering.
	 */
	//TODO: Should this class be here or placed in the event package? Might be inconsistent.
	public static class ManagerEvent<S> extends Event {
		public final S manager;

		public ManagerEvent(S manager) {
			this.manager = manager;
		}
	}
}
