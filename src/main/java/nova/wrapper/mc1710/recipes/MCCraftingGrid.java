package nova.wrapper.mc1710.recipes;

import java.util.List;
import java.util.Optional;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import nova.core.player.Player;
import nova.core.recipes.crafting.CraftingGrid;
import nova.wrapper.mc1710.util.ReflectionUtil;
import nova.wrapper.mc1710.util.WrapUtility;

/**
 *
 * @author Stan Hebben
 */
public class MCCraftingGrid implements CraftingGrid {
	private static final ThreadLocal<MCCraftingGrid> cache = new ThreadLocal<MCCraftingGrid>();
	private static final ThreadLocal<MCCraftingGrid> cache2 = new ThreadLocal<MCCraftingGrid>();
	
	public static MCCraftingGrid get(InventoryCrafting inventory) {
		if (cache.get() == null || cache.get().inventory != inventory) {
			MCCraftingGrid result = new MCCraftingGrid(inventory);
			cache.set(result);
			return result;
		} else {
			MCCraftingGrid result = cache.get();
			result.update();
			return result;
		}
	}
	
	public static MCCraftingGrid get(IInventory inventory, EntityPlayer player) {
		if (cache2.get() == null || cache2.get().inventory != inventory || cache2.get().playerOrig != player) {
			MCCraftingGrid result = new MCCraftingGrid(inventory, player);
			cache2.set(result);
			return result;
		} else {
			MCCraftingGrid result = cache2.get();
			result.update();
			return result;
		}
	}
	
	private int width;
	private int height;
	private final IInventory inventory;
	private nova.core.item.ItemStack[] stacks;
	private net.minecraft.item.ItemStack[] original;
	private int numberOfStacks;
	private final Optional<Player> player;
	private final EntityPlayer playerOrig;
	
	private MCCraftingGrid(InventoryCrafting inventory) {
		this.inventory = inventory;
		width = height = (int) Math.sqrt(inventory.getSizeInventory());
		stacks = new nova.core.item.ItemStack[width * height];
		original = new net.minecraft.item.ItemStack[stacks.length];
		numberOfStacks = 0;
		update();
		
		Container container = ReflectionUtil.getCraftingContainer(inventory);
		if (container != null) {
			List<Slot> slots = container.inventorySlots;
			if (!slots.isEmpty() && slots.get(0) instanceof SlotCrafting) {
				SlotCrafting slotCrafting = (SlotCrafting) slots.get(0);
				playerOrig = ReflectionUtil.getCraftingSlotPlayer(slotCrafting);
				player = WrapUtility.getNovaPlayer(playerOrig);
			} else {
				playerOrig = null;
				player = null;
			}
		} else {
			playerOrig = null;
			player = null;
		}
	}
	
	public MCCraftingGrid(IInventory inventory, EntityPlayer player) {
		this.inventory = inventory;
		width = height = (int) Math.sqrt(inventory.getSizeInventory());
		stacks = new nova.core.item.ItemStack[width * height];
		original = new ItemStack[stacks.length];
		numberOfStacks = 0;
		update();
		
		playerOrig = player;
		this.player = WrapUtility.getNovaPlayer(player);
	}
	
	private void update() {
		if (inventory.getSizeInventory() != original.length) {
			width = height = (int) Math.sqrt(inventory.getSizeInventory());
			stacks = new nova.core.item.ItemStack[inventory.getSizeInventory()];
			original = new ItemStack[stacks.length];
			numberOfStacks = 0;
		}
		
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (changed(i)) {
				//System.out.println("Slot " + i + " changed");
				original[i] = inventory.getStackInSlot(i);
				if (inventory.getStackInSlot(i) != null) {
					if (stacks[i] == null)
                        numberOfStacks++;

					stacks[i] = WrapUtility.unwrapItemStack(original[i]).get();
				} else {
					if (stacks[i] != null)
                        numberOfStacks--;

					stacks[i] = null;
				}
			}
		}
		//System.out.println("Num stack count: " + numberOfStacks);
	}
	
	@Override
	public Optional<Player> getPlayer() {
		return player;
	}

	@Override
	public int size() {
		return width * height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public int countFilledStacks() {
		return numberOfStacks;
	}

	@Override
	public Optional<nova.core.item.ItemStack> getStack(int i) {
		return Optional.ofNullable(stacks[i]);
	}

	@Override
	public Optional<nova.core.item.ItemStack> getStack(int x, int y) {
		return Optional.ofNullable(stacks[y * width + x]);
	}

	@Override
	public boolean setStack(int x, int y, Optional<nova.core.item.ItemStack> stack) {
		//System.out.println("SetStack(" + x + ", " + y + ") " + stack);
		
		int ix = y * width + x;
		if (!stack.equals(stacks[ix])) {
			if (stack.isPresent()) {
                inventory.setInventorySlotContents(ix, WrapUtility.wrapItemStack(stack));

                if (stacks[ix] == null) {
                    numberOfStacks++;
                }

                stacks[ix] = stack.get();
            } else {
				numberOfStacks--;
				inventory.setInventorySlotContents(ix, null);
                stacks[ix] = null;
			}
		}

        return true;
	}

	@Override
	public boolean setStack(int i, Optional<nova.core.item.ItemStack> stack) {
		//System.out.println("SetStack(" + i + ") " + stack);

        if (stack == null) {
            if (stacks[i] == null)
                return true;

            numberOfStacks--;
            inventory.setInventorySlotContents(i, null);
            stacks[i] = null;
        } else {
            if (stacks[i] == null)
                numberOfStacks++;

            inventory.setInventorySlotContents(i, WrapUtility.wrapItemStack(stack));
            stacks[i] = stack.get();
        }

        return true;
	}

    @Override
    public void giveBack(nova.core.item.ItemStack itemStack) {
        playerOrig.inventory.addItemStackToInventory(WrapUtility.wrapItemStack(itemStack));
    }

    @Override
    public String getTopology() {
        return CraftingGrid.TOPOLOGY_SQUARE;
    }

    @Override
    public String getType() {
        return CraftingGrid.TYPE_CRAFTING;
    }

	private boolean changed(int i) {
		if (original[i] != inventory.getStackInSlot(i))
            return true;

		if (original[i] != null && stacks[i].getStackSize() != original[i].stackSize)
            return true;
		
		return false;
	}
}
