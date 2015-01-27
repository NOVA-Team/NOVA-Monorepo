package nova.core.block.components;

import nova.core.util.transform.Vector3i;
import nova.core.world.World;

public interface LightPassthrough {
	public boolean canPassLight(World world, Vector3i position);
}
