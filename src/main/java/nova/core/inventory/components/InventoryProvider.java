package nova.core.inventory.components;

import nova.core.block.components.Connectable;
import nova.core.inventory.Inventory;
import nova.core.util.Direction;

public interface InventoryProvider extends Connectable {
	Inventory getInventory(Direction side);
}
