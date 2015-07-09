package nova.core.inventory;

import nova.core.component.Component;
import nova.core.item.Item;
import nova.core.network.Packet;
import nova.core.network.Syncable;
import nova.core.retention.Data;
import nova.core.retention.Storable;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class provides implementation of {@link Inventory}
 */
public class InventorySimple extends Component implements Inventory, Storable, Syncable {

	public BiFunction<Integer, Item, Boolean> isItemValidForSlot = (slot, item) -> true;
	private Item[] items;
	private boolean changed = false;

	public InventorySimple() {
		this(0);
	}

	public InventorySimple(int size) {
		items = new Item[size];
	}

	/**
	 * Tells if this inventory has changed since last
	 * invocation of {@link #clearChanged()}
	 *
	 * @return Whether the inventory has changed
	 */
	public boolean hasChanged() {
		return changed;
	}

	@Override
	public void markChanged() {
		changed = true;
	}

	/**
	 * Marks this inventory as unchanged
	 */
	public void clearChanged() {
		changed = false;
	}

	@Override
	public int size() {
		return items.length;
	}

	@Override
	public Optional<Item> get(int slot) {
		if (slot < 0 || slot >= items.length) {
			return Optional.empty();
		} else {
			return Optional.ofNullable(items[slot]);
		}
	}

	@Override
	public boolean set(int slot, Item item) {
		if (slot < 0 || slot >= items.length || !isItemValidForSlot.apply(slot, item)) {
			return false;
		} else {
			items[slot] = item;
			changed = true;
			return true;
		}
	}

	@Override
	public Optional<Item> remove(int slot) {
		Item item = items[slot];
		items[slot] = null;
		return Optional.ofNullable(item);
	}

	public Optional<Item> swap(int slot, Item item) {
		Optional<Item> current = get(slot);
		set(slot, item);
		return current;
	}

	@Override
	public void save(Data data) {
		data.put("size", size());
		data.putAll(IntStream.range(0, size()).filter(i -> items[i] != null).boxed().collect(Collectors.toMap(i -> i + "", i -> items[i])));
	}

	@Override
	public void load(Data data) {
		items = new Item[(int) data.get("size")];
		IntStream.range(0, size()).forEach(i -> {
			Data itemData = data.get(i + "");
			if (itemData != null) {
				items[i] = (Item) Data.unserialize(itemData);
			}
		});
	}

	@Override
	public void read(Packet packet) {
		items = new Item[packet.readInt()];
		IntStream.range(0, size()).forEach(i -> {
			if (packet.readBoolean()) {
				items[i] = (Item) packet.readStorable();
			} else {
				items[i] = null;
			}
		});
	}

	@Override
	public void write(Packet packet) {
		packet.write(size());
		IntStream.range(0, size()).forEach(i -> {
			if (get(i).isPresent()) {
				packet.writeBoolean(true);
				packet.write(items[i]);
			} else {
				packet.writeBoolean(false);
			}
		});
	}
}
