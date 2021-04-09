/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.recipes.crafting;

import nova.core.entity.component.Player;
import nova.core.item.Item;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents a crafting grid. Crafting grids contain an item in each slot, each of which can be read and possibly
 * modified.
 * @author Stan Hebben
 */
public interface CraftingGrid extends Iterable<Item> {
	String TOPOLOGY_SQUARE = "nova:square";
	String TYPE_CRAFTING = "nova:crafting";

	/**
	 * Represents the player that is currently using this crafting grid.
	 * @return crafting grid player
	 */
	Optional<Player> getPlayer();

	/**
	 * Returns the total size of this crafting grid. For a square crafting grid, this is width x height. Note that the
	 * size can be less than width x height in the case of non-square crafting grid (but never more).
	 * @return The size
	 */
	int size();

	/**
	 * Gets the item in a specified slot.
	 * @param slot slot index
	 * @return the item in the given slot
	 * @see #getCrafting(int, int)
	 */
	Optional<Item> getCrafting(int slot);

	/**
	 * Gets the item at the given (x, y) position.
	 * Returns an empty optional if there is no the item at that position.
	 * @param x x position
	 * @param y y position
	 * @return the item at the given position
	 * @see #getCrafting(int)
	 */
	Optional<Item> getCrafting(int x, int y);

	/**
	 * Modifies the item in the given slot. If the modification is not possible, this method returns false. If modification
	 * was successful, returns true.
	 *
	 * Slots in a crafting grid should also be ordered with the slots at the y=0 first, starting from smallest x to
	 * largest x and then from smallest y to largest y (natural order). However, not all (x, y) positions need to have
	 * a corresponding slot.
	 * @param slot slot to be modified
	 * @param item the item to be setCrafting
	 * @return true if modification was successful, false otherwise
	 * @see #setCrafting(int, int, Optional)
	 */
	boolean setCrafting(int slot, Optional<Item> item);

	/**
	 * Sets the item at the given (x, y) position.
	 * @param x x position
	 * @param y y position
	 * @param item the item to be setCrafting
	 * @return true if the modification is successful, false otherwise
	 * @see #setCrafting(int, Optional)
	 */
	boolean setCrafting(int x, int y, Optional<Item> item);

	/**
	 * Removes the item in a specified slot.
	 * @param slot slot index
	 * @return the previous item in the given slot
	 * @see #removeCrafting(int, int)
	 */
	default Optional<Item> removeCrafting(int slot) {
		Optional<Item> ret = CraftingGrid.this.getCrafting(slot);
		CraftingGrid.this.setCrafting(slot, Optional.empty());
		return ret;
	}

	/**
	 * Removes the item at the given (x, y) position.
	 * @param x x position
	 * @param y y position
	 * @return the previous item at the position
	 * @see #removeCrafting(int)
	 */
	default Optional<Item> removeCrafting(int x, int y) {
		Optional<Item> ret = getCrafting(x, y);
		setCrafting(x, y, Optional.empty());
		return ret;
	}

	/**
	 * Gets the width of the crafting grid. For a non-square grid,, this should return the highest acceptable x-value + 1.
	 * @return crafting grid width
	 */
	int getWidth();

	/**
	 * Gets the height of the crafting grid. For a non-square grid, this should return the highest acceptable y-value + 1.
	 * @return crafting grid height
	 */
	int getHeight();

	/**
	 * Gives back a certain item. In the case of a player's crafting grid,
	 * this would typically go back to the player's inventory.
	 * Machines may implement this method differently.
	 *
	 * @param item The {@link Item} to give back.
	 */
	default void giveBack(Item item) {
		getPlayer().map(Player::getInventory).ifPresent(inv -> inv.add(item));
	}

	/**
	 * Gets the topology of the crafting grid. For a square grid, this should be {@link #TOPOLOGY_SQUARE}. Other
	 * kinds of grids may return a different value.
	 * @return crafting grid topology
	 */
	String getTopology();

	/**
	 * Gets the type of crafting grid. For a crafting recipe, this should return {@link #TYPE_CRAFTING}. Other
 machines or crafting tables (with a separate setCrafting of recipes) may return a different value.
	 * @return crafting grid type
	 */
	String getType();

	/**
	 * Counts the number of filled the items in this crafting grid.
	 * @return number of non-empty the items in this crafting grid
	 */
	default int countFilledStacks() {
		int filledStacks = 0;
		for (int i = 0; i < size(); i++) {
			if (CraftingGrid.this.getCrafting(i).isPresent()) {
				filledStacks++;
			}
		}
		return filledStacks;
	}

	/**
	 * Gets the first non-empty item in this crafting grid. Returns empty if and only if the crafting grid is completely
	 * empty.
	 * @return first non-empty item
	 */
	default Optional<Item> getFirstNonEmptyItem() {
		for (int i = 0; i < size(); i++) {
			Optional<Item> item = CraftingGrid.this.getCrafting(i);
			if (item.isPresent()) {
				return item;
			}
		}

		return Optional.empty();
	}

	/**
	 * Finds the position of the first non-empty the item in this crafting grid. Returns empty if and only if the crafting
	 * grid is completely empty.
	 * @return first non-empty item position
	 */
	default Optional<Vector2D> getFirstNonEmptyPosition() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (getCrafting(x, y).isPresent()) {
					return Optional.of(new Vector2D(x, y));
				}
			}
		}

		return Optional.empty();
	}

	@Override
	default Iterator<Item> iterator() {
		return new CraftingGridIterator(this);
	}

	@Override
	default Spliterator<Item> spliterator() {
		return Spliterators.spliterator(iterator(), size(), Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SORTED);
	}

	/**
	 * Represents this crafting grid as an {@link Item} {@link Stream}
	 * @return This crafting grid as an {@link Item} {@link Stream}
	 */
	default Stream<Item> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
}
