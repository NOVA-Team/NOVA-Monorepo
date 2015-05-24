package nova.core.entity;

import nova.core.block.components.Stateful;
import nova.core.game.Game;
import nova.core.util.Identifiable;
import nova.core.util.transform.Transform3d;
import nova.core.util.transform.vector.Vector3d;
import nova.core.world.Positioned;
import nova.core.world.component.Component;
import nova.core.world.component.ComponentProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * An entity is an object in the world that has a position.
 */
public abstract class Entity extends Positioned<EntityWrapper, Vector3d> implements Identifiable, Stateful, EntityWrapper, ComponentProvider {

	/**
	 * The default transform component.
	 */
	public final Transform3d transform = Game.instance.componentManager.make(Transform3d.class, this);

	private Set<Component> components = new HashSet<>();

	public Entity() {
		add(transform);
	}

	@Override
	public Set<Component> components() {
		return components;
	}
}
