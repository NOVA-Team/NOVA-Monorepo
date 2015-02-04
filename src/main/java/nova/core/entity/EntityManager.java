package nova.core.entity;

import nova.core.util.Registry;

import java.util.Optional;

public class EntityManager {
	public final Registry<Entity> registry;

	private EntityManager(Registry<Entity> registry) {
		this.registry = registry;
	}

	/**
	 * Returns entity by its name.
	 *
	 * @param name Name of entity to search for.
	 * @return {@link nova.core.entity.Entity} that was searched for·
	 */
	public Optional<Entity> getEntity(String name) {
		return registry.get(name);
	}
}
