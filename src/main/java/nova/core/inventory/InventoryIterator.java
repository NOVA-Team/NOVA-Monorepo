package nova.core.inventory;

import nova.core.item.Item;

import java.util.Iterator;
import java.util.Optional;

class InventoryIterator implements Iterator<Item> {
	private final Inventory inv;
	private int i;
	private Item next = null;

	InventoryIterator(Inventory inv) {
		this.inv = inv;
		findNext();
	}

	private void findNext() {
		while (i < inv.size()) {
			Optional<Item> o = inv.get(i++);
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
	public Item next() {
		Item current = next;
		findNext();
		return current;
	}
}
