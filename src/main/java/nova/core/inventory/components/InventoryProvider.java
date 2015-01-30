package nova.core.inventory.components;

import nova.core.block.components.Connectable;
import nova.core.inventory.Inventory;
import nova.core.util.Direction;

import java.util.Optional;

public interface InventoryProvider extends Connectable {
	Optional<Inventory> getInventory(Direction side);
}
