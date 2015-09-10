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

package nova.core.wrapper.mc17.wrapper.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import nova.core.item.Item;
import nova.core.item.ItemDictionary;
import nova.core.util.Dictionary;
import nova.core.wrapper.mc17.util.ReflectionUtil;
import nova.internal.core.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stan on 8/02/2015.
 */
public class OreDictionaryIntegration {
	public static final OreDictionaryIntegration instance = new OreDictionaryIntegration();
	private static final List<ArrayList<ItemStack>> OREDICT_CONTENTS = ReflectionUtil.getOreIdStacks();

	private OreDictionaryIntegration() {}

	public void registerOreDictionary() {
		ItemDictionary novaItemDictionary = Game.itemDictionary();

		for (String oredictEntry : novaItemDictionary.keys()) {
			for (Item oreValue : novaItemDictionary.get(oredictEntry)) {
				OreDictionary.registerOre(oredictEntry, ItemConverter.instance().toNative(oreValue));
			}
		}

		for (String oredictEntry : OreDictionary.getOreNames()) {
			for (ItemStack oreValue : OreDictionary.getOres(oredictEntry)) {
				Item novaItem = ItemConverter.instance().getNovaItem(oreValue);
				if (!novaItemDictionary.get(oredictEntry).contains(novaItem))
					novaItemDictionary.add(oredictEntry, novaItem);
			}
		}

		novaItemDictionary.whenEntryAdded(this::onEntryAdded);
		novaItemDictionary.whenEntryRemoved(this::onEntryRemoved);
	}

	private void onEntryAdded(Dictionary.AddEvent<Item> event) {
		if (!OreDictionary.getOres(event.key).contains(event.value)) {
			OreDictionary.registerOre(event.key, ItemConverter.instance().toNative(event.value));
		}
	}

	private void onEntryRemoved(Dictionary.RemoveEvent<Item> event) {
		int id = OreDictionary.getOreID(event.key);
		ItemStack itemStack = ItemConverter.instance().toNative(event.value);
		ItemStack toRemove = null;
		for (ItemStack oreDictItemStack : OreDictionary.getOres(event.key)) {
			if (oreDictItemStack.getItem() == itemStack.getItem() && toRemove.getItemDamage() == oreDictItemStack.getItemDamage())
				toRemove = oreDictItemStack;
		}

		if (toRemove != null)
			OREDICT_CONTENTS.get(id).remove(toRemove);
	}
}
