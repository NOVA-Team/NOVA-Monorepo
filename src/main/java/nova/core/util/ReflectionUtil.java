package nova.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import nova.core.util.exception.NovaException;

import com.google.common.primitives.Primitives;

public class ReflectionUtil {

	/**
	 * Array that contains the primitive wrappers in the order they can be
	 * converted without a type-cast.
	 */
	private static final List<Class<?>> PRIMITIVE_WIDENING = Arrays.asList(Byte.class, Short.class, Character.class, Integer.class, Long.class, Float.class, Double.class);

	private ReflectionUtil() {
	}

	private static Class<?>[] types(Object... args) {
		return Arrays.stream(args).map(arg -> arg.getClass()).toArray(size -> new Class[size]);
	}

	static float calculateDistancePrimitive(Class<?> a, Class<?> b) {

		// Nothing can be converted to a char or a boolean.
		if (b == Character.class || b == Boolean.class)
			return -1F;
		// char acts like a short.
		if (a == Character.class)
			a = Short.class;

		int indexA = PRIMITIVE_WIDENING.indexOf(a);
		int indexB = PRIMITIVE_WIDENING.indexOf(b);

		if (indexA < 0 || indexB < 0)
			return -1F;
		return (indexB - indexA) / 10F;
	}

	static float calculateDistance(Class<?> a, Class<?> b) {
		float cost = 0F;
		if (b.isPrimitive()) {
			b = Primitives.wrap(b);
			if (!a.isPrimitive()) {
				cost += 0.1F; // Penalty for unwrapping
			}
			a = Primitives.wrap(a);

			cost += calculateDistancePrimitive(a, b);
			return cost;
		} else {
			while (a != null && !a.equals(b)) {
				if (a.isInterface() && a.isAssignableFrom(b)) {
					// Interface match
					cost += 0.25F;
					break;
				}
				cost++;
				a = a.getSuperclass();
			}
			if (a == null) {
				// Object match
				cost += 1.5F;
			}
		}
		return cost;
	}

	static float calculateDistance(Executable exec, Class<?>[] parameterTypes) {
		float cost = 0;

		Class<?>[] execTypes = exec.getParameterTypes();
		for (int i = 0; i < exec.getParameterCount(); i++) {
			if (i >= parameterTypes.length && exec.isVarArgs())
				break;

			Class<?> a = parameterTypes[i];
			Class<?> b = execTypes[i];

			if (i == exec.getParameterCount() - 1 && exec.isVarArgs()) {
				if (isAssignmentCompatible(a, b)) {
					// Passed array for var-args.
					cost += calculateDistance(a, b);
				} else {
					cost += calculateDistance(a, b.getComponentType());
					// Penalty for every parameter that wasn't used.
					cost += (parameterTypes.length - exec.getParameterCount()) * 3F;
					// Death penalty for using var-args.
					cost += 10F;
				}
			} else {
				cost += calculateDistance(a, b);
			}
		}
		return cost;
	}

	static int compareDistance(Executable a, Executable b, Class<?>[] parameterTypes) {
		float distA = calculateDistance(a, parameterTypes);
		float distB = calculateDistance(b, parameterTypes);
		return Float.compare(distA, distB);
	}

	static boolean isAssignmentCompatible(Executable exec, Class<?>... parameterTypes) {

		// First checks to eliminate an obvious mismatch.
		if (exec.isVarArgs()) {
			if (exec.getParameterCount() == 1 && parameterTypes.length == 0)
				return true;
			if (parameterTypes.length < exec.getParameterCount() - 1)
				return false;
		} else if (parameterTypes.length != exec.getParameterCount()) {
			return false;
		}

		Class<?>[] execTypes = exec.getParameterTypes();
		for (int i = 0; i < exec.getParameterCount(); i++) {

			Class<?> a = parameterTypes[i];
			Class<?> b = execTypes[i];

			if (i == exec.getParameterCount() - 1 && exec.isVarArgs()) {

				// Passed array type for var-args
				if (isAssignmentCompatible(a, b)) {
					return true;
				}

				// Var-args, have to check every element against the array type.
				b = b.getComponentType();
				for (int j = i; j < parameterTypes.length; j++) {
					a = parameterTypes[j];
					if (!isAssignmentCompatible(a, b))
						return false;
				}
				return true;
			} else if (!isAssignmentCompatible(a, b)) {
				return false;
			}
		}
		return true;
	}

	static boolean isAssignmentCompatible(Class<?> a, Class<?> b) {
		if (a == b)
			return true;

		// Wrap primitive type
		a = Primitives.wrap(a);

		// Check primitive type assignment compatibility
		if (b.isPrimitive()) {
			b = Primitives.wrap(b);

			float distance = calculateDistancePrimitive(a, b);
			return distance >= 0;

		} else {
			return b.isInstance(b);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Optional<Constructor<T>> findMatchingConstructor(Class<T> clazz, Class<?>... parameterTypes) {

		try {
			// Try the default method as it is much faster, works in
			// many of the cases.
			return Optional.of(clazz.getConstructor(parameterTypes));
		} catch (Exception e) {
		}

		// Aww, snap. Now we have to do it the hard way...
		try {
			return Arrays.stream(clazz.getConstructors())
				.filter(cons -> isAssignmentCompatible(cons, parameterTypes))
				.sorted((consA, consB) -> {
					return compareDistance(consA, consB, parameterTypes);
				})
				.findFirst()
				.map(constr2 -> (Constructor<T>) constr2);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public static <T> T newInstanceMatching(Class<T> clazz, Object... args) {
		try {
			if (args != null && args.length > 0) {
				return findMatchingConstructor(clazz, types(args)).get().newInstance(args);
			} else {
				return clazz.newInstance();
			}
		} catch (Exception e) {
			throw new NovaException(e);
		}
	}

	public static <T> T newInstance(Class<T> clazz, Object... args) {
		try {
			if (args != null && args.length > 0) {
				return clazz.getConstructor(types(args)).newInstance(args);
			}
			return clazz.newInstance();
		} catch (Exception e) {
			throw new NovaException(e);
		}
	}

	/**
	 * Invokes an action on each field annotated with specified annotation of
	 * given object
	 * 
	 * @param <T> Annotation type
	 * @param annotation Annotation type
	 * @param clazz Class to scan
	 * @param action Action to invoke
	 */
	public static <T extends Annotation> void forEachAnnotatedField(Class<? extends T> annotation, Class<?> clazz, BiConsumer<Field, T> action) {
		Arrays.stream(clazz.getDeclaredFields())
			.filter(f -> f.isAnnotationPresent(annotation))
			.forEachOrdered(f -> action.accept(f, f.getAnnotation(annotation)));
	}

	/**
	 * Gets all the annotated fields of this class, including all the parents
	 * classes in the order of hierarchy.
	 * 
	 * @param annotation
	 * @param clazz
	 * @return An ordered map of annotated fields and their annotations from the
	 *         order of the most sub class to the most super class.
	 */
	public static <T extends Annotation> Map<Field, T> getAnnotatedFields(Class<T> annotation, Class<?> clazz) {
		Map<Field, T> fields = new LinkedHashMap<>();
		forEachRecursiveAnnotatedField(annotation, clazz, fields::put);
		return fields;
	}

	// TODO: Cache this?
	public static <T extends Annotation> void forEachRecursiveAnnotatedField(Class<T> annotation, Class<?> clazz, BiConsumer<Field, T> action) {
		Arrays.stream(clazz.getDeclaredFields())
			.filter(f -> f.isAnnotationPresent(annotation))
			.forEachOrdered(f -> action.accept(f, f.getAnnotation(annotation)));

		Class<?> superClass = clazz.getSuperclass();

		if (superClass != null) {
			forEachRecursiveAnnotatedField(annotation, superClass, action);
		}
	}
}
