package nova.wrapper.mc18.wrapper.inventory;

import net.minecraft.inventory.IInventory;
import nova.core.inventory.Inventory;
import nova.core.item.Item;

import java.util.Optional;

public class BWInventory implements Inventory {
	public final IInventory wrapped;

	public BWInventory(IInventory mcInventory) {
		this.wrapped = mcInventory;
	}

	@Override
	public Optional<Item> get(int i) {
		return Optional.empty();
	}

	@Override
	public boolean set(int i, Item Item) {
		return false;
	}

	@Override
	public int size() {
		return wrapped.getSizeInventory();
	}

	@Override
	public void markChanged() {
		wrapped.markDirty();
	}
}
