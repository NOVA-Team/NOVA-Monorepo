package nova.core.component.inventory;

import nova.core.item.Item;

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
	default Optional<Item> getHeldItem() {
		return get(getHeldSlot());
	}
}
