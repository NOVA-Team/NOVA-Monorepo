package nova.core.player;

/**
 * Basic representation of Player
 */
public interface Player {
	/**
	 * @return Returns player name that can be used to identify this player
	 */
	String getUsername();

	/**
	 * @return Returns the ID representing the player.
	 * For many games, the username is the ID.
	 */
	default String getID() {
		return getUsername();
	}

	/**
	 * @return Inventory of the player
	 */
	InventoryPlayer getInventory();

	/**
	 * @return Returns non-identifying player name
	 */
	default String getDisplayName() {
		return getUsername();
	}
}
