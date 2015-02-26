package nova.core.entity;

import nova.core.util.ReflectionUtil;
import nova.core.util.Registry;

import java.util.Optional;
import java.util.function.Supplier;

public class EntityManager {
	public final Registry<EntityFactory> registry;

	private EntityManager(Registry<EntityFactory> registry) {
		this.registry = registry;
	}

	public EntityFactory register(Class<? extends Entity> item) {
		return register(() -> ReflectionUtil.newInstance(item));
	}

	/**
	 * Register a new item with custom constructor arguments.
	 * @param constructor The lambda expression to create a new constructor.
	 * @return Dummy item
	 */
	public EntityFactory register(Supplier<Entity> constructor) {
		return register(new EntityFactory(constructor));
	}

	public EntityFactory register(EntityFactory factory) {
		registry.register(factory);
		return factory;
	}

	/**
	 * Returns entity by its name.
	 * @param name Name of entity to search for.
	 * @return {@link nova.core.entity.Entity} that was searched for·
	 */
	public Optional<EntityFactory> getEntityFactory(String name) {
		return registry.get(name);
	}

	public Optional<Entity> getEntity(String name) {
		Optional<EntityFactory> entityFactory = getEntityFactory(name);

		if (entityFactory.isPresent()) {
			return Optional.of(entityFactory.get().getDummy());
		}

		return Optional.empty();
	}
}
