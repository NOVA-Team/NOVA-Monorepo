package nova.core.inventory.component;

import nova.core.inventory.Inventory;

import java.util.Set;

public interface InventoryProvider {
	Set<Inventory> getInventory();
}
