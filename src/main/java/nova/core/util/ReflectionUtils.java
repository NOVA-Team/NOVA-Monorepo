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

	public static void forEachStoredField(Object source, BiConsumer<Field, String> action) {
		forEachAnnotatedField(Stored.class, source, (field, annotation) -> {
			String key = annotation.key();
			if (key.length() == 0) {
				key = field.getName();
			}
			
			action.accept(field, key);
		});
	}

	public static <T extends Annotation> void forEachAnnotatedField(Class<? extends T> annotation, Object source, BiConsumer<Field, T> action) {
		for (Field f : source.getClass().getFields()) {
			if (f.isAnnotationPresent(annotation)) {
				action.accept(f, f.getAnnotation(annotation));
			}
		}
	}
}
