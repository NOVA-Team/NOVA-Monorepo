package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import nova.core.game.Game;
import nova.core.item.ItemDictionary;
import nova.wrapper.mc1710.item.ItemWrapperRegistry;

/**
 * Created by Stan on 8/02/2015.
 */
public class ForgeEventHandler {
	@SubscribeEvent
	public void onOreRegister(OreDictionary.OreRegisterEvent event) {
		ItemDictionary novaItemDictionary = Game.instance.get().itemDictionary;

		String id = ItemWrapperRegistry.instance.getNovaItem(event.Ore).getID();
		if (!novaItemDictionary.get(event.Name).contains(id))
			novaItemDictionary.add(event.Name, id);
	}
}
