package nova.core.inventory;

import nova.core.item.Item;
import nova.core.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Inventory extends Iterable<ItemStack> {
	Optional<ItemStack> get(int slot);

	boolean set(int slot, ItemStack stack);

	int size();

	void markChanged();

	default int add(int slot, ItemStack stack) {
		Optional<ItemStack> o = get(slot);
		if (o.isPresent()) {
			if (stack.sameStackType(o.get())) {
				return stack.getStackSize() - o.get().addStackSize(stack.getStackSize());
			} else {
				return stack.getStackSize();
			}
		} else {
			set(slot, stack);
			return 0;
		}
	}

	default int add(ItemStack stack) {
		int itemsLeft = stack.getStackSize();
		for (int i = 0; i < size(); i++) {
			itemsLeft = add(i, stack.withAmount(itemsLeft));
		}

		if (itemsLeft != stack.getStackSize()) {
			markChanged();
		}

		return itemsLeft;
	}

	default List<ItemStack> toList() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for (ItemStack i : this) {
			list.add(i);
		}
		return list;
	}

	default Iterator<ItemStack> iterator() {
		return new InventoryIterator(this);
	}

	default Spliterator<ItemStack> spliterator() {
		return Spliterators.spliterator(iterator(), getSize(), Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SORTED);
	}

	default Stream<ItemStack> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
}
