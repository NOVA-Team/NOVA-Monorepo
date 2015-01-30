package nova.core.inventory;

import nova.core.item.ItemStack;

import java.util.Optional;

/**
 * This class is virtual inventory used for inventory re-mapping
 */
public class InventoryView implements Inventory {
	private final Inventory parent;
	private final int[] slots;

	/**
	 * Creates new inventory view
	 *
	 * @param parent Parent inventory
	 * @param slots Map of slots
	 */
	public InventoryView(Inventory parent, int[] slots) {
		this.parent = parent;
		this.slots = slots;
	}

	@Override
	public Optional<ItemStack> get(int slot) {
		if (slot < 0 || slot >= slots.length) {
			return null;
		} else {
			return parent.get(slots[slot]);
		}
	}

	@Override
	public boolean set(int slot, ItemStack stack) {
		if (slot < 0 || slot >= slots.length) {
			return false;
		} else {
			return parent.set(slots[slot], stack);
		}
	}

	@Override
	public int size() {
		return slots.length;
	}

	@Override
	public void markChanged() {
		parent.markChanged();
	}
}
