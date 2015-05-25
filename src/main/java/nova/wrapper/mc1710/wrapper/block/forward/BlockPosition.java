package nova.wrapper.mc1710.wrapper.block.forward;

import net.minecraft.world.World;

/**
 * @author Stan Hebben
 */
public final class BlockPosition {
	private final World world;
	private final int x;
	private final int y;
	private final int z;

	public BlockPosition(net.minecraft.world.World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		BlockPosition that = (BlockPosition) o;

		if (x != that.x) {
			return false;
		}
		if (y != that.y) {
			return false;
		}
		if (z != that.z) {
			return false;
		}
		if (!world.equals(that.world)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = world.hashCode();
		result = 31 * result + x;
		result = 31 * result + y;
		result = 31 * result + z;
		return result;
	}
}
