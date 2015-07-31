package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.oredict.OreDictionary;
import nova.core.event.GlobalEvents;
import nova.core.item.Item;
import nova.core.item.ItemDictionary;
import nova.internal.core.Game;
import nova.wrapper.mc1710.wrapper.item.ItemConverter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Stan, Calclavia
 */
public class ForgeEventHandler {
	@SubscribeEvent
	public void worldUnload(WorldEvent.Load evt) {
		Game.events().publish(new GlobalEvents.WorldEvent.Load(Game.natives().toNova(evt.world)));
	}

	@SubscribeEvent
	public void worldLoad(WorldEvent.Unload evt) {
		Game.events().publish(new GlobalEvents.WorldEvent.Unload(Game.natives().toNova(evt.world)));
	}

	@SubscribeEvent
	public void onOreRegister(OreDictionary.OreRegisterEvent event) {
		ItemDictionary novaItemDictionary = Game.itemDictionary();

		Item item = ItemConverter.instance().getNovaItem(event.Ore);
		if (!novaItemDictionary.get(event.Name).contains(item)) {
			novaItemDictionary.add(event.Name, item);
		}
	}

	@SubscribeEvent
	public void playerInteractEvent(PlayerInteractEvent event) {
		GlobalEvents.PlayerInteractEvent evt = new GlobalEvents.PlayerInteractEvent(
			Game.natives().toNova(event.world),
			new Vector3D(event.x, event.y, event.z),
			Game.natives().toNova(event.entityPlayer),
			GlobalEvents.PlayerInteractEvent.Action.values()[event.action.ordinal()]
		);

		Game.events().publish(evt);

		event.useBlock = Event.Result.values()[evt.useBlock.ordinal()];
		event.useItem = Event.Result.values()[evt.useItem.ordinal()];
		event.setCanceled(evt.isCanceled());
	}
}
