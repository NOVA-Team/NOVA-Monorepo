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

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import nova.core.component.renderer.StaticRenderer;
import nova.core.item.Item;
import nova.core.retention.Storable;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.forward.IFWItem;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.render.backward.BWBakedModel;

import java.util.Optional;

/**
 * @author Stan
 * @since 3/02/2015.
 */
// TODO: Make this class into a wrapper around an ItemStack instance.
public class BWItem extends Item implements Storable {
	private final net.minecraft.item.Item item;
	private final int meta;
	private final NBTTagCompound tag;
	private final NBTTagCompound caps;

	public BWItem(ItemStack itemStack) {
		this(itemStack.getItem(), itemStack.getHasSubtypes() ? itemStack.getItemDamage() : 0, itemStack.getTagCompound(), itemStack.serializeNBT().getCompoundTag("ForgeCaps"));
	}

	public BWItem(net.minecraft.item.Item item, int meta, NBTTagCompound tag, NBTTagCompound caps) {
		this.item = item;
		this.meta = meta;
		this.tag  = tag;
		this.caps = caps;

		components.add(new StaticRenderer())
			.onRender(model -> {
				model.addChild(new BWBakedModel(Minecraft.getMinecraft().getRenderItem()
					.getItemModelMesher().getItemModel(makeItemStack(count()))));
			});
	}

	public net.minecraft.item.Item getItem() {
		return item;
	}

	public int getMeta() {
		return meta;
	}

	public Optional<NBTTagCompound> getTag() {
		return Optional.ofNullable(tag);
	}

	public Optional<NBTTagCompound> getCaps() {
		return Optional.ofNullable(caps);
	}

	public ItemStack makeItemStack(int stackSize) {
		ItemStack result = new ItemStack(item, stackSize, meta, caps);
		if (tag != null) {
			result.setTagCompound(tag);
		}
		return result;
	}

	@Override
	public String getLocalizedName() {
		return this.item.getItemStackDisplayName(makeItemStack(count()));
	}

	@Override
	public String getUnlocalizedName() {
		return this.item.getUnlocalizedName(makeItemStack(count()));
	}

	@Override
	public int getMaxCount() {
		return this.item.getItemStackLimit(makeItemStack(count()));
	}

	@Override
	public String toString() {
		return getID();
	}
}
