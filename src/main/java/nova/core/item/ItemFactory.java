package nova.core.item;

import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.Factory;
import nova.core.util.Identifiable;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Calclavia
 */
public class ItemFactory extends Factory<ItemFactory, Item> implements Identifiable {

	public ItemFactory(Supplier<Item> constructor) {
		super(constructor);
	}

	private ItemFactory(Supplier<Item> constructor, Function<Item, Item> processor) {
		super(constructor, processor);
	}

	/**
	 * Makes a new item with no data
	 * @return Resulting item
	 */
	@Override
	public Item build() {
		Data data = new Data();
		Item newItem = super.build();
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
		Item newItem = super.build();
		newItem.load(data);
		return newItem;
	}

	public Data save(Item item) {
		Data data = new Data();
		item.save(data);
		return data;
	}

	@Override
	public ItemFactory selfConstructor(Supplier<Item> constructor, Function<Item, Item> processor) {
		return new ItemFactory(constructor, processor);
	}
}
