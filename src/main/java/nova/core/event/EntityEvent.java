package nova.core.event;

import nova.core.entity.Entity;
import nova.core.event.bus.CancelableEvent;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class EntityEvent extends CancelableEvent {
	public final Entity entity;

	public EntityEvent(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Event is triggered when an entity is added to the world.
	 */
	public static class Add extends EntityEvent {
		public Add(Entity entity) {
			super(entity);
		}
	}

	/**
	 * Event is triggered when an entity is removed in the world.
	 */
	public static class Remove extends EntityEvent {
		public Remove(Entity entity) {
			super(entity);
		}
	}

	/**
	 * Event triggered when a player interacts with the world.
	 */
	public static class PlayerInteract extends EntityEvent {
		public final World world;
		public final Vector3D position;
		public final Action action;
		public Result useBlock = Result.DEFAULT;
		public Result useItem = Result.DEFAULT;

		public PlayerInteract(World world, Vector3D position, Entity player, Action action) {
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