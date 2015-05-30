package nova.wrapper.mc1710.wrapper.cuboid;

import net.minecraft.util.AxisAlignedBB;
import nova.core.nativewrapper.NativeConverter;
import nova.core.util.transform.shape.Cuboid;
import nova.core.util.transform.vector.Vector3d;

/**
 * @author Calclavia
 */
public class CuboidConverter implements NativeConverter<Cuboid, AxisAlignedBB> {
	@Override
	public Class<Cuboid> getNovaSide() {
		return Cuboid.class;
	}

	@Override
	public Class<AxisAlignedBB> getNativeSide() {
		return AxisAlignedBB.class;
	}

	@Override
	public Cuboid toNova(AxisAlignedBB aabb) {
		return new Cuboid(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
	}

	@Override
	public AxisAlignedBB toNative(Cuboid cuboid) {
		return AxisAlignedBB.getBoundingBox(cuboid.min.x, cuboid.min.y, cuboid.min.z, cuboid.max.x, cuboid.max.y, cuboid.max.z);
	}
}
