package nova.core.player;

public interface Player {
	String getUserName();

	InventoryPlayer getInventory();

	default String getDisplayName() {
		return getUserName();
	}
}
