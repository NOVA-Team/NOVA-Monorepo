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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.forward;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.world.World;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.language.Translatable;
import nova.core.util.Direction;
import nova.core.util.math.MathUtil;
import nova.core.wrapper.mc.forge.v1_11_2.util.WrapperEvent;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.capability.forward.FWCapabilityProvider;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.backward.BWEntity;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.ItemConverter;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * An interface implemented by {@link FWItem} and {@link FWItemBlock} classes to override Minecraft's item events.
 * @author Calclavia
 */
public interface IFWItem extends Translatable {

	ItemFactory getItemFactory();

	@Nullable
	default FWCapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		Item item = ItemConverter.instance().popNativeConversion().orElseGet(() -> ItemConverter.instance().getNovaItem(stack));
		WrapperEvent.FWItemInitCapabilities event = new WrapperEvent.FWItemInitCapabilities(item, new FWItemCapabilityProvider(item));
		Game.events().publish(event);
		return event.capabilityProvider;
	}

	default void addInformation(ItemStack itemStack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		Item item = ItemConverter.instance().toNova(itemStack);
		item.setCount(itemStack.getCount()).events.publish(new Item.TooltipEvent(Optional.of(new BWEntity(player)), tooltip));
		getItemFactory().save(item);
	}

	default EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Item item = ItemConverter.instance().toNova(itemStack);
		Item.UseEvent event = new Item.UseEvent(new BWEntity(player), new Vector3D(x, y, z), Direction.fromOrdinal(side), new Vector3D(hitX, hitY, hitZ));
		item.events.publish(event);
		ItemConverter.instance().updateMCItemStack(itemStack, item);
		return event.action ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
	}

	default ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		Item item = ItemConverter.instance().toNova(itemStack);
		item.events.publish(new Item.RightClickEvent(new BWEntity(player)));
		return new ActionResult<>(EnumActionResult.PASS, ItemConverter.instance().updateMCItemStack(itemStack, item));
	}

	default int getItemStackLimit(ItemStack stack) {
		return MathUtil.max(ItemConverter.instance().toNova(stack).getMaxCount(), 64);
	}

	@Override
	default String getUnlocalizedName() {
		return getItemFactory().getUnlocalizedName();
	}

	@Override
	default String getLocalizedName() {
		return getItemFactory().getLocalizedName();
	}

	default String getUnlocalizedName(ItemStack stack) {
		return ItemConverter.instance().toNova(stack).getUnlocalizedName();
	}

	default String getItemStackDisplayName(ItemStack stack) {
		return ItemConverter.instance().toNova(stack).getLocalizedName();
	}
}
