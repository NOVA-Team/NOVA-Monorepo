package nova.core.entity;

import nova.core.util.Factory;
import nova.core.util.exception.NovaException;

import java.lang.reflect.Field;
import java.util.function.Supplier;

/**
 * @author Calclavia
 */
public class EntityFactory extends Factory<Entity> {
	public static final Field wrapperField;
	public static final Field rigidBodyField;

	static {
		try {
			wrapperField = Entity.class.getDeclaredField("wrapper");
			wrapperField.setAccessible(true);
			rigidBodyField = Entity.class.getDeclaredField("rigidBody");
			rigidBodyField.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NovaException();
		}
	}

	public EntityFactory(Supplier<Entity> constructor) {
		super(constructor);
	}

	public Entity makeEntity(EntityWrapper wrapper, RigidBody rigidBody) {
		Entity newEntity = constructor.get();

		try {
			wrapperField.set(newEntity, wrapper);
			rigidBodyField.set(newEntity, rigidBody);
		} catch (Exception e) {
			throw new NovaException();
		}

		return newEntity;
	}
}
