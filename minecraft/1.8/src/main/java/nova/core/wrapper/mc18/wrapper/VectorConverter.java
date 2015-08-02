package nova.core.wrapper.mc18.wrapper;

import net.minecraft.util.BlockPos;
import nova.core.nativewrapper.NativeConverter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class VectorConverter implements NativeConverter<Vector3D, BlockPos> {
	@Override
	public Class<Vector3D> getNovaSide() {
		return Vector3D.class;
	}

	@Override
	public Class<BlockPos> getNativeSide() {
		return BlockPos.class;
	}

	@Override
	public Vector3D toNova(BlockPos pos) {
		return new Vector3D(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public BlockPos toNative(Vector3D vec) {
		return new BlockPos(vec.getX(), vec.getY(), vec.getZ());
	}
}
