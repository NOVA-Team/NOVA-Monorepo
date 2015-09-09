package nova.core.event;

import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.event.bus.CancelableEvent;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public abstract class BlockEvent extends CancelableEvent {
	//The world
	public final World world;
	//The position of the block
	public final Vector3D position;

	public BlockEvent(World world, Vector3D position) {
		this.world = world;
		this.position = position;
	}

	public static class Register extends CancelableEvent {
		public BlockFactory blockFactory;

		public Register(BlockFactory blockFactory) {
			this.blockFactory = blockFactory;
		}
	}

	/**
	 * Called when a block in the world changes.
	 */
	public static class Change extends BlockEvent {

		//The block that was in this position previously
		public final Block oldBlock;
		//The block that was set to in this position
		public final Block newBlock;

		public Change(World world, Vector3D position, Block oldBlock, Block newBlock) {
			super(world, position);
			this.newBlock = newBlock;
			this.oldBlock = oldBlock;
		}
	}
}
