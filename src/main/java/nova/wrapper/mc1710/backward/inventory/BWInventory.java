package nova.wrapper.mc1710.backward.inventory;

import net.minecraft.inventory.IInventory;
import nova.core.inventory.Inventory;
import nova.core.item.ItemStack;

import java.util.Optional;

public class BWInventory implements Inventory {
	private final IInventory mcInventory;

	public BWInventory(IInventory mcInventory) {
		this.mcInventory = mcInventory;
	}

	@Override
	public Optional<ItemStack> get(int i) {
		return Optional.empty();
	}

	@Override
	public boolean set(int i, ItemStack itemStack) {
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
