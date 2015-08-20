package nova.core.event;

import nova.core.entity.Entity;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * All events relevant to the player actions.
 * @author Calclavia
 */
public abstract class PlayerEvent extends EntityEvent {
	public PlayerEvent(Entity entity) {
		super(entity);
	}

	/**
	 * Event triggered when a player joins the server.
	 */
	public static class Join extends PlayerEvent {
		public Join(Entity entity) {
			super(entity);
		}
	}

	/**
	 * Event triggered when a player leaves the server.
	 */
	public static class Leave extends PlayerEvent {
		public Leave(Entity entity) {
			super(entity);
		}
	}

	/**
	 * Event triggered when a player interacts with the world.
	 */
	public static class Interact extends PlayerEvent {
		public final World world;
		public final Vector3D position;
		public final Action action;
		public Result useBlock = Result.DEFAULT;
		public Result useItem = Result.DEFAULT;

		public Interact(World world, Vector3D position, Entity player, Action action) {
			super(player);
			this.world = world;
			this.position = position;
			this.action = action;
		}

		public enum Action {
			RIGHT_CLICK_AIR,
			RIGHT_CLICK_BLOCK,
			LEFT_CLICK_BLOCK
		}
	}
}
