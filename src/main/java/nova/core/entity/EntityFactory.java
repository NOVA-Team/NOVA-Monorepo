package nova.core.entity;

import nova.core.util.Factory;

import java.util.function.Supplier;

/**
 * Factory to create entities of specific type
 * @author Calclavia
 */
public class EntityFactory extends Factory<Entity> {
	public EntityFactory(Supplier<Entity> constructor) {
		super(constructor);
	}
}
