package nova.core.util;

import nova.core.event.bus.Event;
import nova.internal.core.Game;

/**
 * A manager is a singleton object that manages a specific aspect of the game.
 * @param <S> The self type
 * @author Calclavia
 */
public abstract class Manager<S extends Manager<S>> {

	/**
	 * Publishes the register event
	 */
	@SuppressWarnings("unchecked")
	public void publish() {
		Game.events().publish(new ManagerEvent((S) this));
	}

	/**
	 * An event that is published when the manager is capable of registering.
	 */
	public class ManagerEvent extends Event {
		public final S manager;

		public ManagerEvent(S manager) {
			this.manager = manager;
		}
	}
}
