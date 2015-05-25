package nova.wrapper.mc1710.manager;

import net.minecraft.client.Minecraft;
import nova.core.game.ClientManager;
import nova.core.player.Player;
import nova.wrapper.mc1710.backward.entity.BWEntityPlayer;

/**
 * @author Calclavia
 */
public class MCClientManager extends ClientManager {

	@Override
	public Player getPlayer() {
		return new BWEntityPlayer(Minecraft.getMinecraft().thePlayer);
	}
}
