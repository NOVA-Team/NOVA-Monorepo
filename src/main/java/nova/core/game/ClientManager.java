package nova.core.game;

import nova.core.player.Player;

/**
 * The ClientManager to access client-side instances.
 * @author Calclavia
 */
public abstract class ClientManager {

	/**
	 * @return Gets the current player on this running client
	 */
	public abstract Player getPlayer();
}
