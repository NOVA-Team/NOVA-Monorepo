package nova.core.util;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public class ReflectionUtils {
	private ReflectionUtils() {

	}

	public static void forEachStoredField(Object source, BiConsumer<Field, String> action) {
		for (Field f : source.getClass().getFields()) {
			if (f.isAnnotationPresent(Stored.class)) {
				String key = f.getAnnotation(Stored.class).key();
				if (key.length() == 0) {
					key = f.getName();
				}

				action.accept(f, key);
			}
		}
	}
}
