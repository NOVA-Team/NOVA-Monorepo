package nova.core.inventory;

import nova.core.item.ItemStack;

import java.util.Optional;

/**
 * This class provides implementation of {@link Inventory}
 */
public class InventorySimple implements Inventory {

	private final ItemStack[] stacks;
	private boolean changed = false;

	public InventorySimple(int size) {
		stacks = new ItemStack[size];
	}

	/**
	 * Tells if this inventory has changed since last
	 * invocation of {@link #clearChanged()}
	 *
	 * @return Whether the inventory has changed
	 */
	public boolean hasChanged() {
		return changed;
	}

	@Override
	public void markChanged() {
		changed = true;
	}

	/**
	 * Marks this inventory as unchanged
	 */
	public void clearChanged() {
		changed = false;
	}

	@Override
	public int size() {
		return stacks.length;
	}

	@Override
	public Optional<ItemStack> get(int slot) {
		if (slot < 0 || slot >= stacks.length) {
			return Optional.empty();
		} else {
			return Optional.ofNullable(stacks[slot]);
		}
	}

	@Override
	public boolean set(int slot, ItemStack stack) {
		if (slot < 0 || slot >= stacks.length) {
			return false;
		} else {
			stacks[slot] = stack;
			changed = true;
			return true;
		}
	}
}
