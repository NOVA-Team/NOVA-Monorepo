package nova.core.entity;

import nova.core.util.Factory;
import nova.core.util.Manager;
import nova.core.util.ReflectionUtil;
import nova.core.util.Registry;

import java.util.Optional;
import java.util.function.Function;

public class EntityManager extends Manager<Entity>{

	private EntityManager(Registry<Factory<Entity>> registry) {
		super(registry);
	}

}
