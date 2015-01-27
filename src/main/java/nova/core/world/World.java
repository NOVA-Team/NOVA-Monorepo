package nova.core.world;

import nova.core.block.Block;
import nova.core.util.Identifiable;
import nova.core.util.transform.Vector3i;

import java.util.Optional;

public abstract class World implements Identifiable {
	public abstract Optional<Block> getBlock(Vector3i position);
}
