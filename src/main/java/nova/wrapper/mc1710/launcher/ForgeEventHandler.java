package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import nova.core.game.Game;
import nova.wrapper.mc1710.item.ItemWrapperRegistry;

/**
 * Created by Stan on 8/02/2015.
 */
public class ForgeEventHandler {
	@SubscribeEvent
	public void onOreRegister(OreDictionary.OreRegisterEvent event) {
		nova.core.item.OreDictionary novaOreDictionary = Game.instance.get().oreDictionary;

		String id = ItemWrapperRegistry.instance.getNovaItem(event.Ore).getID();
		if (!novaOreDictionary.get(event.Name).contains(id))
			novaOreDictionary.add(event.Name, id);
	}
}
