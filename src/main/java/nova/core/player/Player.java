package nova.core.player;

/**
 * Basic representation of Player
 */
public interface Player {
	/**
	 * @return Returns player name that can be used to identify this player
	 */
	String getUserName();

	/**
	 * @return Inventory of the player
	 */
	InventoryPlayer getInventory();

	/**
	 * @return Returns non-identifying player name
	 */
	default String getDisplayName() {
		return getUserName();
	}
}
