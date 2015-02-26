package nova.core.item;

import nova.core.retention.Data;
import nova.core.retention.Storable;
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
	 * Makes a new item with no data
	 *
	 * @return Resulting item
	 */
	public Item makeItem() {
		Data data = new Data();
		data.className = getDummy().getClass().getName();
		return makeItem(data);
	}

	/**
	 * Creates a new instance of the Item.
	 *
	 * @param data Item data, used if item is {@link Storable}
	 * @return Resulting item
	 */
	public Item makeItem(Data data) {
		Item newItem = constructor.get();
		if (newItem instanceof Storable) {
			((Storable) newItem).load(data);
		}
		return newItem;
	}

	public Data saveItem(Item item) {
		Data data = new Data();
		if (item instanceof Storable) {
			((Storable) item).save(data);
		}
		return data;
	}
}
