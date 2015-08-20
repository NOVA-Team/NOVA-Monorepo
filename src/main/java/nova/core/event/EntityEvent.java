package nova.core.event;

import nova.core.entity.Entity;
import nova.core.event.bus.CancelableEvent;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * All events related to the entity
 * @author Calclavia
 */
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
}