package nova.core.inventory;

import nova.core.item.ItemStack;

import java.util.Optional;

public class InventorySimple implements Inventory {

	private final ItemStack[] stacks;
	private boolean changed = false;

	public InventorySimple(int size) {
		stacks = new ItemStack[size];
	}

	public boolean hasChanged() {
		return changed;
	}

	@Override
	public void markChanged() {
		changed = true;
	}

	public void clearChanged() {
		changed = false;
	}

	public int getSize() {
		return stacks.length;
	}

	public Optional<ItemStack> getStack(int slot) {
		if (slot < 0 || slot >= stacks.length) {
			return Optional.empty();
		} else {
			return Optional.ofNullable(stacks[slot]);
		}
	}

	public boolean setStack(int slot, ItemStack stack) {
		if (slot < 0 || slot >= stacks.length) {
			return false;
		} else {
			stacks[slot] = stack;
			changed = true;
			return true;
		}
	}
}
