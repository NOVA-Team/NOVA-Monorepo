package nova.wrapper.mc18.launcher;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import nova.core.event.GlobalEvents;
import nova.core.item.ItemDictionary;
import nova.internal.core.Game;
import nova.wrapper.mc18.wrapper.item.ItemConverter;
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

		String id = ItemConverter.instance().getNovaItem(event.Ore).getID();
		if (!novaItemDictionary.get(event.Name).contains(id)) {
			novaItemDictionary.add(event.Name, id);
		}
	}

	@SubscribeEvent
	public void playerInteractEvent(PlayerInteractEvent event) {
		if (event.world != null && event.pos != null) {
			GlobalEvents.PlayerInteractEvent evt = new GlobalEvents.PlayerInteractEvent(
				Game.natives().toNova(event.world),
				new Vector3D(event.pos.getX(), event.pos.getY(), event.pos.getZ()),
				Game.natives().toNova(event.entityPlayer),
				GlobalEvents.PlayerInteractEvent.Action.values()[event.action.ordinal()]
			);

			Game.events().publish(evt);

			event.useBlock = Event.Result.values()[evt.useBlock.ordinal()];
			event.useItem = Event.Result.values()[evt.useItem.ordinal()];
			event.setCanceled(evt.isCanceled());
		}
	}
}
