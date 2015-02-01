package nova.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public class ReflectionUtils {
	private ReflectionUtils() {
	}

	public static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new NovaException();
		}
	}

	public static void forEachStoredField(Object source, BiConsumer<Field, Stored> action) {
		forEachField(Stored.class, source, action);
	}

	public static <T extends Annotation> void forEachField(Class<? extends T> annotation, Object source, BiConsumer<Field, T> action) {
		for (Field f : source.getClass().getFields()) {
			if (f.isAnnotationPresent(annotation)) {
				action.accept(f, f.getAnnotation(annotation));
			}
		}
	}
}
