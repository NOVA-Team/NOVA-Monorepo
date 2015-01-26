package nova.core.world;

import nova.core.block.Block;
import nova.core.util.Named;
import nova.core.util.Vector3i;

import java.util.Optional;

public abstract class World implements Named {
	public abstract Optional<Block> getBlock(Vector3i position);
}
