package nova.wrapper.mc1710.wrapper.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import nova.core.inventory.Inventory;
import nova.wrapper.mc1710.wrapper.item.ItemConverter;

public class FWInventory implements IInventory {

	public final Inventory wrapped;

	public FWInventory(Inventory inventory) {
		this.wrapped = inventory;
	}

	@Override
	public int getSizeInventory() {
		return wrapped.size();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return ItemConverter.instance().toNative(wrapped.get(slot).orElse(null));
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		stack.stackSize -= amount;
		if (stack.stackSize < 0) {
			setInventorySlotContents(slot, null);
			return null;
		}
		markDirty();
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		wrapped.set(slot, stack != null ? ItemConverter.instance().getNovaItem(stack) : null);
	}

	@Override
	public String getInventoryName() {
		// TODO Shouldn't be null
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		wrapped.markChanged();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void openInventory() {

	}

	@Override
	public void closeInventory() {

	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		// TODO Auto-generated method stub
		return true;
	}

}
