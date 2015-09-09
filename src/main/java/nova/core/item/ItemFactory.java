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
	 * @return Resulting item
	 */
	@Override
	public Item build() {
		Data data = new Data();
		Item newItem = constructor.get();
		data.className = newItem.getClass().getName();
		newItem.load(data);
		return newItem;
	}

	/**
	 * Creates a new instance of the Item.
	 * @param data Item data, used if item is {@link Storable}
	 * @return Resulting item
	 */
	public Item build(Data data) {
		Item newItem = constructor.get();
		newItem.load(data);
		return newItem;
	}

	public Data save(Item item) {
		Data data = new Data();
		item.save(data);
		return data;
	}
}
