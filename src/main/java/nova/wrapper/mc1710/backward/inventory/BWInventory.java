package nova.wrapper.mc1710.backward.inventory;

import java.util.Optional;

import net.minecraft.inventory.IInventory;
import nova.core.inventory.Inventory;
import nova.core.item.Item;

public class BWInventory implements Inventory {
	public final IInventory mcInventory;

	public BWInventory(IInventory mcInventory) {
		this.mcInventory = mcInventory;
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
		return mcInventory.getSizeInventory();
	}

	@Override
	public void markChanged() {
		mcInventory.markDirty();
	}
}
