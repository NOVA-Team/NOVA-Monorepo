package nova.core.inventory.component;

import nova.core.inventory.Inventory;
import nova.core.util.Direction;

import java.util.Set;

public interface SidedInventoryProvider {
	Set<Inventory> getInventory(Direction side);
}
