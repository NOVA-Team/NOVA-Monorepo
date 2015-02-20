package nova.core.event;

import nova.core.block.Block;
import nova.core.util.transform.Vector3i;

/**
 * General event manager that handles basic events
 *
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
		//The block that was changed
		public final Block block;
		//The position of the block
		public final Vector3i position;

		public BlockChangeEvent(Block block, Vector3i position) {
			this.block = block;
			this.position = position;
		}
	}

	public static class EmptyEvent {

	}
}
