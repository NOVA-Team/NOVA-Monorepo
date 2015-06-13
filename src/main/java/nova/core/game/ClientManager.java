package nova.core.game;

import nova.core.entity.Entity;

/**
 * The ClientManager to access client-side instances.
 * @author Calclavia
 */
public abstract class ClientManager {

	/**
	 * @return Gets the current player on this running client. An entity withPriority the Player component.
	 */
	public abstract Entity getPlayer();

	/**
	 * @return Is the game paused?
	 */
	public abstract boolean isPaused();
}
