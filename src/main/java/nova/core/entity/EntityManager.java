package nova.core.entity;

import com.google.inject.Inject;
import nova.core.block.Block;
import nova.core.util.Registry;

import java.util.Optional;

public class EntityManager {
	public final Registry<Entity> registry;

	@Inject
	private EntityManager(Registry<Entity> registry) {
		this.registry = registry;
	}

	public Optional<Entity> getEntity(String name) {
		return registry.get(name);
	}
}
