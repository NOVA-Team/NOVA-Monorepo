package nova.wrapper.mc18.wrapper.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import nova.core.inventory.Inventory;
import nova.core.item.Item;
import nova.internal.core.Game;

import java.util.Optional;

public class BWInventory implements Inventory {
	public final IInventory wrapped;

	public BWInventory(IInventory mcInventory) {
		this.wrapped = mcInventory;
	}

	@Override
	public Optional<Item> get(int i) {
		ItemStack stackInSlot = wrapped.getStackInSlot(i);

		if (stackInSlot == null) {
			return Optional.empty();
		}

		return Optional.of(Game.natives().toNova(stackInSlot));
	}

	@Override
	public boolean set(int i, Item item) {
		wrapped.setInventorySlotContents(i, Game.natives().toNative(item));
		return true;
	}

	@Override
	public Optional<Item> remove(int slot) {
		Optional<Item> item = get(slot);
		wrapped.setInventorySlotContents(slot, null);
		return item;
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
