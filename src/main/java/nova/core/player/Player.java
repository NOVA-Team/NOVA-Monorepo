package nova.core.player;

import nova.core.entity.Entity;

public interface Player extends Entity {
	String getUserName();
	InventoryPlayer getInventory();

	default String getDisplayName() {
		return getUserName();
	}
}
