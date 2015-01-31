package nova.core.item;

import nova.core.util.Factory;
import nova.core.util.Identifiable;

import java.util.function.Supplier;

/**
 * @author Calclavia
 */
public class ItemFactory extends Factory<Item> implements Identifiable {

	public ItemFactory(Supplier<Item> constructor) {
		super(constructor);
	}

	/**
	 * Creates a new instance of the Item.
	 */
	public Item makeItem() {
		Item newItem = constructor.get();
		return newItem;
	}
}
