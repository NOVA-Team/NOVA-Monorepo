package nova.wrapper.mc1710.backward.world;

import nova.core.block.Block;
import nova.core.util.transform.Vector3i;
import nova.core.world.World;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class WorldWrapper extends World {

	private final net.minecraft.world.World world;

	public WorldWrapper(net.minecraft.world.World world) {
		this.world = world;
	}

	@Override
	public Optional<Block> getBlock(Vector3i position) {
		return null;
	}

	@Override
	public boolean setBlock(Vector3i position, Block block) {
		return false;
	}

	@Override
	public boolean removeBlock(Vector3i position) {
		return false;
	}

	@Override
	public String getID() {
		return null;
	}
}
