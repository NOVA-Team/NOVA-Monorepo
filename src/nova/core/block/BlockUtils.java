package nova.core.block;

import nova.core.game.Game;
import nova.core.util.Registry;

import java.util.Optional;

public class BlockUtils {
	private BlockUtils() {

	}

	public static Optional<Block> getBlock(String name) {
		Registry<Block> r = getBlockRegistry();
		return r.contains(name) ? Optional.of(r.get(name)) : Optional.empty();
	}

	public static Registry<Block> getBlockRegistry() {
		return (Registry<Block>) Game.instance.getRegistry(Block.class);
	}
}
