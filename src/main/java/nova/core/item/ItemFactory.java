package nova.core.item;

import nova.core.util.Factory;
import nova.core.util.Identifiable;
import nova.core.util.components.Storable;

import java.util.HashMap;
import java.util.Map;
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
	 * @param data Item data, used if item is {@link Storable}
	 * @return Resulting item
	 */
	public Item makeItem(Map<String, Object> data) {
		Item newItem = constructor.get();
		if (newItem instanceof Storable) {
			((Storable) newItem).load(data);
		}
		return newItem;
	}

	public Map<String, Object> saveItem(Item item) {
		Map<String, Object> data = new HashMap<>();
		if (item instanceof Storable) {
			((Storable) item).save(data);
		}
		return data;
	}
}
