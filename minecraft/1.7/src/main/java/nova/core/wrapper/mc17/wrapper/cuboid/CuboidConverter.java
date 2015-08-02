package nova.core.wrapper.mc17.wrapper.cuboid;

import net.minecraft.util.AxisAlignedBB;
import nova.core.nativewrapper.NativeConverter;
import nova.core.util.shape.Cuboid;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

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
		return new Cuboid(new Vector3D(aabb.minX, aabb.minY, aabb.minZ), new Vector3D(aabb.maxX, aabb.maxY, aabb.maxZ));
	}

	@Override
	public AxisAlignedBB toNative(Cuboid cuboid) {
		return AxisAlignedBB.getBoundingBox(cuboid.min.getX(), cuboid.min.getY(), cuboid.min.getZ(), cuboid.max.getX(), cuboid.max.getY(), cuboid.max.getZ());
	}
}
