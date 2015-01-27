package nova.core.block;

import nova.core.util.transform.Vector3i;

import java.util.Optional;

public interface BlockAccess {
	Optional<Block> getBlock(Vector3i position);
	boolean setBlock(Vector3i position, Block block);
	boolean removeBlock(Vector3i position);
}