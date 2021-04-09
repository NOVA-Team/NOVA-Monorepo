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

package nova.core.wrapper.mc.forge.v1_8.wrapper.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import nova.core.item.Item;

/**
 * A wrapped NBTTagCompound object that references the item instance
 * @author Stan
 * @since 3/02/2015.
 */
public class FWNBTTagCompound extends NBTTagCompound {
	private final Item item;

	public FWNBTTagCompound(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	@Override
	@SuppressWarnings("unchecked")
	public NBTBase copy() {
		FWNBTTagCompound result = new FWNBTTagCompound(item);
		((Iterable<String>) getKeySet()).forEach(s -> result.setTag(s, getTag(s).copy()));
		return result;
	}
}
