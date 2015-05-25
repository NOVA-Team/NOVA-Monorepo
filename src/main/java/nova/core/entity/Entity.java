package nova.core.entity;

import nova.core.block.Stateful;
import nova.core.component.transform.EntityTransform;
import nova.core.game.Game;
import nova.core.util.Identifiable;
import nova.core.util.WrapperProvider;

/**
 * An entity is an object in the world that has a position.
 */
public abstract class Entity extends WrapperProvider<EntityWrapper> implements Identifiable, Stateful {

	/**
	 * The default transform component.
	 */
	//TODO: Make this a method. We don't even need a reference.
	public final EntityTransform transform = Game.instance.componentManager.make(EntityTransform.class, this);

	public Entity() {
		add(transform);
	}
}
