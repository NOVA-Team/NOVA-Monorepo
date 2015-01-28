package nova.wrapper.mc1710.backward.world;

import net.minecraft.world.IBlockAccess;
import nova.core.block.Block;
import nova.core.block.BlockAccess;
import nova.core.util.transform.Vector3i;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class BlockAccessWrapper implements BlockAccess {
	private final net.minecraft.world.IBlockAccess access;

	public BlockAccessWrapper(IBlockAccess access) {
		this.access = access;
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
}
