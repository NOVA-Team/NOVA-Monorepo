package nova.core.event;

import nova.core.block.Block;
import nova.core.util.transform.vector.Vector3i;
import nova.core.world.World;

import java.util.Optional;

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

	/**
	 * Called when a block's neighbor change.
	 */
	public EventBus<BlockNeighborChangeEvent> blockNeighborChange = new EventBus<>();

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

	@CancelableEvent.Cancelable
	public static class BlockNeighborChangeEvent extends BlockEvent {
		//The neighbor block
		public final Optional<Block> neighbor;

		public BlockNeighborChangeEvent(World world, Vector3i position, Optional<Block> neighbor) {
			super(world, position);
			this.neighbor = neighbor;
		}
	}

	public static class EmptyEvent {

	}
}
