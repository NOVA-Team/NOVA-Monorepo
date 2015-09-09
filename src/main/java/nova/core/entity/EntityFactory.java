package nova.core.entity;

import nova.core.util.Factory;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Factory to create entities of specific type
 * @author Calclavia
 */
public class EntityFactory extends Factory<EntityFactory, Entity> {
	public EntityFactory(Supplier<Entity> constructor) {
		super(constructor);
	}

	public EntityFactory(Supplier<Entity> constructor, Function<Entity, Entity> processor) {
		super(constructor, processor);
	}

	@Override
	public EntityFactory selfConstructor(Supplier<Entity> constructor, Function<Entity, Entity> processor) {
		return new EntityFactory(constructor, processor);
	}
}
