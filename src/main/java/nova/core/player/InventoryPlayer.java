package nova.core.player;

import nova.core.inventory.Inventory;
import nova.core.item.ItemStack;

import java.util.Optional;

public interface InventoryPlayer extends Inventory {
	int getHeldSlot();
	default Optional<ItemStack> getHeldItem() {
		return getStack(getHeldSlot());
	}
}
