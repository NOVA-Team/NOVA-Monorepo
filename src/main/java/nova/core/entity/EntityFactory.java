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

	static {
		try {
			wrapperField = Entity.class.getDeclaredField("wrapper");
			wrapperField.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NovaException();
		}
	}

	public EntityFactory(Supplier<Entity> constructor) {
		super(constructor);
	}

	public Entity makeEntity(EntityWrapper wrapper) {
		Entity newEntity = constructor.get();

		try {
			wrapperField.set(newEntity, wrapper);
		} catch (Exception e) {
			throw new NovaException();
		}

		return newEntity;
	}
}
