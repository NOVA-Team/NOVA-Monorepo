package nova.wrapper.mc1710.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import nova.core.game.Game;
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
		nova.core.item.OreDictionary novaOreDictionary = Game.instance.get().oreDictionary;

		for (String oredictEntry : novaOreDictionary.keys()) {
			for (String oreValue : novaOreDictionary.get(oredictEntry)) {
				OreDictionary.registerOre(oredictEntry, ItemWrapperRegistry.instance.getMCItemStack(oreValue));
			}
		}

		for (String oredictEntry : OreDictionary.getOreNames()) {
			for (ItemStack oreValue : OreDictionary.getOres(oredictEntry)) {
				String id = ItemWrapperRegistry.instance.getNovaItem(oreValue).getID();
				if (!novaOreDictionary.get(oredictEntry).contains(id))
					novaOreDictionary.add(oredictEntry, id);
			}
		}

		novaOreDictionary.whenEntryAdded(this::onEntryAdded);
		novaOreDictionary.whenEntryRemoved(this::onEntryRemoved);
	}

	private void onEntryAdded(Dictionary.AddEvent<String> event) {
		if (!OreDictionary.getOres(event.key).contains(event.value))
			OreDictionary.registerOre(event.key, ItemWrapperRegistry.instance.getMCItemStack(event.value));
	}

	private void onEntryRemoved(Dictionary.RemoveEvent<String> event) {
		int id = OreDictionary.getOreID(event.key);
		ItemStack itemStack = ItemWrapperRegistry.instance.getMCItemStack(event.value);
		ItemStack toRemove = null;
		for (ItemStack oreDictItemStack : OreDictionary.getOres(event.key)) {
			if (oreDictItemStack.getItem() == itemStack.getItem() && toRemove.getItemDamage() == oreDictItemStack.getItemDamage())
				toRemove = oreDictItemStack;
		}

		if (toRemove != null)
			OREDICT_CONTENTS.get(id).remove(toRemove);
	}
}
