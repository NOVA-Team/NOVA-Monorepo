package nova.core.inventory.components;

import nova.core.inventory.Inventory;
import nova.core.util.Direction;

import java.util.Optional;

public interface InventoryProvider {
	Optional<Inventory> getInventory(Direction side);
}
