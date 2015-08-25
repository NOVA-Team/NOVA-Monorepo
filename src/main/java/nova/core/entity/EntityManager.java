package nova.core.entity;

import nova.core.util.Manager;
import nova.core.util.Registry;

import java.util.function.Function;

public class EntityManager extends Manager<Entity, EntityFactory> {

	private EntityManager(Registry<EntityFactory> registry) {
		super(registry);
	}

	/**
	 * Register a new item with custom constructor arguments.
	 * @param constructor The lambda expression to create a new constructor.
	 * @return Dummy item
	 */
	@Override
	public EntityFactory register(Function<Object[], Entity> constructor) {
		return register(new EntityFactory(constructor));
	}
}
