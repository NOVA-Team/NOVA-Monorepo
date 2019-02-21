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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.backward;

import net.minecraft.nbt.NBTTagCompound;
import nova.core.component.misc.FactoryProvider;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.retention.Data;
import nova.core.wrapper.mc.forge.v1_11_2.util.WrapperEvent;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.data.DataConverter;
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
		super(net.minecraft.item.Item.REGISTRY.getNameForObject(item) + (item.getHasSubtypes() ? ":" + meta : ""), () -> new BWItem(item, meta, null, null));

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
	public String getUnlocalizedName() {
		return this.item.getUnlocalizedName();
	}

	@Override
	public Item build(Data data) {
		final int meta = (Integer) data.getOrDefault("damage", this.meta);
		final NBTTagCompound capsData = DataConverter.instance().toNative(data.get("forgeCaps"));
		final NBTTagCompound nbtData;
		if (data.containsKey("tag"))
			nbtData = DataConverter.instance().toNative(data.get("tag"));
		else
			nbtData = DataConverter.instance().toNative(data);

		BWItem bwItem = new BWItem(item, meta, nbtData, capsData);
		bwItem.components.add(new FactoryProvider(this));
		WrapperEvent.BWItemCreate event = new WrapperEvent.BWItemCreate(bwItem, item);
		Game.events().publish(event);
		return bwItem;
	}

	@Override
	public Data save(Item item) {
		if (!(item instanceof BWItem)) {
			throw new IllegalArgumentException("This factory can only handle MCItems");
		}

		BWItem mcItem = (BWItem) item;

		Data result = new Data();
		mcItem.getTag().map(DataConverter.instance()::toNova).ifPresent(caps -> result.put("tag", caps));
		mcItem.getCaps().map(DataConverter.instance()::toNova).ifPresent(caps -> result.put("forgeCaps", caps));
		if (mcItem.getMeta() != meta) {
			result.put("damage", mcItem.getMeta());
		}

		return result;
	}
}
