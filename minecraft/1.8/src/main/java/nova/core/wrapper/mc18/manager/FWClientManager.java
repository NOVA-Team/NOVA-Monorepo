package nova.core.wrapper.mc18.manager;

import net.minecraft.client.Minecraft;
import nova.core.entity.Entity;
import nova.core.game.ClientManager;
import nova.core.wrapper.mc18.launcher.NovaMinecraft;
import nova.core.wrapper.mc18.wrapper.entity.BWEntity;

/**
 * @author Calclavia
 */
public class FWClientManager extends ClientManager {

	@Override
	public Entity getPlayer() {
		return new BWEntity(NovaMinecraft.proxy.getClientPlayer());
	}

	@Override
	public boolean isPaused() {
		return Minecraft.getMinecraft().isGamePaused();
	}
}
