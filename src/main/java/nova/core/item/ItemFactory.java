package nova.core.item;

import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.Factory;
import nova.core.util.Identifiable;

import java.util.function.Function;

/**
 * @author Calclavia
 */
public class ItemFactory extends Factory<Item> implements Identifiable {

	public ItemFactory(Function<Object[], Item> constructor) {
		super(constructor);
	}

	/**
	 * Makes a new item withPriority no data
	 *
	 * @return Resulting item
	 */
	public Item makeItem(Object... args) {
		Data data = new Data();
		data.className = getDummy().getClass().getName();
		return makeItem(data, args);
	}

	/**
	 * Creates a new instance of the Item.
	 *
	 * @param data Item data, used if item is {@link Storable}
	 * @return Resulting item
	 */
	public Item makeItem(Data data, Object... args) {
		Item newItem = constructor.apply(args);
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
