package nova.core.inventory;

import nova.core.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This interface provides inventory that can hold {@link ItemStack ItemStacks}
 *
 * @see InventorySimple
 * @see InventoryView
 */
public interface Inventory extends Iterable<ItemStack> {
	Optional<ItemStack> get(int slot);

	/**
	 * Sets {@link ItemStack} in slot
	 *
	 * @param slot Slot number
	 * @param stack Stack to insert
	 * @return Whether succeed
	 */
	boolean set(int slot, ItemStack stack);

	/**
	 * Gets count of slots
	 *
	 * @return Number of slots in this inventory
	 */
	int size();

	/**
	 * Tells this inventory that something has changed
	 */
	void markChanged();

	/**
	 * Adds items to this inventory at specified slot
	 *
	 * @param slot Slot to add items into
	 * @param stack {@link ItemStack} containing items
	 * @return Amount of items left(did not fit inside this inventory)
	 */
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

	/**
	 * Adds items to this inventory
	 *
	 * @param stack {@link ItemStack} containing items
	 * @return Amount of items left(did not fit inside this inventory)
	 */
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

	/**
	 * Represents this inventory as list of {@link ItemStack ItemStacks}
	 *
	 * @return This inventory as list of {@link ItemStack ItemStacks}
	 */
	default List<ItemStack> toList() {
		ArrayList<ItemStack> list = new ArrayList<>();
		for (ItemStack i : this) {
			list.add(i);
		}
		return list;
	}

	default Iterator<ItemStack> iterator() {
		return new InventoryIterator(this);
	}

	default Spliterator<ItemStack> spliterator() {
		return Spliterators.spliterator(iterator(), size(), Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SORTED);
	}

	/**
	 * Represents this inventory as {@link ItemStack} {@link Stream}
	 *
	 * @return This inventory as {@link ItemStack} {@link Stream}
	 */
	default Stream<ItemStack> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
}
