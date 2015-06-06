package nova.wrapper.mc1710.wrapper.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import nova.internal.Game;
import nova.core.item.Item;
import nova.core.item.ItemDictionary;
import nova.core.util.Dictionary;
import nova.wrapper.mc1710.util.ReflectionUtil;

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
			for (String oreValue : novaItemDictionary.get(oredictEntry)) {
				OreDictionary.registerOre(oredictEntry, ItemConverter.instance().toNative(oreValue));
			}
		}

		for (String oredictEntry : OreDictionary.getOreNames()) {
			for (ItemStack oreValue : OreDictionary.getOres(oredictEntry)) {
				Item novaItem = ItemConverter.instance().getNovaItem(oreValue);
				String id = novaItem.getID();
				if (!novaItemDictionary.get(oredictEntry).contains(id))
					novaItemDictionary.add(oredictEntry, id);
			}
		}

		novaItemDictionary.whenEntryAdded(this::onEntryAdded);
		novaItemDictionary.whenEntryRemoved(this::onEntryRemoved);
	}

	private void onEntryAdded(Dictionary.AddEvent<String> event) {
		if (!OreDictionary.getOres(event.key).contains(event.value)) {
			OreDictionary.registerOre(event.key, ItemConverter.instance().toNative(event.value));
		}
	}

	private void onEntryRemoved(Dictionary.RemoveEvent<String> event) {
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
