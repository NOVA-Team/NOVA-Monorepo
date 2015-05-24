package nova.core.entity;

import nova.core.block.components.Stateful;
import nova.core.game.Game;
import nova.core.util.Identifiable;
import nova.core.util.transform.Transform3d;
import nova.core.util.transform.vector.Vector3d;
import nova.core.world.Positioned;

/**
 * An entity is an object in the world that has a position.
 */
public abstract class Entity extends Positioned<EntityWrapper, Vector3d> implements Identifiable, Stateful, EntityWrapper {

	/**
	 * The default transform component.
	 */
	public final Transform3d transform = Game.instance.componentManager.make(Transform3d.class, this);

	public Entity() {
		add(transform);
	}

}
