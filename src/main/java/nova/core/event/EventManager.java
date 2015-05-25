package nova.core.event;

import nova.core.block.Block;
import nova.core.util.transform.vector.Vector3i;
import nova.core.world.World;

/**
 * General event manager that handles basic events
 * @author Calclavia
 */
public class EventManager {
	/**
	 * Called when the server starts
	 */
	public EventBus<EmptyEvent> serverStarting = new EventBus<>();

	/**
	 * Called when the server stops
	 */
	public EventBus<EmptyEvent> serverStopping = new EventBus<>();

	/**
	 * Called when a block is changed (set block) in the world.
	 */
	public EventBus<BlockChangeEvent> blockChange = new EventBus<>();

	public static class BlockEvent extends CancelableEvent {
		//The world
		public final World world;
		//The position of the block
		public final Vector3i position;

		public BlockEvent(World world, Vector3i position) {
			this.world = world;
			this.position = position;
		}
	}

	/**
	 * Called when a block in the world changes.
	 */
	public static class BlockChangeEvent extends BlockEvent {

		//The block that was in this position previously
		public final Block oldBlock;
		//The block that was set to in this position
		public final Block newBlock;

		public BlockChangeEvent(World world, Vector3i position, Block oldBlock, Block newBlock) {
			super(world, position);
			this.newBlock = newBlock;
			this.oldBlock = oldBlock;
		}
	}

	public static class EmptyEvent {

	}
}
