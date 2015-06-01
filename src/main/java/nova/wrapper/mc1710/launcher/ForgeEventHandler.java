package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.oredict.OreDictionary;
import nova.core.event.GlobalEvents;
import nova.core.game.Game;
import nova.core.item.ItemDictionary;
import nova.core.util.transform.vector.Vector3i;
import nova.wrapper.mc1710.wrapper.item.ItemConverter;

/**
 * @author Stan, Calclavia
 */
public class ForgeEventHandler {
	@SubscribeEvent
	public void onOreRegister(OreDictionary.OreRegisterEvent event) {
		ItemDictionary novaItemDictionary = Game.itemDictionary();

		String id = ItemConverter.instance().getNovaItem(event.Ore).getID();
		if (!novaItemDictionary.get(event.Name).contains(id)) {
			novaItemDictionary.add(event.Name, id);
		}
	}

	@SubscribeEvent
	public void playerInteractEvent(PlayerInteractEvent event) {
		GlobalEvents.PlayerInteractEvent evt = new GlobalEvents.PlayerInteractEvent(
			Game.natives().toNova(event.world),
			new Vector3i(event.x, event.y, event.z),
			Game.natives().toNova(event.entityPlayer),
			GlobalEvents.PlayerInteractEvent.Action.values()[event.action.ordinal()]
		);

		Game.events().playerInteract.publish(evt);

		event.useBlock = Event.Result.values()[evt.useBlock.ordinal()];
		event.useItem = Event.Result.values()[evt.useItem.ordinal()];
	}
}
