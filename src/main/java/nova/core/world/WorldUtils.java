package nova.core.world;

import nova.core.block.Block;
import nova.core.game.Game;
import nova.core.util.Registry;

public class WorldUtils {
	public static Registry<World> getWorldRegistry() {
		return Game.instance.getRegistry(World.class);
	}
}