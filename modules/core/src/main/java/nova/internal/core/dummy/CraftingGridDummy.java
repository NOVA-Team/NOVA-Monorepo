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

package nova.internal.core.dummy;

import nova.core.entity.component.Player;
import nova.core.item.Item;
import nova.core.recipes.crafting.CraftingGrid;

import java.util.Optional;

/**
 * @author Stan
 * @since 3/02/2015.
 */
public class CraftingGridDummy implements CraftingGrid {
	public static final CraftingGrid instance = new CraftingGridDummy();

	private CraftingGridDummy() {
	}

	@Override
	public Optional<Player> getPlayer() {
		return Optional.empty();
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Optional<Item> getCrafting(int slot) {
		return Optional.empty();
	}

	@Override
	public boolean setCrafting(int slot, Optional<Item> Item) {
		return false;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public Optional<Item> getCrafting(int x, int y) {
		return Optional.empty();
	}

	@Override
	public boolean setCrafting(int x, int y, Optional<Item> Item) {
		return false;
	}

	@Override
	public void giveBack(Item Item) {

	}

	@Override
	public String getTopology() {
		return "dummy";
	}

	@Override
	public String getType() {
		return "dummy";
	}
}
