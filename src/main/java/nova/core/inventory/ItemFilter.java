package nova.core.inventory;

import nova.core.gui.component.inventory.Slot;
import nova.core.item.Item;

import java.util.function.Predicate;

/**
 * A filter that only accepts a specific sub-type of {@link Item}. For use with
 * {@link Slot}.
 * 
 * @author Vic Nightfall
 */
@FunctionalInterface
public interface ItemFilter extends Predicate<Item> {

	/**
	 * Returns an {@link ItemFilter} that accepts an {@link Item} of the same
	 * type as the provided.
	 * 
	 * @param item
	 * @return ItemFilter
	 */
	static ItemFilter of(Item item) {
		return item::sameItemType;
	}

	/**
	 * Returns an {@link ItemFilter} that accepts an {@link Item} of the same
	 * type as provided.
	 * 
	 * @param id
	 * @return ItemFilter
	 */
	static ItemFilter of(String id) {
		return (other) -> {
			return id.equals(other.getID());
		};
	}

	/**
	 * Accepts any {@link Item} that has a &gt;= stack size than provided.
	 * 
	 * @param amount
	 * @return
	 */
	static ItemFilter of(int amount) {
		return (other) -> {
			return other.count() >= amount;
		};
	}
}
