package nova.core.item;

import java.util.Iterator;
import java.util.Optional;

public class Inventory implements Iterable<ItemStack> {
	private class InventoryIterator implements Iterator<ItemStack> {
		private final Inventory inv;
		private int i;
		private ItemStack next = null;

		public InventoryIterator(Inventory inv) {
			this.inv = inv;
			findNext();
		}

		private void findNext() {
			while (i < inv.getSize()) {
				Optional<ItemStack> o = inv.getStack(i++);
				if (o.isPresent()) {
					next = o.get();
				}
			}
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public ItemStack next() {
			ItemStack current = next;
			findNext();
			return current;
		}
	}

	private final ItemStack[] stacks;
	private boolean changed = false;

	public Inventory(int size) {
		stacks = new ItemStack[size];
	}

	public boolean hasChanged() {
		return changed;
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

	public int addStack(ItemStack stack) {
		int itemsLeft = stack.getStackSize();
		for (int i = 0; i < stacks.length; i++) {
			if (stacks[i] != null && stack.sameStackType(stacks[i])) {
				itemsLeft -= stacks[i].addStackSize(itemsLeft);
			} else {
				stacks[i] = stack.clone();
				stacks[i].setStackSize(itemsLeft);
				itemsLeft = 0;
			}

			if (itemsLeft == 0) {
				break;
			}
		}

		if (itemsLeft != stack.getStackSize()) {
			changed = true;
		}

		return itemsLeft;
	}

	@Override
	public Iterator<ItemStack> iterator() {
		return new InventoryIterator(this);
	}
}