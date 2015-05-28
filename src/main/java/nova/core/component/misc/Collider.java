package nova.core.component.misc;

/**
 * @author Calclavia
 */

import nova.core.component.Component;
import nova.core.entity.Entity;
import nova.core.event.EventBus;
import nova.core.event.EventListener;
import nova.core.util.transform.shape.Cuboid;
import nova.core.util.transform.vector.Vector3i;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class Collider extends Component {
	/**
	 * Called when an entity collides with this object. More specifically, when
	 * the entity's block bounds coincide with the bounds of this object.
	 * Entity - the colliding entity
	 */
	public EventBus<CollideEvent> collideEvent = new EventBus<>();

	/**
	 * A general cuboid that represents the bounds of this object.
	 */
	public Supplier<Cuboid> boundingBox = () -> new Cuboid(new Vector3i(0, 0, 0), new Vector3i(1, 1, 1));

	/**
	 * The boxes that provide occlusion to the specific block.
	 */
	public Function<Optional<Entity>, Set<Cuboid>> occlusionBoxes = opEnt -> Collections.singleton(boundingBox.get());

	public Function<Optional<Entity>, Set<Cuboid>> selectionBoxes = opEnt -> Collections.singleton(boundingBox.get());

	/**
	 * Called to check if the block is a cube.
	 * Returns true if this block is a cube.
	 */
	public Supplier<Boolean> isCube = () -> true;

	/**
	 * Called to check if the block is an opaque cube.
	 * Returns true if this block is a cube that is opaque.
	 */
	public Supplier<Boolean> isOpaqueCube = isCube;

	public Collider setBoundingBox(Cuboid boundingBox) {
		return setBoundingBox(() -> boundingBox);
	}

	public Collider setBoundingBox(Supplier<Cuboid> boundingBox) {
		this.boundingBox = boundingBox;
		return this;
	}

	public Collider onCollide(EventListener<CollideEvent> listener) {
		this.collideEvent.add(listener);
		return this;
	}

	public Collider setOcclusionBoxes(Function<Optional<Entity>, Set<Cuboid>> occlusionBoxes) {
		this.occlusionBoxes = occlusionBoxes;
		return this;
	}

	public Collider isCube(boolean is) {
		isCube = () -> is;
		return this;
	}

	public Collider isOpaqueCube(boolean is) {
		isOpaqueCube = () -> is;
		return this;
	}

	public static class CollideEvent {
		public final Entity entity;

		public CollideEvent(Entity entity) {
			this.entity = entity;
		}
	}
}
