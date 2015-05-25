package nova.wrapper.mc1710.forward.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import nova.core.inventory.Inventory;
import nova.wrapper.mc1710.item.ItemWrapperRegistry;

public class FWInventory implements IInventory {

	private final Inventory inventory;

	public FWInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public int getSizeInventory() {
		return inventory.size();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return ItemWrapperRegistry.instance.getMCItemStack(inventory.get(slot).orElse(null));
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
		inventory.set(slot, stack != null ? ItemWrapperRegistry.instance.getNovaItem(stack) : null);
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
		inventory.markChanged();
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
