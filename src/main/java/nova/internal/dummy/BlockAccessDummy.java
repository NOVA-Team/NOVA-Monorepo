package nova.internal.dummy;

import nova.core.block.Block;
import nova.core.block.BlockAccess;
import nova.core.util.transform.Vector3i;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class BlockAccessDummy implements BlockAccess {
	public static final BlockAccessDummy INSTANCE = new BlockAccessDummy();

	private BlockAccessDummy() {
	}

	@Override
	public Optional<Block> getBlock(Vector3i position) {
		return Optional.empty();
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