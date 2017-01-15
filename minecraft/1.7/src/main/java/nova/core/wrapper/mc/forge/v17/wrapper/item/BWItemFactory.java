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

package nova.core.wrapper.mc.forge.v17.wrapper.item;

import net.minecraft.nbt.NBTTagCompound;
import nova.core.component.misc.FactoryProvider;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.retention.Data;
import nova.core.util.id.StringIdentifier;
import nova.internal.core.Game;

/**
 * A Minecraft wrapped item factory.
 * @author Stan
 * @since 3/02/2015.
 */
public class BWItemFactory extends ItemFactory {
	private final net.minecraft.item.Item item;
	private final int meta;

	public BWItemFactory(net.minecraft.item.Item item, int meta) {
		super(new StringIdentifier(net.minecraft.item.Item.itemRegistry.getNameForObject(item) + (item.getHasSubtypes() ? ":" + meta : "")), () -> new BWItem(item, meta, null));

		this.item = item;
		this.meta = meta;
	}

	public net.minecraft.item.Item getItem() {
		return item;
	}

	public int getMeta() {
		return meta;
	}

	@Override
	public Item build(Data data) {
		int meta = (Integer) data.getOrDefault("damage", this.meta);
		NBTTagCompound nbtData = Game.natives().toNative(data);
		BWItem bwItem = new BWItem(item, meta, nbtData);
		bwItem.components.add(new FactoryProvider(this));
		return bwItem;
	}

	@Override
	public Data save(Item item) {
		if (!(item instanceof BWItem)) {
			throw new IllegalArgumentException("This factory can only handle MCItems");
		}

		BWItem mcItem = (BWItem) item;

		Data result = mcItem.getTag() != null ? Game.natives().toNova(mcItem.getTag()) : new Data();
		if (result == null) {
			result = new Data();
		}

		if (mcItem.getMeta() != meta) {
			result.put("damage", mcItem.getMeta());
		}

		return result;
	}
}
