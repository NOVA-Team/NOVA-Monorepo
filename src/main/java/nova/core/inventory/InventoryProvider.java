package nova.core.inventory;

import nova.core.util.Direction;

public interface InventoryProvider {
	Inventory getInventory(Direction side);
}
