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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import nova.core.item.ItemFactory;
import nova.core.wrapper.mc.forge.v1_8.wrapper.block.forward.FWBlock;

import java.util.List;

/**
 * @author Calclavia
 */
public class FWItemBlock extends net.minecraft.item.ItemBlock implements ItemWrapperMethods {

	public FWItemBlock(net.minecraft.block.Block block) {
		super(block);
	}

	@Override
	public ItemFactory getItemFactory() {
		return ((FWBlock) block).dummy.getItemFactory();
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addInformation(ItemStack itemStack, EntityPlayer player, List tooltip, boolean advanced) {
		ItemWrapperMethods.super.addInformation(itemStack, player, tooltip, advanced);
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		return ItemWrapperMethods.super.onItemUse(itemStack, player, world, pos.getX(), pos.getY(), pos.getZ(), side.ordinal(), hitX, hitY, hitZ);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		return ItemWrapperMethods.super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}

	@Override
	public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
		return ItemWrapperMethods.super.getColorFromItemStack(p_82790_1_, p_82790_2_);
	}

	@Override
	public String getUnlocalizedName() {
		return getItemFactory().getUnlocalizedName();
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return ItemConverter.instance().toNova(stack).getUnlocalizedName();
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return ItemConverter.instance().toNova(stack).getLocalizedName();
	}
}
