package nova.wrapper.mc1710.backward.world;

import nova.core.block.Block;
import nova.core.util.transform.Vector3i;
import nova.core.world.World;
import nova.wrapper.mc1710.backward.block.BWBlock;
import nova.wrapper.mc1710.forward.block.BlockWrapper;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class BWWorld extends World {
	private final BWBlockAccess blockAccess;
	private final net.minecraft.world.World world;

	public BWWorld(net.minecraft.world.World world) {
		this.world = world;
		this.blockAccess = new BWBlockAccess(world);
	}

	@Override
	public Optional<Block> getBlock(Vector3i position) {
		return blockAccess.getBlock(position);
	}

	@Override
	public boolean setBlock(Vector3i position, Block block) {
		net.minecraft.block.Block mcBlock = net.minecraft.block.Block.getBlockFromName(block.getID());
		return world.setBlock(position.x, position.y, position.z, mcBlock);
	}

	@Override
	public boolean removeBlock(Vector3i position) {
		return world.setBlockToAir(position.x, position.y, position.z);
	}

	@Override
	public String getID() {
		return world.provider.getDimensionName();
	}
}
