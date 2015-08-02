package nova.wrapper.mc18.wrapper.block.forward;

import nova.core.block.Block;
import nova.core.component.transform.BlockTransform;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Calclavia
 */
public class MCBlockTransform extends BlockTransform {

	public final Block block;
	public final World world;
	public final Vector3D position;

	public MCBlockTransform(Block block, World world, Vector3D position) {
		this.block = block;
		this.world = world;
		this.position = position;
	}

	@Override
	public Vector3D position() {
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
	public void setPosition(Vector3D position) {
		world.removeBlock(position);
		world.setBlock(position, block.factory());
	}
}
