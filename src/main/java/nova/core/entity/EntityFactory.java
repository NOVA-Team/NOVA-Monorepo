package nova.core.entity;

import nova.core.util.Factory;
import nova.core.util.WrapperProvider;
import nova.core.util.exception.NovaException;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * @author Calclavia
 */
public class EntityFactory extends Factory<Entity> {
	public static final Field wrapperField;

	static {
		try {
			wrapperField = WrapperProvider.class.getDeclaredField("wrapper");
			wrapperField.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NovaException();
		}
	}

	//TODO: This is not the optimal way, especially when we have more arguments to pass...
	public EntityFactory(Function<Object[], Entity> constructor) {
		super(constructor);
	}

	public Entity makeEntity(EntityWrapper wrapper, Object... args) {
		Entity newEntity = constructor.apply(args);

		try {
			wrapperField.set(newEntity, wrapper);
		} catch (Exception e) {
			throw new NovaException();
		}

		return newEntity;
	}
}
