package nova.core.wrapper.mc18.launcher;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import nova.core.event.PlayerEvent;
import nova.internal.core.Game;

/**
 * Handles FML events and forwards them to NOVA.
 * @author Calclavia
 */
public class FMLEventHandler {
	@SubscribeEvent
	public void playerJoin(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent evt) {
		Game.events().publish(new PlayerEvent.Join(Game.natives().toNova(evt.player)));
	}

	@SubscribeEvent
	public void playerLeave(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent evt) {
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
