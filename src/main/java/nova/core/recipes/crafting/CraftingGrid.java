package nova.core.recipes.crafting;

import nova.core.item.ItemStack;
import nova.core.player.Player;
import nova.core.util.transform.Vector2i;

import java.util.Optional;

/**
 * Represents a crafting grid. Crafting grids contain an item stack in each slot, each of which can be read and possibly
 * modified.
 *
 * @author Stan Hebben
 */
public interface CraftingGrid {
	public static final String TOPOLOGY_SQUARE = "square";
	public static final String TYPE_CRAFTING = "crafting";

	/**
	 * Represents the player that is currently using this crafting grid. Can be null.
	 *
	 * @return crafting grid player
	 */
	public Optional<Player> getPlayer();

	/**
	 * Returns the total size of this crafting grid. For a square crafting grid, this is width x height. Note that the
	 * size can be less than width x height in the case of non-square crafting grid (but never more).
	 *
	 * @return The size
	 */
	public int size();

	/**
	 * Gets the stack in a specified slot. Can be null if empty.
	 *
	 * @param slot slot index
	 * @return item stack in the given slot
	 */
	public Optional<ItemStack> getStack(int slot);

	/**
	 * Modifies the stack in the given slot. If the modification is not possible, this method returns null. If modification
	 * was successful, returns true.
	 *
	 * Slots in a crafting grid should also be ordered with the slots at the y=0 first, starting from smallest x to
	 * largest x and then from smallest y to largest y (natural order). However, not all (x, y) positions need to have
	 * a corresponding slot.
	 *
	 * @param slot slot to be modified
	 * @param itemStack item stack to be set
	 * @return true if modification was successful, false otherwise
	 */
	public boolean setStack(int slot, Optional<ItemStack> itemStack);

	/**
	 * Gets the width of the crafting grid. For a non-square grid,, this should return the highest acceptable x-value + 1.
	 *
	 * @return crafting grid width
	 */
	public int getWidth();

	/**
	 * Gets the height of the crafting grid. For a non-square grid, this should return the highest acceptable y-value + 1.
	 *
	 * @return crafting grid height
	 */
	public int getHeight();

	/**
	 * Gets the stack at the given (x, y) position. Returns null if there is no stack at that position.
	 *
	 * @param x x position
	 * @param y y position
	 * @return stack at the given position
	 */
	public Optional<ItemStack> getStack(int x, int y);

	/**
	 * Sets the stack at the given (x, y) position.
	 *
	 * @param x x position
	 * @param y y posittion
	 * @param itemStack stack to be set
	 * @return true if the modification is successful, false otherwise
	 */
	public boolean setStack(int x, int y, Optional<ItemStack> itemStack);

	/**
	 * Gives back a certain item. In the case of a player's crafting grid, this would typically go back to the player's
	 * inventory. Machines may implement this method differently.
	 *
	 * @param itemStack The {@link ItemStack}
	 */
	public void giveBack(ItemStack itemStack);

	/**
	 * Gets the topology of the crafting grid. For a square grid, this should be CraftingGrid.TOPOLOGY_SQUARE. Other
	 * kinds of grids may return a different value.
	 *
	 * @return crafting grid topology
	 */
	public String getTopology();

	/**
	 * Gets the type of crafting grid. For a crafting recipe, this should return CraftingGrid.TYPE_CRAFTING. Other
	 * machines or crafting tables (with a separate set of recipes) may return a different value.
	 *
	 * @return crafting grid type0
	 */
	public String getType();

	/**
	 * Counts the number of filled stacks in this crafting grid.
	 *
	 * @return number of non-empty stacks in this crafting grid
	 */
	public default int countFilledStacks() {
		int filledStacks = 0;
		for (int i = 0; i < size(); i++) {
			if (getStack(i).isPresent()) {
				filledStacks++;
			}
		}
		return filledStacks;
	}

	/**
	 * Gets the first non-empty item in this crafting grid. Returns empty if and only if the crafting grid is completely
	 * empty.
	 *
	 * @return first non-empty item
	 */
	public default Optional<ItemStack> getFirstNonEmptyItem() {
		for (int i = 0; i < size(); i++) {
			Optional<ItemStack> stackInSlot = getStack(i);
			if (stackInSlot.isPresent()) {
				return stackInSlot;
			}
		}

		return Optional.empty();
	}

	/**
	 * Finds the position of the first non-empty stack in this crafting grid. Returns empty if and only if the crafting
	 * grid is completely empty.
	 *
	 * @return first non-empty item position
	 */
	public default Optional<Vector2i> getFirstNonEmptyPosition() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (getStack(x, y).isPresent()) {
					return Optional.of(new Vector2i(x, y));
				}
			}
		}

		return Optional.empty();
	}
}
