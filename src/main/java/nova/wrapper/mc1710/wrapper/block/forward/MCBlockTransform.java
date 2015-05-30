package nova.wrapper.mc1710.wrapper.block.forward;

import nova.core.block.Block;
import nova.core.component.transform.BlockTransform;
import nova.core.util.transform.vector.Vector3i;
import nova.core.world.World;

/**
 * @author Calclavia
 */
public class MCBlockTransform extends BlockTransform {

	public final Block block;
	public final World world;
	public final Vector3i position;

	public MCBlockTransform(Block block, World world, Vector3i position) {
		this.block = block;
		this.world = world;
		this.position = position;
	}

	@Override
	public Vector3i position() {
		return position;
	}

	@Override
	public World world() {
		return world;
	}

	@Override
	public void setWorld(World world) {
		this.world.removeBlock(position);
		world.setBlock(position, block.factory());
	}

	@Override
	public void setPosition(Vector3i position) {
		world.removeBlock(position);
		world.setBlock(position, block.factory());
	}
}
