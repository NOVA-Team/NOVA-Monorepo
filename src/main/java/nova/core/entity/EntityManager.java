package nova.core.entity;

import nova.core.util.Manager;
import nova.core.util.Registry;

import java.util.function.Supplier;

public class EntityManager extends Manager<Entity, EntityFactory> {

	private EntityManager(Registry<EntityFactory> registry) {
		super(registry);
	}

	/**
	 * Register a new entity type.
	 * @param constructor The lambda expression to create a new constructor.
	 * @return The entity factory
	 */
	@Override
	public EntityFactory register(Supplier<Entity> constructor) {
		return register(new EntityFactory(constructor));
	}
}
