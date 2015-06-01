package nova.core.event;

import nova.core.block.Block;
import nova.core.entity.Entity;
import nova.core.util.transform.vector.Vector3i;
import nova.core.world.World;

/**
 * General event manager that handles basic events
 * @author Calclavia
 */
public class GlobalEvents {
	/**
	 * Called when the server starts
	 */
	public EventBus<Event> serverStarting = new EventBus<>();

	/**
	 * Called when the server stops
	 */
	public EventBus<Event> serverStopping = new EventBus<>();

	/**
	 * Called when a block is changed (set block) in the world.
	 */
	public EventBus<BlockChangeEvent> blockChange = new EventBus<>();

	/**
	 * Called when a player interacts
	 */
	public EventBus<PlayerInteractEvent> playerInteract = new EventBus<>();

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

	@CancelableEvent.Cancelable
	public static class PlayerInteractEvent extends CancelableEvent {
		public final World world;
		public final Vector3i position;
		public final Entity player;
		public final Action action;
		public Result useBlock = Result.DEFAULT;
		public Result useItem = Result.DEFAULT;

		public PlayerInteractEvent(World world, Vector3i position, Entity player, Action action) {
			this.world = world;
			this.position = position;
			this.player = player;
			this.action = action;
		}

		public enum Action {
			RIGHT_CLICK_AIR,
			RIGHT_CLICK_BLOCK,
			LEFT_CLICK_BLOCK
		}
	}
}
