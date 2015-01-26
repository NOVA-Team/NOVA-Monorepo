package nova.core.inventory;

import nova.core.item.ItemStack;

import java.util.Optional;

public class InventoryView implements Inventory {
	private final Inventory parent;
	private final int[] slots;

	public InventoryView(Inventory parent, int[] slots) {
		this.parent = parent;
		this.slots = slots;
	}

	@Override
	public Optional<ItemStack> getStack(int slot) {
		if (slot < 0 || slot >= slots.length) {
			return null;
		} else {
			return parent.getStack(slots[slot]);
		}
	}

	@Override
	public boolean setStack(int slot, ItemStack stack) {
		if (slot < 0 || slot >= slots.length) {
			return false;
		} else {
			return parent.setStack(slots[slot], stack);
		}
	}

	@Override
	public int getSize() {
		return slots.length;
	}

	@Override
	public void markChanged() {
		parent.markChanged();
	}
}
