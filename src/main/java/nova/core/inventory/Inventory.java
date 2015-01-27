package nova.core.inventory;

import nova.core.item.ItemStack;

import java.util.Iterator;
import java.util.Optional;

public interface Inventory extends Iterable<ItemStack> {
	Optional<ItemStack> getStack(int slot);

	boolean setStack(int slot, ItemStack stack);

	int getSize();

	void markChanged();

	default int addStack(ItemStack stack) {
		int itemsLeft = stack.getStackSize();
		for (int i = 0; i < getSize(); i++) {
			Optional<ItemStack> o = getStack(i);
			if (o.isPresent() && stack.sameStackType(o.get())) {
				itemsLeft -= o.get().addStackSize(itemsLeft);
			} else if (!o.isPresent()) {
				ItemStack stack1 = stack.clone();
				stack1.setStackSize(itemsLeft);
				setStack(i, stack1);
				itemsLeft = 0;
			}

			if (itemsLeft == 0) {
				break;
			}
		}

		if (itemsLeft != stack.getStackSize()) {
			markChanged();
		}

		return itemsLeft;
	}

	default Iterator<ItemStack> iterator() {
		return new InventoryIterator(this);
	}
}
