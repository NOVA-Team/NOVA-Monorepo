package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import nova.core.event.EventManager;
import nova.internal.tick.UpdateTicker;

/**
 * Handles FML events and forwards them to NOVA.
 * @author Calclavia
 */
public class FMLEventHandler {

	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event) {
		EventManager.instance.serverStarting.publish(null);
	}

	@SubscribeEvent
	public void serverStopping(FMLServerStoppingEvent event) {
		EventManager.instance.serverStopping.publish(null);
	}

	@SubscribeEvent
	public void tickEnd(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			UpdateTicker.SynchronizedTicker.instance.update();
		}
	}
}
