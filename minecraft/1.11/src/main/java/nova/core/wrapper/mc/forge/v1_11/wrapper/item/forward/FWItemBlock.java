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

package nova.core.wrapper.mc.forge.v1_11.wrapper.item.forward;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.wrapper.mc.forge.v1_11.util.WrapperEvent;
import nova.core.wrapper.mc.forge.v1_11.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc.forge.v1_11.wrapper.capability.forward.FWCapabilityProvider;
import nova.core.wrapper.mc.forge.v1_11.wrapper.item.ItemWrapperMethods;
import nova.internal.core.Game;

import java.util.List;

/**
 * @author Calclavia
 */
public class FWItemBlock extends net.minecraft.item.ItemBlock implements ItemWrapperMethods {

	public FWItemBlock(FWBlock block) {
		super(block);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		Item item = Game.natives().toNova(stack);
		WrapperEvent.FWItemInitCapabilities event = new WrapperEvent.FWItemInitCapabilities(item, new FWCapabilityProvider());
		return event.capabilityProvider.hasCapabilities() ? event.capabilityProvider : null;
	}

	@Override
	public ItemFactory getItemFactory() {
		return ((FWBlock) block).dummy.getItemFactory();
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean p_77624_4_) {
		ItemWrapperMethods.super.addInformation(itemStack, player, list, p_77624_4_);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return ItemWrapperMethods.super.onItemUse(player.getHeldItem(hand), player, world, pos.getX(), pos.getY(), pos.getZ(), side.ordinal(), hitX, hitY, hitZ);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		return ItemWrapperMethods.super.onItemRightClick(itemStack, world, player);
	}

	@Override
	public int getColorFromItemStack(ItemStack itemStack, int p_82790_2_) {
		return ItemWrapperMethods.super.getColorFromItemStack(itemStack, p_82790_2_);
	}
}
