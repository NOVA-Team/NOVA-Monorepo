package nova.core.block;

import nova.core.util.Vector3i;
import nova.core.world.World;

public interface LightEmitter {
	public float getEmittedLightLevel(World world, Vector3i position);
}
