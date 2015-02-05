package nova.core.util;

import nova.core.util.components.Stored;
import nova.core.util.exception.NovaException;

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

	/**
	 * Invokes action for each field in source object annotated with {@link nova.core.util.components.Stored}
	 *
	 * @param source Object to iterate over
	 * @param action Action to do
	 */
	public static void forEachStoredField(Object source, BiConsumer<Field, String> action) {
		forEachAnnotatedField(Stored.class, source, (field, annotation) -> {
			String key = annotation.key();
			if (key.length() == 0) {
				key = field.getName();
			}

			action.accept(field, key);
		});
	}

	/**
	 * Invokes an action on each field annotated with specified annotation of given object
	 *
	 * @param <T> Annotation type
	 * @param annotation Annotation type
	 * @param source Object to scan
	 * @param action Action to invoke
	 */
	public static <T extends Annotation> void forEachAnnotatedField(Class<? extends T> annotation, Object source, BiConsumer<Field, T> action) {
		for (Field f : source.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(annotation)) {
				f.setAccessible(true);
				action.accept(f, f.getAnnotation(annotation));
				f.setAccessible(false);
			}
		}
	}
}
