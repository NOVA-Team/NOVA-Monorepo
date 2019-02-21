/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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

package nova.core.wrapper.mc.forge.v1_7_10.wrapper;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import nova.core.component.Category;
import nova.core.nativewrapper.NativeConverter;
import nova.core.wrapper.mc.forge.v1_7_10.util.ModCreativeTab;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.item.ItemConverter;
import nova.internal.core.Game;

import java.util.Arrays;

/**
 * Converts NOVA {@link Category Categories} to Minecraft {@link CreativeTabs} and back.
 *
 * @author ExE Boss
 */
public class CategoryConverter implements NativeConverter<Category, CreativeTabs> {

	public static CategoryConverter instance() {
		return Game.natives().getNative(Category.class, CreativeTabs.class);
	}

	@Override
	public Class<Category> getNovaSide() {
		return Category.class;
	}

	@Override
	public Class<CreativeTabs> getNativeSide() {
		return CreativeTabs.class;
	}

	@Override
	public Category toNova(CreativeTabs creativeTab) {
		switch (creativeTab.getTabLabel()) {
			case "buildingBlocks":
				return Category.BUILDING_BLOCKS;
			case "decorations":
				return Category.DECORATIONS;
			case "redstone":
				return Category.TECHNOLOGY;
			case "transportation":
				return Category.TRANSPORTATION;
			case "food":
				return Category.FOOD;
			case "tools":
				return Category.TOOLS;
			case "combat":
				return Category.COMBAT;
			case "brewing":
				return Category.ALCHEMY;
			case "materials":
				return Category.MATERIALS;
			case "misc":
				return Category.MISCELLANEOUS;
			default:
				return new Category(creativeTab.getTabLabel(), ItemConverter.instance().toNova(creativeTab.getIconItemStack()));
		}
	}

	@Override
	public CreativeTabs toNative(Category category) {
		return toNative(category, new ItemStack((Item) null));
	}

	public CreativeTabs toNative(Category category, Block block) {
		return toNative(category, new ItemStack(block));
	}

	public CreativeTabs toNative(Category category, Item item) {
		return toNative(category, new ItemStack(item));
	}

	public CreativeTabs toNative(Category category, ItemStack item) {
		if (category.name.startsWith("nova:")) {
			switch (category.name) {
				case Category.BUILDING_BLOCKS_NAME:
					return CreativeTabs.tabBlock;
				case Category.DECORATIONS_NAME:
					return CreativeTabs.tabDecorations;
				case Category.TECHNOLOGY_NAME:
					return CreativeTabs.tabRedstone;
				case Category.TRANSPORTATION_NAME:
					return CreativeTabs.tabTransport;
				case Category.FOOD_NAME:
					return CreativeTabs.tabFood;
				case Category.TOOLS_NAME:
					return CreativeTabs.tabTools;
				case Category.COMBAT_NAME:
					return CreativeTabs.tabCombat;
				case Category.ALCHEMY_NAME:
					return CreativeTabs.tabBrewing;
				case Category.MATERIALS_NAME:
					return CreativeTabs.tabMaterials;
				case Category.MISCELLANEOUS_NAME:
					return CreativeTabs.tabMisc;
			}
		}
		return Arrays.stream(CreativeTabs.creativeTabArray)
			.filter(tab -> tab.getTabLabel().equals(category.name))
			.findFirst()
			.orElseGet(() -> new ModCreativeTab(category.name, category.item.map(ItemConverter.instance()::toNative).orElse(item)));
	}
}
