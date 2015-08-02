package nova.wrapper.mc18.launcher;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import nova.internal.core.Game;

/**
 * Handles FML events and forwards them to NOVA.
 * @author Calclavia
 */
public class FMLEventHandler {

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
