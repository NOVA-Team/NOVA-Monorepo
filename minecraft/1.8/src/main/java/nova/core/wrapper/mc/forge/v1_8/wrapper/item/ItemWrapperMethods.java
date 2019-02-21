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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.util.Direction;
import nova.core.wrapper.mc.forge.v1_8.wrapper.entity.backward.BWEntity;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.List;
import java.util.Optional;

/**
 * An interface implemented by ItemBlockWrapper and ItemWrapper classes to override Minecraft's item events.
 * @author Calclavia
 */
public interface ItemWrapperMethods {

	ItemFactory getItemFactory();

	/**
	 * Allows items to add custom lines of information to the mouseover description
	 *
	 * @param itemStack The ItemStack instance
	 * @param player The player entity
	 * @param tooltip All lines to display in the Item's tooltip. This is a List of Strings.
	 * @param advanced Whether the setting "Advanced tooltips" is enabled
	 */
	default void addInformation(ItemStack itemStack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		Item item = ItemConverter.instance().toNova(itemStack);
		item.setCount(itemStack.stackSize).events.publish(new Item.TooltipEvent(Optional.of(new BWEntity(player)), tooltip));
		getItemFactory().save(item);
	}

	default boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Item item = ItemConverter.instance().toNova(itemStack);
		Item.UseEvent event = new Item.UseEvent(new BWEntity(player), new Vector3D(x, y, z), Direction.fromOrdinal(side), new Vector3D(hitX, hitY, hitZ));
		item.events.publish(event);
		ItemConverter.instance().updateMCItemStack(itemStack, item);
		return event.action;
	}

	default ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		Item item = ItemConverter.instance().toNova(itemStack);
		item.events.publish(new Item.RightClickEvent(new BWEntity(player)));
		return ItemConverter.instance().updateMCItemStack(itemStack, item);
	}

	@SuppressWarnings("deprecation")
	default int getColorFromItemStack(ItemStack itemStack, int p_82790_2_) {
		return ItemConverter.instance().toNova(itemStack).colorMultiplier().argb();
	}
}
