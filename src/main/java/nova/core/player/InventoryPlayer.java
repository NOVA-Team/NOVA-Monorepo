package nova.core.player;

import nova.core.inventory.Inventory;
import nova.core.item.ItemStack;

import java.util.Optional;

/**
 * Player {@link Inventory}
 *
 * @see Inventory
 */
public interface InventoryPlayer extends Inventory {
	/**
	 * @return Number of currently selected slot
	 */
	int getHeldSlot();

	/**
	 * @return Currently held item instance
	 */
	default Optional<ItemStack> getHeldItem() {
		return get(getHeldSlot());
	}
}
