package nova.core.player;

import nova.core.entity.Entity;
import nova.core.util.vector.Vector3d;
import nova.core.world.World;

public interface Player extends Entity {
	String getUserName();
	InventoryPlayer getInventory();

	default String getDisplayName() {
		return getUserName();
	}
}
