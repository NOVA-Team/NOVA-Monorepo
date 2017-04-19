/*
 * Copyright (c) 2017 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.testutils;

import nova.core.entity.component.Player;
import nova.core.item.Item;
import nova.core.recipes.crafting.CraftingGrid;
import nova.core.util.Identifiable;
import nova.core.util.math.MathUtil;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.StringJoiner;

/**
 * @author ExE Boss
 */
public class FakeCraftingGrid implements CraftingGrid {

	private Optional<Player> player;
	private final Optional<Item>[] slots;
	private final int width, height;

	public FakeCraftingGrid(int width, int height) {
		this(Optional.empty(), width, height);
	}

	public FakeCraftingGrid(Player player, int width, int height) {
		this(Optional.of(player), width, height);
	}

	@SuppressWarnings("unchecked")
	public FakeCraftingGrid(Optional<Player> player, int width, int height) {
		this.player = player;
		this.width  = width;
		this.height = height;
		this.slots  = (Optional<Item>[]) new Optional<?>[width * height];
	}

	public FakeCraftingGrid(int width, int height, Optional<Item>[] grid) {
		this(Optional.empty(), width, height, grid);
	}

	public FakeCraftingGrid(Optional<Player> player, int width, int height, Optional<Item>[] grid) {
		this(player, width, height);
		if (grid.length == slots.length) {
			for (int i = 0; i < grid.length; i++) {
				slots[i] = grid[i];
				if (slots[i] == null)
					slots[i] = Optional.empty();
			}
		} else {
			throw new IllegalArgumentException("Grid size (" + grid.length + ") must be equal to 'width * height' (" + slots.length + ')');
		}
	}

	public FakeCraftingGrid(int width, int height, Item[] grid) {
		this(Optional.empty(), width, height, grid);
	}

	public FakeCraftingGrid(Optional<Player> player, int width, int height, Item[] grid) {
		this(player, width, height);
		if (grid.length == slots.length) {
			for (int i = 0; i < grid.length; i++) {
				slots[i] = Optional.ofNullable(grid[i]);
			}
		} else {
			throw new IllegalArgumentException("Grid size (" + grid.length + ") must be equal to 'width * height' (" + slots.length + ')');
		}
	}

	@Override
	public Optional<Player> getPlayer() {
		return player;
	}

	@Override
	public int size() {
		return slots.length;
	}

	@Override
	public Optional<Item> getCrafting(int slot) {
		return slot >= 0 && slot < size() ? slots[slot] : Optional.empty();
	}

	@Override
	public boolean setCrafting(int slot, Optional<Item> item) {
		if (slot >= size() || slot < 0) {
			return false;
		}

		Optional<Item> orig = getCrafting(slot);
		if (orig.equals(item)) {
			return false;
		}

		slots[slot] = item;
		return true;
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
	public Optional<Item> getCrafting(int x, int y) {
		OptionalInt slot = coordsToSlot(x, y);
		if (slot.isPresent()) {
			return getCrafting(slot.getAsInt());
		} else {
			return Optional.empty();
		}
	}

	@Override
	public boolean setCrafting(int x, int y, Optional<Item> item) {
		OptionalInt slot = coordsToSlot(x, y);
		if (slot.isPresent()) {
			return setCrafting(slot.getAsInt(), item);
		} else {
			return false;
		}
	}

	@Override
	public String getTopology() {
		return TOPOLOGY_SQUARE;
	}

	@Override
	public String getType() {
		return TYPE_CRAFTING;
	}

	public final OptionalInt coordsToSlot(int x, int y) {
		if (x < 0 || x > getWidth()) return OptionalInt.empty();
		if (y < 0 || y > getHeight()) return OptionalInt.empty();

		return OptionalInt.of(x + y * getWidth());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("FakeCraftingGrid:").append(System.lineSeparator());
		if (getHeight() == 0) {
			StringJoiner sj = new StringJoiner("+", "+", "+");
			for (int x = 0; x < getWidth(); x++) {
				sj.add("-");
			}
			return sb.append(sj).append(sj).toString();
		} else if (getWidth() == 0) {
			sb.append("++").append(System.lineSeparator());
			for (int x = 0; x < getWidth(); x++) {
				sb.append("||").append(System.lineSeparator());
				sb.append("++").append(System.lineSeparator());
			}
			return sb.append("++").toString();
		}
		int longestItemIdLength = this.stream().map(Item::getID).mapToInt(String::length).reduce(6, MathUtil::max);

		StringBuilder sb1 = new StringBuilder(longestItemIdLength + 2).append('-');
		for (int i = 0; i < longestItemIdLength; i++) {
			sb1.append('-');
		}
		String verticalLineSeparator = sb1.append('-').toString();
		String itemFormat = " %-" + longestItemIdLength + "s ";

		StringJoiner sjvl = new StringJoiner("+", "+", "+");
		for (int x = 0; x < getWidth(); x++) {
			sjvl.add(verticalLineSeparator);
		}
		verticalLineSeparator = sjvl.toString();
		sb.append(verticalLineSeparator);
		for (int y = 0; y < getHeight(); y++) {
			StringJoiner sj = new StringJoiner("|", "|", "|");
			for (int x = 0; x < getWidth(); x++) {
				sj.add(String.format(itemFormat, getCrafting(x, y).map(Identifiable::getID).orElse("<null>")));
			}
			sb.append(System.lineSeparator()).append(sj).append(System.lineSeparator()).append(verticalLineSeparator);
		}

		return sb.toString();
	}
}
