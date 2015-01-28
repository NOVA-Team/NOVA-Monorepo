package nova.wrapper.mc1710.forward.util;

import net.minecraft.util.AxisAlignedBB;
import nova.core.util.transform.Cuboid;

/**
 * @author Calclavia
 */
public class CuboidForwardWrapper extends AxisAlignedBB {
	public CuboidForwardWrapper(Cuboid cuboid) {
		super(cuboid.min.x, cuboid.min.y, cuboid.min.z, cuboid.max.x, cuboid.max.y, cuboid.max.z);
	}
}
