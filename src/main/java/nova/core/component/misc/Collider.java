package nova.core.component.misc;

/**
 * @author Calclavia
 */

import nova.core.component.Component;
import nova.core.entity.Entity;
import nova.core.util.transform.shape.Cuboid;
import nova.core.util.transform.vector.Vector3i;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Collider extends Component {
	/**
	 * Called when an entity collides with this object. More specifically, when
	 * the entity's block bounds coincide with the bounds of this object.
	 * @param entity colliding entity
	 */
	public Consumer<Entity> onEntityCollide = (entity) -> {
	};

	public List<Cuboid> collisionBoxes = Collections.singletonList(new Cuboid(new Vector3i(0, 0, 0), new Vector3i(1, 1, 1)));

	public Collider setEntityCollide(Consumer<Entity> onEntityCollide) {
		this.onEntityCollide = onEntityCollide;
		return this;
	}

	public Collider setCollisionBoxes(Cuboid... boxes) {
		collisionBoxes = Arrays.asList(boxes);
		return this;
	}
}
