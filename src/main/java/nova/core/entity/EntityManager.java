package nova.core.entity;

import nova.core.util.Registry;

import java.util.Optional;

public class EntityManager {
	public final Registry<Entity> registry;


	private EntityManager(Registry<Entity> registry) {
		this.registry = registry;
	}

	public Optional<Entity> getEntity(String name) {
		return registry.get(name);
	}
}
