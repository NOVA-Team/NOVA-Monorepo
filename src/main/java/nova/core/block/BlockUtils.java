package nova.core.block;

import nova.core.game.Game;
import nova.core.util.Registry;

import java.util.Optional;

public class BlockUtils {
	private BlockUtils() {

	}

	public static Optional<Block> getBlock(String name) {
		return getBlockRegistry().get(name);
	}

	public static Registry<Block> getBlockRegistry() {
		return Game.instance.getRegistry(Block.class);
	}
}
