package nova.wrapper.mc18.wrapper.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import nova.core.item.Item;
import nova.core.item.ItemDictionary;
import nova.core.util.Dictionary;
import nova.internal.core.Game;
import nova.wrapper.mc18.util.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;

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

		for (String oredictEntry : novaItemDictionary.keys()) {
			for (Item oreValue : novaItemDictionary.get(oredictEntry)) {
				OreDictionary.registerOre(oredictEntry, ItemConverter.instance().toNative(oreValue));
			}
		}

		for (String oredictEntry : OreDictionary.getOreNames()) {
			for (ItemStack oreValue : OreDictionary.getOres(oredictEntry)) {
				Item novaItem = ItemConverter.instance().getNovaItem(oreValue);
				if (!novaItemDictionary.get(oredictEntry).contains(novaItem)) {
					novaItemDictionary.add(oredictEntry, novaItem);
				}
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
			if (oreDictItemStack.getItem() == itemStack.getItem() && toRemove.getItemDamage() == oreDictItemStack.getItemDamage()) {
				toRemove = oreDictItemStack;
			}
		}

		if (toRemove != null) {
			OREDICT_CONTENTS.get(id).remove(toRemove);
		}
	}
}
