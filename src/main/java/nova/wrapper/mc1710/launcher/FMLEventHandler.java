package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import nova.internal.tick.UpdateTicker;

/**
 * Handles FML events and forwards them to NOVA.
 *
 * @author Calclavia
 */
public class FMLEventHandler {

	@SubscribeEvent
	public void tickEnd(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			UpdateTicker.SynchronizedTicker.instance.update();
		}
	}
}
