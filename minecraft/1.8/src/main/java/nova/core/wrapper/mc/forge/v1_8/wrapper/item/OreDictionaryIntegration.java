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

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import nova.core.event.DictionaryEvent;
import nova.core.item.Item;
import nova.core.item.ItemDictionary;
import nova.core.wrapper.mc.forge.v1_8.util.ReflectionUtil;
import nova.internal.core.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by Stan on 8/02/2015.
 */
public class OreDictionaryIntegration {
	public static final OreDictionaryIntegration instance = new OreDictionaryIntegration();
	private static final List<ArrayList<ItemStack>> OREDICT_CONTENTS = ReflectionUtil.getOreIdStacks();

	private OreDictionaryIntegration() {
	}

	public void registerOreDictionary() {
		ItemDictionary novaItemDictionary = Game.itemDictionary();

		novaItemDictionary.stream().forEach(entry -> {
			entry.getValue().stream()
				.map(ItemConverter.instance()::toNative)
				.filter(item -> !OreDictionary.getOres(entry.getKey()).contains(item))
				.forEach(item -> OreDictionary.registerOre(entry.getKey(), item));
			});

		Arrays.stream(OreDictionary.getOreNames()).forEach(key -> {
			OreDictionary.getOres(key).stream()
				.map(ItemConverter.instance()::getNovaItem)
				.filter(item -> !novaItemDictionary.get(key).contains(item))
				.forEach(item -> novaItemDictionary.add(key, item));
			});

		novaItemDictionary.whenEntryAdded(this::onNovaAdded);
		novaItemDictionary.whenEntryRemoved(this::onNovaRemoved);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void onNovaAdded(DictionaryEvent.Add<Item> event) {
		ItemStack nativeStack = ItemConverter.instance().toNative(event.value);
		if (!OreDictionary.getOres(event.key).stream().anyMatch(stack -> stack.isItemEqual(nativeStack))) {
			OreDictionary.registerOre(event.key, nativeStack);
		}
	}

	private void onNovaRemoved(DictionaryEvent.Remove<Item> event) {
		int id = OreDictionary.getOreID(event.key);
		ItemStack nativeStack = ItemConverter.instance().toNative(event.value);
		Optional<ItemStack> toRemove = OreDictionary.getOres(event.key).stream().filter(stack -> stack.isItemEqual(nativeStack)).findFirst();

		if (toRemove.isPresent()) {
			OREDICT_CONTENTS.get(id).remove(toRemove.get());
		}
	}

	@SubscribeEvent
	public void onForgeAdded(OreDictionary.OreRegisterEvent event) {
		Item novaItem = ItemConverter.instance().getNovaItem(event.Ore);
		ItemDictionary novaItemDictionary = Game.itemDictionary();
		if (!novaItemDictionary.get(event.Name).contains(novaItem)) {
			novaItemDictionary.add(event.Name, novaItem);
		}
	}
}
