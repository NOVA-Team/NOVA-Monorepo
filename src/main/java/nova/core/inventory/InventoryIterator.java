package nova.core.inventory;

import nova.core.item.ItemStack;

import java.util.Iterator;
import java.util.Optional;

/**
 * Created by asie on 1/26/15.
 */
class InventoryIterator implements Iterator<ItemStack> {
	private final Inventory inv;
	private int i;
	private ItemStack next = null;

	public InventoryIterator(Inventory inv) {
		this.inv = inv;
		findNext();
	}

	private void findNext() {
		while (i < inv.size()) {
			Optional<ItemStack> o = inv.get(i++);
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
