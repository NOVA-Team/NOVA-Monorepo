package nova.core.wrapper.mc17.launcher;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import nova.core.event.PlayerEvent;
import nova.internal.core.Game;

/**
 * Handles FML events and forwards them to NOVA.
 * @author Calclavia
 */
public class FMLEventHandler {
	@SubscribeEvent
	public void playerJoin(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent evt) {
		Game.events().publish(new PlayerEvent.Join(Game.natives().toNova(evt.player)));
	}

	@SubscribeEvent
	public void playerLeave(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent evt) {
		Game.events().publish(new PlayerEvent.Leave(Game.natives().toNova(evt.player)));
	}

	@SubscribeEvent
	public void tickEnd(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			Game.syncTicker().update();
		}
	}

	@SubscribeEvent
	public void tickEnd(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			Game.syncTicker().update();
		}
	}
}
