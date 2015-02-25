package nova.core.event;

import nova.core.block.Block;
import nova.core.util.transform.Vector3i;
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
	 * Called when a block is changed (set block).
	 */
	public EventBus<BlockChangeEvent> blockChange = new EventBus<>();

	public static class BlockChangeEvent {
		//The world
		public final World world;
		//The position of the block
		public final Vector3i position;
		//The block that was in this position previously
		public final Block oldBlock;
		//The block that was set to in this position
		public final Block newBlock;

		public BlockChangeEvent(World world, Vector3i position, Block oldBlock, Block newBlock) {
			this.world = world;
			this.newBlock = newBlock;
			this.oldBlock = oldBlock;
			this.position = position;
		}
	}

	public static class EmptyEvent {

	}
}
