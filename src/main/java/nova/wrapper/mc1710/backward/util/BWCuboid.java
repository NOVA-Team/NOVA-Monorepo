package nova.wrapper.mc1710.backward.util;

import net.minecraft.util.AxisAlignedBB;
import nova.core.util.transform.shape.Cuboid;
import nova.core.util.transform.vector.Vector3d;

/**
 * @author Calclavia
 */
public class BWCuboid extends Cuboid {
	public BWCuboid(AxisAlignedBB aabb) {
		super(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
	}
}
